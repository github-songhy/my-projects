package com.tms.realtime.app.dws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.app.func.DimAsyncFunction;
import com.tms.realtime.app.func.MyAggregationFunction;
import com.tms.realtime.app.func.MyTriggerFunction;
import com.tms.realtime.beans.DwdTradeOrderDetailBean;
import com.tms.realtime.beans.DwsTradeCargoTypeOrderDayBean;
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
 * 交易域：货物类型下单数以及下单金额聚合统计
 * 需要启动的进程
 *      zk、kafka、hdfs、hbase、redis、clickhouse、OdsApp、DwdOrderRelevantApp、DwsTradeCargoTypeOrderDay
 *
 */
public class DwsTradeCargoTypeOrderDay {
    public static void main(String[] args) throws Exception {
        //TODO 1.环境准备
        //1.1 指定流处理环境以及检查点相关的设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 设置并行度
        env.setParallelism(4);
        //TODO 2.从kafka的下单事实表中读取数据
        //2.1 声明消费的主题以及消费者组
        String topic = "tms_dwd_trade_order_detail";
        String groupId = "dws_trade_cargo_type_order_group";
        //2.2 创建消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据 封装为流
        DataStreamSource<String> kafkaStrDS
            = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source");

        //TODO 3.对流中的数据进行类型转换   jsonStr->实体类对象
        SingleOutputStreamOperator<DwsTradeCargoTypeOrderDayBean> mapDS = kafkaStrDS.map(
            new MapFunction<String, DwsTradeCargoTypeOrderDayBean>() {
                @Override
                public DwsTradeCargoTypeOrderDayBean map(String jsonStr) throws Exception {
                    DwdTradeOrderDetailBean dwdTradeOrderDetailBean = JSON.parseObject(jsonStr, DwdTradeOrderDetailBean.class);
                    DwsTradeCargoTypeOrderDayBean bean = DwsTradeCargoTypeOrderDayBean.builder()
                        .cargoType(dwdTradeOrderDetailBean.getCargoType())
                        .orderAmountBase(dwdTradeOrderDetailBean.getAmount())
                        .orderCountBase(1L)
                        .ts(dwdTradeOrderDetailBean.getTs() + 8 * 60 * 60 * 1000)
                        .build();
                    return bean;
                }
            }
        );
        //TODO 4.指定Watermark以及提取事件时间字段
        SingleOutputStreamOperator<DwsTradeCargoTypeOrderDayBean> withWatermarkDS = mapDS.assignTimestampsAndWatermarks(
            WatermarkStrategy
                .<DwsTradeCargoTypeOrderDayBean>forMonotonousTimestamps()
                .withTimestampAssigner(
                    new SerializableTimestampAssigner<DwsTradeCargoTypeOrderDayBean>() {
                        @Override
                        public long extractTimestamp(DwsTradeCargoTypeOrderDayBean element, long recordTimestamp) {
                            return element.getTs();
                        }
                    }
                )
        );
        //TODO 5.按照货物类型进行分组
        KeyedStream<DwsTradeCargoTypeOrderDayBean, String> keyedDS = withWatermarkDS.keyBy(DwsTradeCargoTypeOrderDayBean::getCargoType);
        //TODO 6.开窗
        WindowedStream<DwsTradeCargoTypeOrderDayBean, String, TimeWindow> windowDS = keyedDS.window(TumblingEventTimeWindows.of(Time.days(1)));
        //TODO 7.指定自定义触发器
        WindowedStream<DwsTradeCargoTypeOrderDayBean, String, TimeWindow> triggerDS = windowDS.trigger(new MyTriggerFunction<DwsTradeCargoTypeOrderDayBean>());
        //TODO 8.聚合计算
        SingleOutputStreamOperator<DwsTradeCargoTypeOrderDayBean> aggregateDS = triggerDS.aggregate(
            new MyAggregationFunction<DwsTradeCargoTypeOrderDayBean>() {
                @Override
                public DwsTradeCargoTypeOrderDayBean add(DwsTradeCargoTypeOrderDayBean value, DwsTradeCargoTypeOrderDayBean accumulator) {
                    if (accumulator == null) {
                        return value;
                    }
                    accumulator.setOrderAmountBase(value.getOrderAmountBase().add(accumulator.getOrderAmountBase()));
                    accumulator.setOrderCountBase(value.getOrderCountBase() + accumulator.getOrderCountBase());
                    return accumulator;
                }
            },
            new ProcessWindowFunction<DwsTradeCargoTypeOrderDayBean, DwsTradeCargoTypeOrderDayBean, String, TimeWindow>() {
                @Override
                public void process(String s, Context context, Iterable<DwsTradeCargoTypeOrderDayBean> elements, Collector<DwsTradeCargoTypeOrderDayBean> out) throws Exception {
                    long stt = context.window().getStart() - 8 * 60 * 60 * 1000L;
                    String curDate = DateFormatUtil.toDate(stt);
                    for (DwsTradeCargoTypeOrderDayBean bean : elements) {
                        bean.setCurDate(curDate);
                        bean.setTs(System.currentTimeMillis());
                        out.collect(bean);
                    }
                }
            }
        );
        //TODO 9.关联货物维度
        SingleOutputStreamOperator<DwsTradeCargoTypeOrderDayBean> withCargoTypeDS = AsyncDataStream.unorderedWait(
            aggregateDS,
            new DimAsyncFunction<DwsTradeCargoTypeOrderDayBean>("dim_base_dic") {
                @Override
                public void join(DwsTradeCargoTypeOrderDayBean bean, JSONObject dimInfoJsonObj) {
                    bean.setCargoTypeName(dimInfoJsonObj.getString("name"));
                }

                @Override
                public Tuple2<String, String> getCondition(DwsTradeCargoTypeOrderDayBean bean) {
                    return Tuple2.of("id",bean.getCargoType());
                }
            },
            60, TimeUnit.SECONDS
        );
        //TODO 10.将结果写到CK
        withCargoTypeDS.print(">>>>");
        withCargoTypeDS.addSink(
            ClickHouseUtil.getJdbcSink("insert into dws_trade_cargo_type_order_day_base values(?,?,?,?,?,?)")
        );

        env.execute();
    }
}
