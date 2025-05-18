package com.tms.realtime.app.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.beans.DwdBoundInboundBean;
import com.tms.realtime.beans.DwdBoundOutboundBean;
import com.tms.realtime.beans.DwdBoundSortBean;
import com.tms.realtime.beans.DwdOrderOrgBoundOriginBean;
import com.tms.realtime.utils.CreateEnvUtil;
import com.tms.realtime.utils.DateFormatUtil;
import com.tms.realtime.utils.KafkaUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.SideOutputDataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author Felix
 * @date 2025/3/28
 * 中转域相关事实表准备
 * 需要启动的进程
 *      zk、kafka、hdfs、OdsApp、DwdBoundRelevantApp
 */
public class DwdBoundRelevantApp {
    public static void main(String[] args) throws Exception {
        //TODO 1.环境准备
        //1.1 指定流出来环境以及检查点相关的设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 设置并行度
        env.setParallelism(4);

        //TODO 2.从kakfa的主题中读取数据
        //2.1 声明消费的主题以及消费者组
        String topic = "tms_ods";
        String groupId = "dwd_bound_group";
        //2.2 创建消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据 封装为流
        SingleOutputStreamOperator<String> kafkaStrDS = env
            .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source")
            .uid("kafka_source");

        //TODO 3.筛选出订单机构中转表
        SingleOutputStreamOperator<String> filterDS = kafkaStrDS.filter(
            new FilterFunction<String>() {
                @Override
                public boolean filter(String jsonStr) throws Exception {
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    String table = jsonObj.getJSONObject("source").getString("table");
                    return "order_org_bound".equals(table);
                }
            }
        );
        filterDS.print("+++");

        //TODO 4.定义侧输出流标签
        //分拣侧输出流标签
        OutputTag<String> sortTag = new OutputTag<String>("sortTag") {};
        //出库侧输出流标签
        OutputTag<String> outboundTag = new OutputTag<String>("outboundTag") {};

        //TODO 5.分流  入库->主流     分拣->分拣侧输出流  出库->出库侧输出流
        SingleOutputStreamOperator<String> inboundDS = filterDS.process(
            new ProcessFunction<String, String>() {
                @Override
                public void processElement(String jsonStr, Context ctx, Collector<String> out) throws Exception {
                    //将jsonStr转换为jsonObj
                    JSONObject jsonObj = JSON.parseObject(jsonStr);

                    DwdOrderOrgBoundOriginBean before = jsonObj.getObject("before", DwdOrderOrgBoundOriginBean.class);
                    DwdOrderOrgBoundOriginBean after = jsonObj.getObject("after", DwdOrderOrgBoundOriginBean.class);

                    //获取中转数据id
                    String id = after.getId();
                    //获取运单id
                    String orderId = after.getOrderId();
                    //获取机构id
                    String orgId = after.getOrgId();

                    //获取操作类型
                    String op = jsonObj.getString("op");
                    if ("c".equals(op)) {
                        Long ts = Long.parseLong(after.getInboundTime()) - 8 * 60 * 60 * 1000L;
                        String inboundTime = DateFormatUtil.toYmdHms(ts);
                        String inboundEmpId = after.getInboundEmpId();
                        //入库   入库操作数据放到主流
                        DwdBoundInboundBean inboundBean = DwdBoundInboundBean.builder()
                            .id(id)
                            .orderId(orderId)
                            .orgId(orgId)
                            .inboundTime(inboundTime)
                            .inboundEmpId(inboundEmpId)
                            .ts(ts)
                            .build();
                        out.collect(JSON.toJSONString(inboundBean));
                    } else {
                        //分拣或者出库
                        //筛选分拣操作  将分拣数据放到分拣侧输出流
                        String beforeSortTime = before.getSortTime();
                        String afterSortTime = after.getSortTime();
                        if (beforeSortTime == null && afterSortTime != null) {
                            Long ts = Long.parseLong(after.getSortTime()) - 8 * 60 * 60 * 1000L;
                            String sortTime = DateFormatUtil.toYmdHms(ts);
                            String sorterEmpId = after.getSorterEmpId();
                            DwdBoundSortBean sortBean = DwdBoundSortBean.builder()
                                .id(id)
                                .orderId(orderId)
                                .orgId(orgId)
                                .sortTime(sortTime)
                                .sorterEmpId(sorterEmpId)
                                .ts(ts)
                                .build();
                            ctx.output(sortTag, JSON.toJSONString(sortBean));
                        }

                        //筛选出库操作  将出库数据放到出库侧输出流
                        String beforeOutboundTime = before.getOutboundTime();
                        String afterOutboundTime = after.getOutboundTime();
                        if (beforeOutboundTime == null && afterOutboundTime != null) {
                            Long ts = Long.parseLong(after.getOutboundTime()) - 8 * 60 * 60 * 1000L;
                            String outboundTime = DateFormatUtil.toYmdHms(ts);
                            String outboundEmpId = after.getOutboundEmpId();
                            DwdBoundOutboundBean outboundBean = DwdBoundOutboundBean.builder()
                                .id(id)
                                .orderId(orderId)
                                .orgId(orgId)
                                .outboundTime(outboundTime)
                                .outboundEmpId(outboundEmpId)
                                .ts(ts)
                                .build();
                            ctx.output(outboundTag, JSON.toJSONString(outboundBean));
                        }
                    }
                }
            }
        );

        //TODO 6.从主流中提取侧输出流
        //分拣流
        SideOutputDataStream<String> sortDS = inboundDS.getSideOutput(sortTag);
        //出库流
        SideOutputDataStream<String> outboundDS = inboundDS.getSideOutput(outboundTag);

        //TODO 7.将不同流的数据写到kafka的主题
        //中转域入库事实主题
        String inboundTopic = "tms_dwd_bound_inbound";
        //中转域分拣事实主题
        String sortTopic = "tms_dwd_bound_sort";
        //中转域出库事实主题
        String outboundTopic = "tms_dwd_bound_outbound";

        inboundDS.print(">>>");
        inboundDS.sinkTo(KafkaUtil.getKafkaSink(inboundTopic,args)).uid("inbound_sink");
        sortDS.print("###");
        sortDS.sinkTo(KafkaUtil.getKafkaSink(sortTopic,args)).uid("sort_sink");
        outboundDS.print("@@@");
        outboundDS.sinkTo(KafkaUtil.getKafkaSink(outboundTopic,args)).uid("outbound_sink");

        env.execute();
    }
}
