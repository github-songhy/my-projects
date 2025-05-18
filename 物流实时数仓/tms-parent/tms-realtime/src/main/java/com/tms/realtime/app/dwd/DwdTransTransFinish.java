package com.tms.realtime.app.dwd;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.beans.DwdTransTransFinishBean;
import com.tms.realtime.utils.CreateEnvUtil;
import com.tms.realtime.utils.DateFormatUtil;
import com.tms.realtime.utils.KafkaUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author Felix
 * @date 2025/3/28
 * 物流域：运输完成事实表
 * 需要启动的进程
 *      zk、kafka、hdfs、OdsApp、DwdTransTransFinish
 */
public class DwdTransTransFinish {
    public static void main(String[] args) throws Exception {
        //TODO 1.准备环境
        //1.1 指定流处理环境以及检查点相关的设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 设置并行度
        env.setParallelism(4);

        //TODO 2.从kafka的tms_ods主题中读取数据
        //2.1 声明消费的主题以及消费者组
        String topic = "tms_ods";
        String groupId = "dwd_trans_trans_finish_group";
        //2.2 获取消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据 封装为流
        SingleOutputStreamOperator<String> kafkaStrDS = env
            .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source")
            .uid("kafka_source");
        // kafkaStrDS.print(">>>>>");

        //TODO 3.筛选出运输完成的数据
        SingleOutputStreamOperator<String> filterDS = kafkaStrDS.filter(
            new FilterFunction<String>() {
                @Override
                public boolean filter(String jsonStr) throws Exception {
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    //将transport_task表的操作数据过滤出来
                    String table = jsonObj.getJSONObject("source").getString("table");
                    if (!"transport_task".equals(table)) {
                        return false;
                    }

                    String op = jsonObj.getString("op");
                    JSONObject beforeJsonObj = jsonObj.getJSONObject("before");
                    if (beforeJsonObj == null) {
                        return false;
                    }
                    JSONObject afterJsonObj = jsonObj.getJSONObject("after");
                    String oldActualEndTime = beforeJsonObj.getString("actual_end_time");
                    String newActualEndTime = afterJsonObj.getString("actual_end_time");

                    return "u".equals(op) && oldActualEndTime == null && newActualEndTime != null;
                }
            }
        );

        // filterDS.print(">>>");

        //TODO 4.对筛选出的数据进行处理（时间问题修复、脱敏、ts指定）
        SingleOutputStreamOperator<String> processDS = filterDS.process(
            new ProcessFunction<String, String>() {
                @Override
                public void processElement(String jsonStr, Context ctx, Collector<String> out) throws Exception {
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    DwdTransTransFinishBean finishBean = jsonObj.getObject("after", DwdTransTransFinishBean.class);
                    //补充运输时长字段
                    finishBean.setTransportTime(Long.parseLong(finishBean.getActualEndTime()) - Long.parseLong(finishBean.getActualStartTime()));

                    //将运输结束时间转换为毫秒并-8小时 赋值给事件时间字段ts
                    finishBean.setTs(Long.parseLong(finishBean.getActualEndTime()) - 8 * 60 * 60 * 1000L);

                    //处理时间问题  说明：使用FlinkCDC读取MySQL表中DateTime类型的字段，会将读取的内容转换为时间毫秒数，并比实际时间大8小时
                    finishBean.setActualStartTime(DateFormatUtil.toYmdHms(Long.parseLong(finishBean.getActualStartTime()) - 8 * 60 * 60 * 1000L));
                    finishBean.setActualEndTime(DateFormatUtil.toYmdHms(Long.parseLong(finishBean.getActualEndTime()) - 8 * 60 * 60 * 1000L));

                    //脱敏
                    String driver1Name = finishBean.getDriver1Name();
                    String driver2Name = finishBean.getDriver2Name();
                    String truckNo = finishBean.getTruckNo();

                    driver1Name = driver1Name.charAt(0) +
                        driver1Name.substring(1).replaceAll(".", "\\*");
                    driver2Name = driver2Name == null ? driver2Name : driver2Name.charAt(0) +
                        driver2Name.substring(1).replaceAll(".", "\\*");
                    truckNo = DigestUtils.md5Hex(truckNo);

                    finishBean.setDriver1Name(driver1Name);
                    finishBean.setDriver2Name(driver2Name);
                    finishBean.setTruckNo(truckNo);

                    out.collect(JSON.toJSONString(finishBean));
                }
            }
        );
        processDS.print(">>>");
        //TODO 5.将处理后的数据写到kafka主题中
        String sinkTopic = "tms_dwd_trans_trans_finish";
        processDS.sinkTo(KafkaUtil.getKafkaSink(sinkTopic,args)).uid("kafka_sink");
        env.execute();
    }
}
