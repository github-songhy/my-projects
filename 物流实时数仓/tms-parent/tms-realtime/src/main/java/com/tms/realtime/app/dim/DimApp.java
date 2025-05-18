package com.tms.realtime.app.dim;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tms.realtime.app.func.DimSinkFunction;
import com.tms.realtime.app.func.MyBroadcastProcessFunction;
import com.tms.realtime.beans.TmsConfigDimBean;
import com.tms.realtime.common.TmsConfig;
import com.tms.realtime.utils.CreateEnvUtil;
import com.tms.realtime.utils.HbaseUtil;
import com.tms.realtime.utils.KafkaUtil;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Felix
 * @date 2025/3/24
 * DIM维度层处理
 * 需要启动的的进程
 *      mysql、zk、kafka、hdfs、hbase、OdsApp、DimApp
 * 开发流程
 *      基本环境准备
 *      从kafka的tms_ods主题中读取主流业务数据
 *      对读取的数据进行类型转换    jsonStr->jsonObj
 *      使用FlinkCDC从配置表中读取配置信息---配置流
 *      提前将配置表创建出来
 *      将配置流进行广播---广播流
 *      将主流和广播流进行关联---connect
 *      对关联之后的数据进行处理---process
 *      class MyBroadcastProcessFunction extends BroadcastProcessFunction{
 *          open：将配置信息预加载到configMap集合中
 *          processElement:处理主流数据
 *              获取处理的维度表的表名
 *              根据表名到广播状态以及对应的configMap中获取对应的配置信息
 *              如果是维度，传递到下游
 *                  过滤掉不需要传递的属性
 *                  补充sink_table
 *                  补充sink_pk
 *          processBroadcastElement:处理广播流数据
 *              op="d"
 *                  从广播状态以及configMap中删除配置信息
 *              op!="d"
 *                  将对应的配置信息放到广播状态以及configMap中
 *      }
 *      将维度数据写到hbase
 *          HbaseUtil工具类中提供了一个批量、异步的写入一行数据的方法
 *          class DimSinkFunction implements SinkFunction{
 *              invoke:调用HbaseUtil的写入方法完成写入操作
 *          }
 *执行流程
 *      当启动DimApp应用的时候，提前创建维度表
 *      从配置表中加载配置信息到广播状态以及configMap中
 *      当启动OdsApp应用的时候，会从业务数据库中，对所有监控的维度表进行全表扫描，获取维度的历史数据
 *      将历史数据封装为json格式字符串传递到kafka的tms_ods主题中
 *      DimApp会从tms_ods主题中读取业务数据
 *      对读取的数据进行判断，根据表名到广播状态以及configMap中查找对应的配置信息
 *      如果找到对应的配置信息了，说明是维度，将维度数据传递到下游
 *      将维度数据写到hbase表中
 */
public class DimApp {
    public static void main(String[] args) throws Exception {
        //TODO 1.基本环境准备
        //1.1 指定流处理环境以及检查点相关的设置
        StreamExecutionEnvironment env = CreateEnvUtil.getStreamEnv(args);
        //1.2 设置并行度
        env.setParallelism(4);

        //TODO 2.从kafka的tms_ods主题中读取业务数据
        //2.1 声明消费的主题以及消费者组
        String topic = "tms_ods";
        String groupId = "dim_app_group";
        //2.2 创建消费者对象
        KafkaSource<String> kafkaSource = KafkaUtil.getKafkaSource(topic, groupId, args);
        //2.3 消费数据 封装为流
        SingleOutputStreamOperator<String> kafkaStrDS = env
            .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "kafka_source")
            .uid("kafka_source");

//         kafkaStrDS.print(">>>");

        //TODO 3.对读取的数据进行类型转换并过滤掉不需要传递json属性
        SingleOutputStreamOperator<JSONObject> jsonObjDS = kafkaStrDS.map(new MapFunction<String, JSONObject>() {
            @Override
            public JSONObject map(String jsonStr) throws Exception {
                //为了操作方便，将json字符串转换为json对象
                JSONObject jsonObj = JSON.parseObject(jsonStr);
                String table = jsonObj.getJSONObject("source").getString("table");
                jsonObj.put("table",table);
                // jsonObj.remove("before");
                jsonObj.remove("source");
                jsonObj.remove("transaction");
                return jsonObj;
            }
        });
        // jsonObjDS.print(">>>");
        //TODO 4.使用FlinkCDC读取配置表数据
        //4.1 获取MySqlSource
        MySqlSource<String> mySqlSource = CreateEnvUtil.getMySqlSource("config_dim", "6000", args);
        //4.2 读取数据 封装为流
        SingleOutputStreamOperator<String> mysqlDS = env
            .fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), "mysql_source")
            .setParallelism(1)
            .uid("mysql_source");
        //{"before":null,"after":{"source_table":"user_info","sink_table":"dim_user_info","sink_family":null,"sink_columns":"id,login_name,nick_name,passwd,real_name,phone_num,email,user_level,birthday,gender,create_time,update_time,is_deleted","sink_pk":"id"},"source":{"version":"1.6.4.Final","connector":"mysql","name":"mysql_binlog_source","ts_ms":0,"snapshot":"false","db":"tms_config","sequence":null,"table":"tms_config_dim","server_id":0,"gtid":null,"file":"","pos":0,"row":0,"thread":null,"query":null},"op":"r","ts_ms":1684926440339,"transaction":null}

        //TODO 5.提前将hbase中的维度表创建出来
        SingleOutputStreamOperator<String> createTableDs = mysqlDS.map(
            new MapFunction<String, String>() {

                @Override
                public String map(String jsonStr) throws Exception {
                    //将jsonStr转换为jsonObj
                    JSONObject jsonObj = JSON.parseObject(jsonStr);
                    //获取对配置表的操作
                    String op = jsonObj.getString("op");
                    if ("r".equals(op) || "c".equals(op)) {
                        //获取after属性的值
                        JSONObject afterJsonObj = jsonObj.getJSONObject("after");
                        String sinkTable = afterJsonObj.getString("sink_table");
                        String sinkFamily = afterJsonObj.getString("sink_family");
                        if (StringUtils.isEmpty(sinkFamily)) {
                            sinkFamily = "info";
                        }
                        System.out.println("在Hbase中创建表：" + sinkTable);
                        HbaseUtil.createTable(TmsConfig.HBASE_NAMESPACE, sinkTable, sinkFamily.split(","));
                    }

                    return jsonStr;
                }
            }
        );
        // createTableDs.print(">>>>");

        //TODO 6.对配置数据进行广播
        MapStateDescriptor<String, TmsConfigDimBean> mapStateDescriptor
            = new MapStateDescriptor<String, TmsConfigDimBean>("mapStateDescriptor",String.class,TmsConfigDimBean.class);
        BroadcastStream<String> broadcastDS = createTableDs.broadcast(mapStateDescriptor);

        //TODO 7.将主流和广播流进行关联---connect
        BroadcastConnectedStream<JSONObject, String> connectDS = jsonObjDS.connect(broadcastDS);

        //TODO 8.对关联之后的数据进行处理
        SingleOutputStreamOperator<JSONObject> dimDS = connectDS.process(
            new MyBroadcastProcessFunction(mapStateDescriptor,args)
        );
        //TODO 9.将维度数据保存到Hbase表中
        dimDS.print(">>>");
        dimDS.addSink(new DimSinkFunction());

        env.execute();
    }
}
