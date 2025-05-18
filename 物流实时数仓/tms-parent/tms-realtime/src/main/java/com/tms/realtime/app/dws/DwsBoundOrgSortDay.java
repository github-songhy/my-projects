package com.tms.realtime.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.app.func.MyAggregationFunction;
import com.tms.realtime.app.func.MyTriggerFunction;
import com.tms.realtime.beans.DwdBoundSortBean;
import com.tms.realtime.beans.DwsBoundOrgSortDayBean;
import com.tms.realtime.common.TmsConfig;
import com.realtime.tms.realtime.utils.*;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.Collections;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Felix
 * @date 2025/3/29
 * 中转域：机构粒度分拣业务过程聚合统计
 * 需要启动的进程
 * zk、kafka、hdfs、hbase、redis、clickhouse、OdsApp、DwdBoundRelevantApp、DwsBoundOrgSortDay
 * <p>
 * 开发流程
 * -环境准备
 * -从kafka的dwd分拣事实表主题中读取数据
 * -对流中的数据进行类型转换 jsonStr->实体类(维度、度量)
 * -将流中数据 对应的事件时间 +8小时
 * -指定Watermark以及提取事件时间字段
 * -按照机构id进行分组
 * -开窗(窗口大小:1day)
 * 自定义触发器，窗口指定使用自定义触发器
 * -聚合
 * aggregate(
 * 参数1：聚合逻辑
 * 自定义一个聚合函数(适配器设计模式)
 * 参数2：对聚合后的数据进行处理
 * 窗口时间左移8小时
 * )
 * -关联维度(城市、省份)
 * 基本实现
 * 在HbaseUtil中提供两个查询方法，一个是根据主键查询，另一个根据外键查询
 * 在类中调用map算子对流中数据进行维度的补充
 * 优化1：旁路缓存
 * 思路：先从缓存中获取维度数据，如果缓存中有，直接作为方法的返回值返回(缓存命中)；
 * 如果在缓存中没有找到对应的维度，发送请求到hbase表中进行查询，并将查询的结果放到缓存中缓存起来，方便下次查询。
 * 缓存产品选型：
 * Redis  性能不差、维护性好
 * Redis相关的设置
 * key:    dim:维度表名:查询条件(名_值)
 * type:   String
 * ttl:    1day
 * 注意：在维度数据发生变化后，从Redis中将缓存的数据清除掉
 * <p>
 * 优化2：异步IO
 * 为什么要使用异步IO？
 * 因为默认情况下，如果使用map算子对流中数据进行处理，使用的是同步的方式，处理完一个元素后再处理下一个，如果是和外部系统交互，效率较低。
 * 发送异步请求的API
 * AsyncDataStream.[un]orderedWait(
 * 流,
 * 发送异步请求过程,
 * 超时时间,
 * 时间单位
 * )
 * 双重校验锁解决单例设计模式懒汉式线程安全的问题
 * class DimAsyncFunction implements AsyncFunction{
 * asyncInvoke:
 * 获取线程
 * 根据流中的对象获取查询维度的条件-----abstrct
 * 根据查询条件到维度表中获取维度数据
 * 将维度数据相关的属性补充到流中的对象上-----abstrct
 * 将关联维度后的流中对象向下游传递
 * }
 * 模板方法设计模式：在父类中定义实现某一个功能的核心算法骨架(步骤)，但是有些步骤的具体实现延迟到子类中去完成
 * 在不改变父类核心算法骨架的前提下，每一个子类都可以有自己不同的实现方式。
 * <p>
 * -将关联的结果写到CK表中
 * class ClickhouseUtil{
 * getJdbcSink{
 * JdbcSink.sink(
 * sql,
 * 给sql中问号占位符赋值,
 * 执行选项, ---攒批
 * 连接选项
 * )
 * }
 * }
 * 构造者设计模式：在类中定义通用辅助创建外部类对象的内部类，在内部类中定义和外部类向匹配的属性，在内部类中提供给属性赋值的方法，并在赋值之后，
 * 将对象本身返回。好处：链式调用   对象的创建和属性的赋值可以一步搞定
 * Clickhouse物化视图
 */
public class DwsBoundOrgSortDay {
    public static void main(String[] args) throws Exception {
        //TODO 1.环境准备
        //1.1 指定流处理环境以及检查点相关的设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 指定并行度
        env.setParallelism(4);

        //TODO 2.从kafka主题中读取数据
        //2.1 声明消费的主题以及消费者组
        String topic = "tms_dwd_bound_sort";
        String groupId = "dws_bound_org_sort_group";
        //2.2 创建消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据 封装为流
        SingleOutputStreamOperator<String> kafkaStrDS = env
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source")
                .uid("kafka_source");
        // {"id":"32612","orderId":"18759","orgId":"27","sortTime":"2023-05-09 17:00:50","sorterEmpId":"227","ts":1683622850000}
//         kafkaStrDS.print(">>>");

        //TODO 3.对流中的数据进行类型转换 jsonStr->实体类
        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> dwsBoundOrgSortDayBeanDS = kafkaStrDS.map(
                new MapFunction<String, DwsBoundOrgSortDayBean>() {
                    @Override
                    public DwsBoundOrgSortDayBean map(String jsonStr) throws Exception {
                        DwdBoundSortBean dwdBoundSortBean = JSON.parseObject(jsonStr, DwdBoundSortBean.class);
                        DwsBoundOrgSortDayBean sortDayBean = DwsBoundOrgSortDayBean.builder()
                                .orgId(dwdBoundSortBean.getOrgId())
                                .sortCountBase(1L)
                                .ts(dwdBoundSortBean.getTs() + 8 * 60 * 60 * 1000L)
                                .build();
                        return sortDayBean;
                    }
                }
        );

        // dwsBoundOrgSortDayBeanDS.print(">>>>");

        //TODO 4.指定Watermark以及提取事件时间字段
        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withWatermarkDS = dwsBoundOrgSortDayBeanDS.assignTimestampsAndWatermarks(
                WatermarkStrategy
                        .<DwsBoundOrgSortDayBean>forMonotonousTimestamps()
                        .withTimestampAssigner(
                                new SerializableTimestampAssigner<DwsBoundOrgSortDayBean>() {
                                    @Override
                                    public long extractTimestamp(DwsBoundOrgSortDayBean boundOrgSortDayBean, long recordTimestamp) {
                                        return boundOrgSortDayBean.getTs();
                                    }
                                }
                        )
        );


        //TODO 5.按照机构id进行分组
        KeyedStream<DwsBoundOrgSortDayBean, String> keyedDS = withWatermarkDS.keyBy(DwsBoundOrgSortDayBean::getOrgId);


        //TODO 6.开窗
        WindowedStream<DwsBoundOrgSortDayBean, String, TimeWindow> windowDS
                = keyedDS.window(TumblingEventTimeWindows.of(Time.days(1L)));

        //TODO 7.指定自定义的触发器
        WindowedStream<DwsBoundOrgSortDayBean, String, TimeWindow> triggerDS
                = windowDS.trigger(new MyTriggerFunction<DwsBoundOrgSortDayBean>());

        //TODO 8.聚合
        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> aggregateDS = windowDS.aggregate(
                new MyAggregationFunction<DwsBoundOrgSortDayBean>() {
                    @Override
                    public DwsBoundOrgSortDayBean add(DwsBoundOrgSortDayBean value, DwsBoundOrgSortDayBean accumulator) {
                        if (accumulator == null) {
                            // value 和累加器类型相同，return value这就相当于创建一个累加器了
                            //      把第一个过来的数据当做累加器
                            return value;
                        }
                         accumulator.setSortCountBase(accumulator.getSortCountBase() + 1);
                        return accumulator;
                    }
                },
                new ProcessWindowFunction<DwsBoundOrgSortDayBean, DwsBoundOrgSortDayBean, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<DwsBoundOrgSortDayBean> elements, Collector<DwsBoundOrgSortDayBean> out) throws Exception {
                        for (DwsBoundOrgSortDayBean element : elements) {
                            //获取窗口的起始时间
                            long stt = context.window().getStart();
                            //将窗口时间左移8小时  并转换为年月日的形式
                            element.setCurDate(DateFormatUtil.toDate(stt - 8 * 60 * 60 * 1000L));
                            element.setTs(System.currentTimeMillis());
                            out.collect(element);
                        }
                    }
                }
        );

        //TODO 9.关联维度(机构、城市、省份)
        // 未使用多线程
        //我的写法 关联维度 一次性关联
//        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withReginInfoDS = aggregateDS.map(
//                new MapFunction<DwsBoundOrgSortDayBean, DwsBoundOrgSortDayBean>() {
//                    @Override
//                    public DwsBoundOrgSortDayBean map(DwsBoundOrgSortDayBean orgSortDayBean) throws Exception {
//                        //根据流中的对象获取要关联的维度主键
//                        String orgId = orgSortDayBean.getOrgId();
//                        //根据维度的主键到维度表中获取对应的维度对象
//                        JSONObject dimOrgJsonObj = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_organ", Tuple2.of("id", orgId));
//                        //将维度对象的属性补充到流中对象上
//                        orgSortDayBean.setOrgName(dimOrgJsonObj.getString("org_name"));
//                        // 获取区域信息
//                        String reginId = dimOrgJsonObj.getString("region_id");
//                        JSONObject regionInfo = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_region_info", Tuple2.of("id", reginId));
//                        JSONObject cityInfo = null;
//                        if ("1".equals(dimOrgJsonObj.getString("org_level"))) {
//                            // 获取城市信息
//                            cityInfo = regionInfo;
//                        } else if ("2".equals(dimOrgJsonObj.getString("org_level"))){
//                            // 获取城市信息
//                            String cityId = regionInfo.getString("parent_id");
//                            cityInfo = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_region_info", Tuple2.of("id", cityId));
//                        }
//                        // 获取省份信息
//                        String provinceId = cityInfo.getString("parent_id");
//                        JSONObject provinceInfo = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_region_info", Tuple2.of("id", provinceId));
//                        // 补充城市信息
//                        orgSortDayBean.setCityId(cityInfo.getString("id"));
//                        orgSortDayBean.setCityName(cityInfo.getString("name"));
//                        // 补充省份信息
//                        orgSortDayBean.setProvinceId(provinceInfo.getString("id"));
//                        orgSortDayBean.setProvinceName(provinceInfo.getString("name"));
//
//                        return orgSortDayBean;
//                    }
//                }
//        );


        //9.1 关联机构维度  获取机构名称以及机构对应的转运中心的id
//        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withOrgNameDS = aggregateDS.map(
//                new MapFunction<DwsBoundOrgSortDayBean, DwsBoundOrgSortDayBean>() {
//                    @Override
//                    public DwsBoundOrgSortDayBean map(DwsBoundOrgSortDayBean orgSortDayBean) throws Exception {
//                        //根据流中的对象获取要关联的维度主键
//                        String orgId = orgSortDayBean.getOrgId();
//                        //根据维度的主键到维度表中获取对应的维度对象
//                        JSONObject dimOrgJsonObj = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_organ", Tuple2.of("id", orgId));
//                        //将维度对象的属性补充到流中对象上
//                        orgSortDayBean.setOrgName(dimOrgJsonObj.getString("org_name"));
//                        return orgSortDayBean;
//                    }
//                }
//        );

        // 使用多线程
        //我的写法 关联维度 一次性关联
        // 将异步 I/O 操作应用于 DataStream 作为 DataStream 的一次转换操作, 也可采用带重试的方法unorderedWaitRetry
        // AsyncDataStream.unorderedWait方法相当于对流进行map操作,只不过是多线程请求
        // unordered表示使用多线程不保证处理后的数据顺序和处理前的一致
        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withReginInfoDS = AsyncDataStream.unorderedWait(
                aggregateDS,
                new AsyncFunction<DwsBoundOrgSortDayBean, DwsBoundOrgSortDayBean>() {
                    @Override
                    public void asyncInvoke(DwsBoundOrgSortDayBean orgSortDayBean, ResultFuture<DwsBoundOrgSortDayBean> resultFuture) throws Exception {
                        // 从线程池获取线程
                        ThreadPoolExecutor threadPool = ThreadPoolUtil.getInstance();
                        // 提交线程任务
                        threadPool.submit(new Runnable() {
                            @Override
                            public void run() {
                                //根据流中的对象获取要关联的维度主键
                                String orgId = orgSortDayBean.getOrgId();
                                //根据维度的主键到维度表中获取对应的维度对象
                                JSONObject dimOrgJsonObj = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_organ", Tuple2.of("id", orgId));
                                //将维度对象的属性补充到流中对象上
                                orgSortDayBean.setOrgName(dimOrgJsonObj.getString("org_name"));
                                // 获取区域信息
                                String reginId = dimOrgJsonObj.getString("region_id");
                                JSONObject regionInfo = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_region_info", Tuple2.of("id", reginId));
                                JSONObject cityInfo = JSONObject.parseObject("");
                                if ("1".equals(dimOrgJsonObj.getString("org_level"))) {
                                    // 获取城市信息
                                    cityInfo = regionInfo;
                                } else if ("2".equals(dimOrgJsonObj.getString("org_level"))) {
                                    // 获取城市信息
                                    String cityId = regionInfo.getString("parent_id");
                                    cityInfo = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_region_info", Tuple2.of("id", cityId));
                                }
                                // 获取省份信息
                                String provinceId = cityInfo.getString("parent_id");
                                JSONObject provinceInfo = DimUtil.getDimInfo(TmsConfig.HBASE_NAMESPACE, "dim_base_region_info", Tuple2.of("id", provinceId));
                                // 补充城市信息
                                orgSortDayBean.setCityId(cityInfo.getString("id"));
                                orgSortDayBean.setCityName(cityInfo.getString("name"));
                                // 补充省份信息
                                orgSortDayBean.setProvinceId(provinceInfo.getString("id"));
                                orgSortDayBean.setProvinceName(provinceInfo.getString("name"));

                                //向下游传递数据
                                resultFuture.complete(Collections.singleton(orgSortDayBean));
                            }
                        });
                    }
                },
                // 超时时间
                60,
                TimeUnit.SECONDS
        );

        // 使用多线程 + 模版方法设计模式 分步关联维度
        //9.1 关联机构维度  获取机构名称以及机构对应的转运中心的id
//        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withOrgNameDS = AsyncDataStream.unorderedWait(
//            aggregateDS,
//            // 实现分发请求的 AsyncFunction
//            new DimAsyncFunction<DwsBoundOrgSortDayBean>("dim_base_organ") {
//                @Override
//                public void join(DwsBoundOrgSortDayBean sortDayBean, JSONObject dimInfoJsonObj) {
//                    sortDayBean.setOrgName(dimInfoJsonObj.getString("org_name"));
//                    String orgParentId = dimInfoJsonObj.getString("org_parent_id");
//                    sortDayBean.setJoinOrgId(orgParentId != null?orgParentId:sortDayBean.getOrgId());
//                }
//
//                @Override
//                public Tuple2<String, String> getCondition(DwsBoundOrgSortDayBean sortDayBean) {
//                    return Tuple2.of("id",sortDayBean.getOrgId());
//                }
//            },
//            60,
//            TimeUnit.SECONDS
//        );

////         withOrgNameDS.print(">>>");
//        //9.2 关联机构维度表  根据转运中心的id获取当前转运中心对应的城市id
//        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withCityIdDS = AsyncDataStream.unorderedWait(
//            withOrgNameDS,
//            new DimAsyncFunction<DwsBoundOrgSortDayBean>("dim_base_organ") {
//                @Override
//                public void join(DwsBoundOrgSortDayBean orgSortDayBean, JSONObject dimInfoJsonObj) {
//                    orgSortDayBean.setCityId(dimInfoJsonObj.getString("region_id"));
//                }
//
//                @Override
//                public Tuple2<String, String> getCondition(DwsBoundOrgSortDayBean orgSortDayBean) {
//                    return Tuple2.of("id", orgSortDayBean.getJoinOrgId());
//                }
//            },
//            60,
//            TimeUnit.SECONDS
//        );
//        // withCityIdDS.print(">>>>");
//
//        //9.3 关联地区维度表  根据城市的id获取城市名称以及当前城市所属的省份id
//        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withCityNameAndProvinceIdDS = AsyncDataStream.unorderedWait(
//            withCityIdDS,
//            new DimAsyncFunction<DwsBoundOrgSortDayBean>("dim_base_region_info") {
//                @Override
//                public void join(DwsBoundOrgSortDayBean sortDayBean, JSONObject dimInfoJsonObj) {
//                    sortDayBean.setCityName(dimInfoJsonObj.getString("name"));
//                    sortDayBean.setProvinceId(dimInfoJsonObj.getString("parent_id"));
//                }
//
//                @Override
//                public Tuple2<String, String> getCondition(DwsBoundOrgSortDayBean sortDayBean) {
//                    return Tuple2.of("id", sortDayBean.getCityId());
//                }
//            },
//            60, TimeUnit.SECONDS
//        );
//
//        //9.4 关联地区维度表  根据省份的id获取省份的名称
//        SingleOutputStreamOperator<DwsBoundOrgSortDayBean> withProvinceDS = AsyncDataStream.unorderedWait(
//            withCityNameAndProvinceIdDS,
//            new DimAsyncFunction<DwsBoundOrgSortDayBean>("dim_base_region_info") {
//                @Override
//                public void join(DwsBoundOrgSortDayBean sortDayBean, JSONObject dimInfoJsonObj) {
//                    sortDayBean.setProvinceName(dimInfoJsonObj.getString("name"));
//                }
//
//                @Override
//                public Tuple2<String, String> getCondition(DwsBoundOrgSortDayBean sortDayBean) {
//                    return Tuple2.of("id", sortDayBean.getProvinceId());
//                }
//            },
//            60, TimeUnit.SECONDS
//        );
//
//        withProvinceDS.print(">>>>");

        withReginInfoDS.print(">>>>");

//        //TODO 10.将关联的结果写到CK表中
//        withProvinceDS.addSink(
//            ClickHouseUtil.getJdbcSink("insert into dws_bound_org_sort_day_base values(?,?,?,?,?,?,?,?,?)")
//        );

        env.execute();
    }
}



