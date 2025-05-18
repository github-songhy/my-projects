package com.tms.realtime.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.app.func.DimAsyncFunction;
import com.tms.realtime.app.func.MyAggregationFunction;
import com.tms.realtime.app.func.MyTriggerFunction;
import com.tms.realtime.beans.DwdTradeOrderDetailBean;
import com.tms.realtime.beans.DwsTradeOrgOrderDayBean;
import com.tms.realtime.utils.ClickHouseUtil;
import com.tms.realtime.utils.CreateEnvUtil;
import com.tms.realtime.utils.DateFormatUtil;
import com.tms.realtime.utils.KafkaUtil;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * @author Felix
 * @date 2025/3/1
 * 交易域：机构粒度下单聚合统计
 * 需要启动的进程
 *      zk、kafka、hdfs、hbase、redis、clickhouse、OdsApp、DwdOrderRelevantApp、DwsTradeOrgOrderDay
 */
public class DwsTradeOrgOrderDay {
    public static void main(String[] args) throws Exception {
        //TODO 1.环境准备
        //1.1 指定流处理环境以及检查点相关设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 指定并行度
        env.setParallelism(4);
        //TODO 2.从kafka的下单事实表中读取数据
        //2.1 声明消费的主题以及消费者组
        String topic = "tms_dwd_trade_order_detail";
        String groupId = "dws_trade_org_order_group";
        //2.2 创建消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据 封装为流
        DataStreamSource<String> kafkaStrDS = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source");
        //TODO 3.对读取的数据进行类型转换
        SingleOutputStreamOperator<DwsTradeOrgOrderDayBean> mapDS = kafkaStrDS.map(
            new MapFunction<String, DwsTradeOrgOrderDayBean>() {
                @Override
                public DwsTradeOrgOrderDayBean map(String jsonStr) throws Exception {
                    DwdTradeOrderDetailBean dwdTradeOrderDetailBean = JSON.parseObject(jsonStr, DwdTradeOrderDetailBean.class);
                    DwsTradeOrgOrderDayBean bean = DwsTradeOrgOrderDayBean.builder()
                        .senderDistrictId(dwdTradeOrderDetailBean.getSenderDistrictId())
                        .cityId(dwdTradeOrderDetailBean.getSenderCityId())
                        .orderAmountBase(dwdTradeOrderDetailBean.getAmount())
                        .orderCountBase(1L)
                        .ts(dwdTradeOrderDetailBean.getTs() + 8 * 60 * 60 * 1000L)
                        .build();
                    return bean;
                }
            }
        );
        //TODO 4.关联机构维度
        SingleOutputStreamOperator<DwsTradeOrgOrderDayBean> withOrgDS = AsyncDataStream.unorderedWait(
            mapDS,
            new DimAsyncFunction<DwsTradeOrgOrderDayBean>("dim_base_organ") {
                @Override
                public void join(DwsTradeOrgOrderDayBean bean, JSONObject dimInfoJsonObj) {
                    bean.setOrgId(dimInfoJsonObj.getString("id"));
                    bean.setOrgName(dimInfoJsonObj.getString("org_name"));
                }

                @Override
                public Tuple2<String, String> getCondition(DwsTradeOrgOrderDayBean bean) {
                    return Tuple2.of("region_id",bean.getSenderDistrictId());
                }
            },
            60, TimeUnit.SECONDS
        );
        //TODO 5.指定Watermark以及提取事件时间字段
        SingleOutputStreamOperator<DwsTradeOrgOrderDayBean> withWatermarkDS = withOrgDS.assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<DwsTradeOrgOrderDayBean>forMonotonousTimestamps()
                .withTimestampAssigner(
                    new SerializableTimestampAssigner<DwsTradeOrgOrderDayBean>() {
                        @Override
                        public long extractTimestamp(DwsTradeOrgOrderDayBean element, long recordTimestamp) {
                            return element.getTs();
                        }
                    }
                )
        );
        //TODO 6.按照机构id进行分组
        KeyedStream<DwsTradeOrgOrderDayBean, String> keyedDS = withWatermarkDS.keyBy(DwsTradeOrgOrderDayBean::getOrgId);

        //TODO 7.开窗
        WindowedStream<DwsTradeOrgOrderDayBean, String, TimeWindow> windowDS = keyedDS.window(TumblingEventTimeWindows.of(Time.days(1)));

        //TODO 8.指定自定义触发器
        WindowedStream<DwsTradeOrgOrderDayBean, String, TimeWindow> triggerDS = windowDS.trigger(new MyTriggerFunction<DwsTradeOrgOrderDayBean>());

        //TODO 9.聚合
        SingleOutputStreamOperator<DwsTradeOrgOrderDayBean> aggregateDS = triggerDS.aggregate(
            new MyAggregationFunction<DwsTradeOrgOrderDayBean>() {
                @Override
                public DwsTradeOrgOrderDayBean add(DwsTradeOrgOrderDayBean value, DwsTradeOrgOrderDayBean accumulator) {
                    if (accumulator == null) {
                        return value;
                    }
                    accumulator.setOrderAmountBase(value.getOrderAmountBase().add(accumulator.getOrderAmountBase()));
                    accumulator.setOrderCountBase(value.getOrderCountBase() + accumulator.getOrderCountBase());
                    return accumulator;
                }
            },
            new ProcessWindowFunction<DwsTradeOrgOrderDayBean, DwsTradeOrgOrderDayBean, String, TimeWindow>() {
                @Override
                public void process(String s, Context context, Iterable<DwsTradeOrgOrderDayBean> elements, Collector<DwsTradeOrgOrderDayBean> out) throws Exception {
                    long stt = context.window().getStart() - 8 * 60 * 60 * 1000;
                    String curDate = DateFormatUtil.toDate(stt);
                    for (DwsTradeOrgOrderDayBean bean : elements) {
                        bean.setCurDate(curDate);
                        bean.setTs(System.currentTimeMillis());
                        out.collect(bean);
                    }
                }
            }
        );
        //TODO 10.补充城市维度信息
        SingleOutputStreamOperator<DwsTradeOrgOrderDayBean> withCityDS = AsyncDataStream.unorderedWait(
            aggregateDS,
            new DimAsyncFunction<DwsTradeOrgOrderDayBean>("dim_base_region_info") {
                @Override
                public void join(DwsTradeOrgOrderDayBean bean, JSONObject dimInfoJsonObj) {
                    bean.setCityName(dimInfoJsonObj.getString("name"));
                }

                @Override
                public Tuple2<String, String> getCondition(DwsTradeOrgOrderDayBean bean) {
                    return Tuple2.of("id",bean.getCityId());
                }
            },
            60, TimeUnit.SECONDS
        );
        //TODO 11.将结果写到CK中
        withCityDS.print(">>>>");
        withCityDS.addSink(
            ClickHouseUtil.getJdbcSink("insert into dws_trade_org_order_day_base values(?,?,?,?,?,?,?,?)")
        );

        env.execute();
    }
}
