package com.tms.realtime.utils;

import com.esotericsoftware.minlog.Log;
import com.ververica.cdc.connectors.mysql.source.MySqlSource;
import com.ververica.cdc.connectors.mysql.source.MySqlSourceBuilder;
import com.ververica.cdc.connectors.mysql.table.StartupOptions;
import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.connect.json.DecimalFormat;
import org.apache.kafka.connect.json.JsonConverterConfig;

import java.util.HashMap;

/**
 * @author Felix
 * @date 2025/3/16
 * 获取执行环境
 */
public class CreateEnvUtil {
    //获取流处理环境
    public static StreamExecutionEnvironment getStreamEnv(String[] args){
        //TODO 1.基本环境准备
        //1.1 指定流处理环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //TODO 2.检查点相关的设置
        //2.1 开启检查点
        env.enableCheckpointing(60000L, CheckpointingMode.EXACTLY_ONCE);
        //2.2 设置检查点的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(120000L);
        //2.3 设置job取消之后 检查点是否保留
        env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //2.4 设置两个检查点之间的最小时间间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(30000L);
        //2.5 设置重启策略
        env.setRestartStrategy(RestartStrategies.failureRateRestart(3, Time.days(1),Time.seconds(3)));
        //2.6 设置状态后端
        env.setStateBackend(new HashMapStateBackend());
        env.getCheckpointConfig().setCheckpointStorage("hdfs://mycluster/tms_realtime/ck");
        //2.7 设置操作hdfs的用户
        //获取命令行参数
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String hdfsUserName = parameterTool.get("hadoop-user-name", "root");
        System.setProperty("HADOOP_USER_NAME",hdfsUserName);
        return env;
    }
    //获取MySqlSource
    public static MySqlSource<String> getMySqlSource(String option,String serverId,String[] args){
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String mysqlHostname = parameterTool.get("mysql-hostname", "node1");
        int mysqlPort = Integer.parseInt(parameterTool.get("mysql-port", "3306"));
        String mysqlUsername = parameterTool.get("mysql-username", "root");
        String mysqlPasswd = parameterTool.get("mysql-passwd", "123456");
        option = parameterTool.get("start-up-options", option);
        serverId = parameterTool.get("server-id",serverId);

        // 创建配置信息 Map 集合，将 Decimal 数据类型的解析格式配置 k-v 置于其中
        HashMap config = new HashMap<>();
        config.put(JsonConverterConfig.DECIMAL_FORMAT_CONFIG, DecimalFormat.NUMERIC.name());
        // 将前述 Map 集合中的配置信息传递给 JSON 解析 Schema，该 Schema 将用于 MysqlSource 的初始化
        JsonDebeziumDeserializationSchema jsonDebeziumDeserializationSchema =
            new JsonDebeziumDeserializationSchema(false, config);


        MySqlSourceBuilder<String> builder = MySqlSource.<String>builder()
            .hostname(mysqlHostname)
            .port(mysqlPort)
            .username(mysqlUsername)
            .password(mysqlPasswd)
            .deserializer(jsonDebeziumDeserializationSchema);


        switch (option){
            //读取事实数据
            case "dwd":
                String[] dwdTables = new String[]{"tms_realtime.order_info",
                    "tms_realtime.order_cargo",
                    "tms_realtime.transport_task",
                    "tms_realtime.order_org_bound"};
                return builder
                    .databaseList("tms_realtime")
                    .tableList(dwdTables)
                    .startupOptions(StartupOptions.latest())
                    .serverId(serverId)
                    .build();
            //读取维度数据
            case "realtime_dim":
                String[] realtimeDimTables = new String[]{"tms_realtime.user_info",
                    "tms_realtime.user_address",
                    "tms_realtime.base_complex",
                    "tms_realtime.base_dic",
                    "tms_realtime.base_region_info",
                    "tms_realtime.base_organ",
                    "tms_realtime.express_courier",
                    "tms_realtime.express_courier_complex",
                    "tms_realtime.employee_info",
                    "tms_realtime.line_base_shift",
                    "tms_realtime.line_base_info",
                    "tms_realtime.truck_driver",
                    "tms_realtime.truck_info",
                    "tms_realtime.truck_model",
                    "tms_realtime.truck_team"};
                return builder
                    .databaseList("tms_realtime")
                    .tableList(realtimeDimTables)
                    .startupOptions(StartupOptions.initial())
                    .serverId(serverId)
                    .build();
            case "config_dim":
                return builder
                    .databaseList("tms_config")
                    .tableList("tms_config.tms_config_dim")
                    .startupOptions(StartupOptions.initial())
                    .serverId(serverId)
                    .build();
        }

        Log.error("不支持的操作类型！");
        return null;
    }
}
