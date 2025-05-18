package com.tms.realtime;

import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.api.common.functions.RichAggregateFunction;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.io.IOException;

public class TestParallelism {
    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        env.setParallelism(2);
        KafkaSource<String> kafkaSource = KafkaSource
                .<String>builder()
                .setBootstrapServers("node1:9092,node2:9092,node3:9092")
                .setTopics("test")
                .setGroupId("test_groups223")
                .setStartingOffsets(OffsetsInitializer.latest())
                .setValueOnlyDeserializer(new DeserializationSchema<String>() {
                    @Override
                    public String deserialize(byte[] message) throws IOException {
                        if (message != null) {
                            return new String(message);
                        }
                        return null;
                    }

                    @Override
                    public boolean isEndOfStream(String nextElement) {
                        return false;
                    }

                    @Override
                    public TypeInformation<String> getProducedType() {
                        return TypeInformation.of(String.class);
                    }
                })
                .build();
        DataStreamSource<String> kfkDS = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), "test123458");

        DataStream<String> WRDS = kfkDS
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
                                return value.split(",")[1];
                            }
                        }
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy
                                .<String>forMonotonousTimestamps()
                                .withTimestampAssigner(
                                        new SerializableTimestampAssigner<String>() {
                                            @Override
                                            public long extractTimestamp(String element, long recordTimestamp) {
//                                        return Long.parseLong(element) * 1000L;
                                                return Long.parseLong(element.split(",")[1]) * 1000L;
                                            }
                                        }
                                )
                )
                ;

        SingleOutputStreamOperator<String> map = WRDS.map(r -> r);


        KeyedStream<String, String> key = map.keyBy(r -> r.split(",")[0]);


        WindowedStream<String, String, TimeWindow> window = key.window(TumblingEventTimeWindows.of(Time.seconds(10)));

        window.aggregate(
                        new AggregateFunction<String, String, String>() {
                            @Override
                            public String createAccumulator() {
                                return "beg--";
                            }

                            @Override
                            public String add(String value, String accumulator) {
                                return accumulator + value + "--";
                            }

                            @Override
                            public String getResult(String accumulator) {
                                return accumulator + "end";
                            }

                            @Override
                            public String merge(String a, String b) {
                                return null;
                            }
                        }
                )
                .print("ggg");

        env.execute();
    }
}
