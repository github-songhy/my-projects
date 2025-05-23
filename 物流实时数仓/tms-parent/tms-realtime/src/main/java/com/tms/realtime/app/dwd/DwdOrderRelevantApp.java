package com.tms.realtime.app.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.realtime.tms.realtime.beans.*;
import com.tms.realtime.utils.CreateEnvUtil;
import com.tms.realtime.utils.KafkaUtil;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.StateTtlConfig;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

/**
 * @author Felix
 * @date 2025/3/27
 * 订单相关事实表准备
 * 需要启动的进程
 *      zk、kafka、hdfs、OdsApp、DwdOrderRelevantApp
 */
public class DwdOrderRelevantApp {
    public static void main(String[] args) throws Exception {
        //TODO 1.环境准备
        //1.1 指定流处理环境以及检查点相关的设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 设置并行度
        env.setParallelism(4);

        //TODO 2.从kafka的tms_ods主题中读取
        //2.1 声明消费的主题
        String topic = "tms_ods";
        String groupId = "dwd_order_relevant_group";
        //2.2 创建消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据  封装为流
        SingleOutputStreamOperator<String> kafkaStrDS = env
            .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source")
            .uid("kafka_source");

        //TODO 3.筛选订单和订单明细数据
        SingleOutputStreamOperator<String> filterDS = kafkaStrDS.filter(
            new FilterFunction<String>() {
                @Override
                public boolean filter(String jsonStr) throws Exception {
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    String tableName = jsonObj.getJSONObject("source").getString("table");
                    return "order_info".equals(tableName) || "order_cargo".equals(tableName);
                }
            }
        );
        //TODO 4.对流中的数据类型进行转换   jsonStr->jsonObj
        SingleOutputStreamOperator<JSONObject> jsonObjDS = filterDS.map(
            new MapFunction<String, JSONObject>() {
                @Override
                public JSONObject map(String jsonStr) throws Exception {
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    String tableName = jsonObj.getJSONObject("source").getString("table");
                    jsonObj.put("table", tableName);
                    jsonObj.remove("source");
                    jsonObj.remove("transaction");
                    return jsonObj;
                }
            }
        );

        // jsonObjDS.print(">>>");

        //TODO 5.按照order_id进行分组
        KeyedStream<JSONObject, String> keyedDS = jsonObjDS.keyBy(
            new KeySelector<JSONObject, String>() {
                @Override
                public String getKey(JSONObject jsonObj) throws Exception {
                    String table = jsonObj.getString("table");
                    if ("order_info".equals(table)) {
                        return jsonObj.getJSONObject("after").getString("id");
                    }
                    return jsonObj.getJSONObject("after").getString("order_id");
                }
            }
        );

        //TODO 6.定义侧输出流标签 下单放到主流，支付成功、取消运单、揽收(接单)、发单 转运完成、派送成功、签收放到侧输出流

        // 支付成功明细流标签
        OutputTag<String> paySucTag = new OutputTag<String>("dwd_trade_pay_suc_detail") {
        };
        // 取消运单明细流标签
        OutputTag<String> cancelDetailTag = new OutputTag<String>("dwd_trade_cancel_detail") {
        };
        // 揽收明细流标签
        OutputTag<String> receiveDetailTag = new OutputTag<String>("dwd_trans_receive_detail") {
        };
        // 发单明细流标签
        OutputTag<String> dispatchDetailTag = new OutputTag<String>("dwd_trans_dispatch_detail") {
        };
        // 转运完成明细流标签
        OutputTag<String> boundFinishDetailTag = new OutputTag<String>("dwd_trans_bound_finish_detail") {
        };
        // 派送成功明细流标签
        OutputTag<String> deliverSucDetailTag = new OutputTag<String>("dwd_trans_deliver_detail") {
        };
        // 签收明细流标签
        OutputTag<String> signDetailTag = new OutputTag<String>("dwd_trans_sign_detail") {
        };

        //TODO 7.分流
        SingleOutputStreamOperator<String> orderDetailDS = keyedDS.process(
            new KeyedProcessFunction<String, JSONObject, String>() {
                private ValueState<DwdOrderInfoOriginBean> infoBeanState;
                private ValueState<DwdOrderDetailOriginBean> detailBeanState;

                @Override
                public void open(Configuration parameters) throws Exception {
                    ValueStateDescriptor<DwdOrderInfoOriginBean> infoBeanStateDescriptor
                        = new ValueStateDescriptor<DwdOrderInfoOriginBean>("infoBeanState", DwdOrderInfoOriginBean.class);
                    infoBeanStateDescriptor.enableTimeToLive(StateTtlConfig.newBuilder(Time.seconds(5)).build());
                    infoBeanState = getRuntimeContext().getState(infoBeanStateDescriptor);

                    ValueStateDescriptor<DwdOrderDetailOriginBean> detailBeanStateDescriptor
                        = new ValueStateDescriptor<DwdOrderDetailOriginBean>("detailBeanState", DwdOrderDetailOriginBean.class);
                    detailBeanState = getRuntimeContext().getState(detailBeanStateDescriptor);
                }

                @Override
                public void processElement(JSONObject jsonObj, Context ctx, Collector<String> out) throws Exception {
                    //获取处理的表名
                    String table = jsonObj.getString("table");
                    //获取对表的操作类型
                    String op = jsonObj.getString("op");
                    //获取业务数据库表受影响的记录
                    JSONObject data = jsonObj.getJSONObject("after");
                    //判断当前流中处理的是订单还是明细
                    if ("order_info".equals(table)) {
                        //处理的是订单数据
                        DwdOrderInfoOriginBean infoOriginBean = data.toJavaObject(DwdOrderInfoOriginBean.class);

                        // 脱敏
                        String senderName = infoOriginBean.getSenderName();
                        String receiverName = infoOriginBean.getReceiverName();

                        senderName = senderName.charAt(0) + senderName.substring(1).replaceAll(".", "\\*");
                        receiverName = receiverName.charAt(0) + receiverName.substring(1).replaceAll(".", "\\*");

                        infoOriginBean.setSenderName(senderName);
                        infoOriginBean.setReceiverName(receiverName);

                        DwdOrderDetailOriginBean detailOriginBean = detailBeanState.value();
                        if ("c".equals(op)) {
                            //下单
                            if (detailOriginBean == null) {
                                //订单数据 比 订单明细数据先到,将订单数据放到状态中
                                infoBeanState.update(infoOriginBean);
                            } else {
                                //说明订单数据来之前，明细数据已经来到了,将订单和明细进行关联
                                // 属于下单业务过程
                                DwdTradeOrderDetailBean dwdTradeOrderDetailBean = new DwdTradeOrderDetailBean();
                                dwdTradeOrderDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                //将下单业务过程数据 放到主流中
                                out.collect(JSON.toJSONString(dwdTradeOrderDetailBean));
                            }
                        } else if ("u".equals(op) && detailOriginBean != null) {
                            //下单外的其它操作
                            //获取修改前的数据
                            JSONObject oldData = jsonObj.getJSONObject("before");
                            //获取修改前状态的值
                            String oldStatus = oldData.getString("status");
                            //获取修改后状态的值
                            String status = infoOriginBean.getStatus();
                            if (!oldStatus.equals(status)) {
                                //说明修改的status字段
                                String changeLog = oldStatus + " -> " + status;

                                switch (changeLog) {
                                    case "60010 -> 60020":
                                        // 处理支付成功数据
                                        DwdTradePaySucDetailBean dwdTradePaySucDetailBean = new DwdTradePaySucDetailBean();
                                        dwdTradePaySucDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                        ctx.output(paySucTag, JSON.toJSONString(dwdTradePaySucDetailBean));
                                        break;
                                    case "60020 -> 60030":
                                        // 处理揽收明细数据
                                        DwdTransReceiveDetailBean dwdTransReceiveDetailBean = new DwdTransReceiveDetailBean();
                                        dwdTransReceiveDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                        ctx.output(receiveDetailTag, JSON.toJSONString(dwdTransReceiveDetailBean));
                                        break;
                                    case "60040 -> 60050":
                                        // 处理发单明细数据
                                        DwdTransDispatchDetailBean dispatchDetailBean = new DwdTransDispatchDetailBean();
                                        dispatchDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                        ctx.output(dispatchDetailTag, JSON.toJSONString(dispatchDetailBean));
                                        break;
                                    case "60050 -> 60060":
                                        // 处理转运完成明细数据
                                        DwdTransBoundFinishDetailBean boundFinishDetailBean = new DwdTransBoundFinishDetailBean();
                                        boundFinishDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                        ctx.output(boundFinishDetailTag, JSON.toJSONString(boundFinishDetailBean));
                                        break;
                                    case "60060 -> 60070":
                                        // 处理派送成功数据
                                        DwdTransDeliverSucDetailBean dwdTransDeliverSucDetailBean = new DwdTransDeliverSucDetailBean();
                                        dwdTransDeliverSucDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                        ctx.output(deliverSucDetailTag, JSON.toJSONString(dwdTransDeliverSucDetailBean));
                                        break;
                                    case "60070 -> 60080":
                                        // 处理签收明细数据
                                        DwdTransSignDetailBean dwdTransSignDetailBean = new DwdTransSignDetailBean();
                                        dwdTransSignDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                        ctx.output(signDetailTag, JSON.toJSONString(dwdTransSignDetailBean));
                                        // 签收后订单数据不会再发生变化，状态可以清除
                                        detailBeanState.clear();
                                        break;
                                    default:
                                        if (status.equals("60999")) {
                                            DwdTradeCancelDetailBean dwdTradeCancelDetailBean = new DwdTradeCancelDetailBean();
                                            dwdTradeCancelDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                            ctx.output(cancelDetailTag, JSON.toJSONString(dwdTradeCancelDetailBean));
                                            // 取消后订单数据不会再发生变化，状态可以清除
                                            detailBeanState.clear();
                                        }
                                        break;
                                }
                            }
                        }
                    } else {
                        //处理的是订单明细数据
                        DwdOrderDetailOriginBean detailOriginBean = data.toJavaObject(DwdOrderDetailOriginBean.class);
                        if ("c".equals(op)) {
                            //将明细数据放到状态中
                            detailBeanState.update(detailOriginBean);
                            //获取状态中存放的订单数据  注意：只有下单操作，并且订单数据先到，明细数据后到的情况，才会从状态中拿到订单数据
                            DwdOrderInfoOriginBean infoOriginBean = infoBeanState.value();
                            if (infoOriginBean != null) {
                                //属于下单业务过程
                                DwdTradeOrderDetailBean dwdTradeOrderDetailBean = new DwdTradeOrderDetailBean();
                                dwdTradeOrderDetailBean.mergeBean(detailOriginBean, infoOriginBean);
                                //将下单业务过程数据 放到主流中
                                out.collect(JSON.toJSONString(dwdTradeOrderDetailBean));
                            }
                        }
                    }
                }
            }
        ).uid("process_data");

        //TODO 8.从主流中提取侧输出流
        //8.1 支付成功明细流
        DataStream<String> paySucDS = orderDetailDS.getSideOutput(paySucTag);
        // 8.2 取消运单明细流
        DataStream<String> cancelDetailDS = orderDetailDS.getSideOutput(cancelDetailTag);
        // 8.3 揽收明细流
        DataStream<String> receiveDetailDS = orderDetailDS.getSideOutput(receiveDetailTag);
        // 8.4 发单明细流
        DataStream<String> dispatchDetailDS = orderDetailDS.getSideOutput(dispatchDetailTag);
        // 8.5 转运成功明细流
        DataStream<String> boundFinishDetailDS = orderDetailDS.getSideOutput(boundFinishDetailTag);
        // 8.6 派送成功明细流
        DataStream<String> deliverSucDetailDS = orderDetailDS.getSideOutput(deliverSucDetailTag);
        // 8.7 签收明细流
        DataStream<String> signDetailDS = orderDetailDS.getSideOutput(signDetailTag);

        //TODO 9.将不同流的数据写到kafka的不同主题中
        //9.1 定义主题名称
        // 9.1.1 交易域下单明细主题
        String detailTopic = "tms_dwd_trade_order_detail";
        // 9.1.2 交易域支付成功明细主题
        String paySucDetailTopic = "tms_dwd_trade_pay_suc_detail";
        // 9.1.3 交易域取消运单明细主题
        String cancelDetailTopic = "tms_dwd_trade_cancel_detail";
        // 9.1.4 物流域接单（揽收）明细主题
        String receiveDetailTopic = "tms_dwd_trans_receive_detail";
        // 9.1.5 物流域发单明细主题
        String dispatchDetailTopic = "tms_dwd_trans_dispatch_detail";
        // 9.1.6 物流域转运完成明细主题
        String boundFinishDetailTopic = "tms_dwd_trans_bound_finish_detail";
        // 9.1.7 物流域派送成功明细主题
        String deliverSucDetailTopic = "tms_dwd_trans_deliver_detail";
        // 9.1.8 物流域签收明细主题
        String signDetailTopic = "tms_dwd_trans_sign_detail";

        // 9.2 发送数据到 Kafka
        // 9.2.1 运单明细数据
        KafkaSink<String> kafkaProducer = KafkaUtil.getKafkaSink(detailTopic, args);
        orderDetailDS.print("~~");
        orderDetailDS
            .sinkTo(kafkaProducer)
            .uid("order_detail_sink");

        // 9.2.2 支付成功明细数据
        KafkaSink<String> paySucKafkaProducer = KafkaUtil.getKafkaSink(paySucDetailTopic, args);
        paySucDS.print("!!");
        paySucDS
            .sinkTo(paySucKafkaProducer)
            .uid("pay_suc_detail_sink");

        // 9.2.3 取消运单明细数据
        KafkaSink<String> cancelKafkaProducer = KafkaUtil.getKafkaSink(cancelDetailTopic, args);
        cancelDetailDS.print("@@");
        cancelDetailDS
            .sinkTo(cancelKafkaProducer)
            .uid("cancel_detail_sink");

        // 9.2.4 揽收明细数据
        KafkaSink<String> receiveKafkaProducer = KafkaUtil.getKafkaSink(receiveDetailTopic, args);
        receiveDetailDS.print("##");
        receiveDetailDS
            .sinkTo(receiveKafkaProducer)
            .uid("reveive_detail_sink");

        // 9.2.5 发单明细数据
        KafkaSink<String> dispatchKafkaProducer = KafkaUtil.getKafkaSink(dispatchDetailTopic, args);
        dispatchDetailDS.print("$$");
        dispatchDetailDS
            .sinkTo(dispatchKafkaProducer)
            .uid("dispatch_detail_sink");

        // 9.2.6 转运完成明细主题
        KafkaSink<String> boundFinishKafkaProducer = KafkaUtil.getKafkaSink(boundFinishDetailTopic, args);
        boundFinishDetailDS.print("%%");
        boundFinishDetailDS
            .sinkTo(boundFinishKafkaProducer)
            .uid("bound_finish_detail_sink");

        // 9.2.7 派送成功明细数据
        KafkaSink<String> deliverSucKafkaProducer = KafkaUtil.getKafkaSink(deliverSucDetailTopic, args);
        deliverSucDetailDS.print("^^");
        deliverSucDetailDS
            .sinkTo(deliverSucKafkaProducer)
            .uid("deliver_suc_detail_sink");

        // 9.2.8 签收明细数据
        KafkaSink<String> signKafkaProducer = KafkaUtil.getKafkaSink(signDetailTopic, args);
        signDetailDS.print("&&");
        signDetailDS
            .sinkTo(signKafkaProducer)
            .uid("sign_detail_sink");


        env.execute();
    }
}
