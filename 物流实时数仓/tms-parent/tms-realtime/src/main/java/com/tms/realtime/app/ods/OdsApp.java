package com.tms.realtime.app.ods;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.utils.CreateEnvUtil;
import com.tms.realtime.utils.KafkaUtil;
import com.esotericsoftware.minlog.Log;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author Felix
 * @date 2025/3/15
 * ODS数据的采集
 */
public class OdsApp {
    public static void main(String[] args) throws Exception {
        //TODO 1.获取流处理环境并指定检查点
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        env.setParallelism(4);

        //TODO 2.使用FlinkCDC从MySQL中读取数据-事实数据
        String dwdOption = "dwd";
        String dwdServerId = "6030";
        String dwdSourceName = "ods_app_dwd_source";
        mysqlToKafka(dwdOption,dwdServerId,dwdSourceName,env,args);

        //TODO 3.使用FlinkCDC从MySQL中读取数据-维度数据
        String realtimeDimOption = "realtime_dim";
        String realtimeDimServerId = "6040";
        String realtimeDimSourceName = "ods_app_realtimeDim_source";
        mysqlToKafka(realtimeDimOption,realtimeDimServerId,realtimeDimSourceName,env,args);

        env.execute();
    }

    public static void mysqlToKafka(String option,String serverId,String sourceName,StreamExecutionEnvironment env,String[] args){
        //1.使用FlinkCDC从MySQL中读取数据
        MySqlSource<String> mySqlSource = CreateEnvUtil.getMySqlSource(option,serverId,args);

        SingleOutputStreamOperator<String> strDS = env
            .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), sourceName)
            .setParallelism(1)
            .uid(option + sourceName);

        //2.简单的ETL
        SingleOutputStreamOperator<String> processDS = strDS.process(
            new ProcessFunction<String, String>() {
                @Override
                public void processElement(String jsonStr, Context ctx, Collector<String> out) throws Exception {
                    try {
                        JSONObject jsonObj = JSON.parseObject(jsonStr);

                        if (jsonObj.getJSONObject("after") != null && !"d".equals(jsonObj.getString("op"))) {
                            Long tsMs = jsonObj.getLong("ts_ms");
                            jsonObj.put("ts", tsMs);
                            jsonObj.remove("ts_ms");
                            out.collect(jsonObj.toJSONString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.error("从Flink-CDC得到的数据不是一个标准的json格式");
                    }
                }
            }
        ).setParallelism(1);
        //3.按照主键进行分组，避免出现乱序
        KeyedStream<String, String> keyedDS = processDS.keyBy(
            new KeySelector<String, String>() {
                @Override
                public String getKey(String jsonStr) throws Exception {
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    return jsonObj.getJSONObject("after").getString("id");
                }
            }
        );

        //4.将数据写到kafka主题中
        keyedDS
            .sinkTo(KafkaUtil.getKafkaSink("tms_ods",sourceName + "_transPre",args))
            .uid(option + "_ods_app_sink");
    }
}
