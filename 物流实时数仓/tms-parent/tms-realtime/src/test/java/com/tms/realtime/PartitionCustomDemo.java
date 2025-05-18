package com.tms.realtime;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * TODO
 *
 * @author cjp
 * @version 1.0
 */
public class PartitionCustomDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(4);

        DataStreamSource<String> socketDS = env.socketTextStream("node1", 7777);

        socketDS
                .partitionCustom(
                        new Partitioner<String>() {
                            @Override
                            public int partition(String key, int numPartitions) {
                                return Integer.parseInt(key) % numPartitions;
                            }
                        },
                        new KeySelector<String, String>() {
                            @Override
                            public String getKey(String value) throws Exception {
                                return value;
                            }
                        }
                )
                .print(">>>");

        socketDS
                .keyBy(s -> s)
                .print("+++");


        env.execute();
    }
}
