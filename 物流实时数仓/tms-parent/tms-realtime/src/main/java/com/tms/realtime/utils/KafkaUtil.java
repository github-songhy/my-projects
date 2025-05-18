package com.tms.realtime.utils;

import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;

import java.io.IOException;

/**
 * @author Felix
 * @date 2025/3/16
 * 操作Kafka的工具类
 */
public class KafkaUtil {
    private static final String KAFKA_SERVER = "node1:9092,node2:9092,node3:9092";
    //获取KafkaSink的方法
    public static KafkaSink<String> getKafkaSink(String topic,String transIdPrefix,String[] args){
        // 将命令行参数对象封装为 ParameterTool 类对象
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // 提取命令行传入的 key 为 topic 的配置信息，并将默认值指定为方法参数 topic
        // 当命令行没有指定 topic 时，会采用默认值
        topic = parameterTool.get("topic", topic);
        // 如果命令行没有指定主题名称且默认值为 null 则抛出异常
        if (topic == null) {
            throw new IllegalArgumentException("主题名不可为空：命令行传参为空且没有默认值!");
        }

        // 获取命令行传入的 key 为 bootstrap-servers 的配置信息，并指定默认值
        String bootstrapServers = parameterTool.get("bootstrap-severs", KAFKA_SERVER);
        // 获取命令行传入的 key 为 transaction-timeout 的配置信息，并指定默认值
        String transactionTimeout = parameterTool.get("transaction-timeout", 15 * 60 * 1000 + "");


        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
            .setBootstrapServers(bootstrapServers)
            .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                .setTopic(topic)
                .setValueSerializationSchema(new SimpleStringSchema())
                .build()
            )
             .setTransactionalIdPrefix(transIdPrefix)
            // .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
            // .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG,transactionTimeout)
            .build();
        return kafkaSink;
    }


    public static KafkaSink<String> getKafkaSink(String topic,String[] args){
        return getKafkaSink(topic,topic + "_trans",args);
    }

    //获取KafkaSource
    public static KafkaSource<String> getKafkaSource(String topic,String groupId,String [] args){
        // 将命令行参数对象封装为 ParameterTool 类对象
        ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // 提取命令行传入的 key 为 topic 的配置信息，并将默认值指定为方法参数 topic
        // 当命令行没有指定 topic 时，会采用默认值
        topic = parameterTool.get("topic", topic);
        // 如果命令行没有指定主题名称且默认值为 null 则抛出异常
        if (topic == null) {
            throw new IllegalArgumentException("主题名不可为空：命令行传参为空且没有默认值!");
        }

        // 获取命令行传入的 key 为 bootstrap-servers 的配置信息，并指定默认值
        String bootstrapServers = parameterTool.get("bootstrap-severs", KAFKA_SERVER);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
            .setBootstrapServers(bootstrapServers)
            .setTopics(topic)
            .setGroupId(groupId)
            //在flink程序中维护偏移量（而不是使用kafka自己维护的偏移量），当数据已经读取但此时程序出现异常时，再次恢复程序不会因为kafka已经更新偏移量而丢失数据
            //使用flink中的 kafkaSource 自动维护的偏移量，如果是第一次消费没有偏移量默认从最新位置
            .setStartingOffsets(OffsetsInitializer.committedOffsets(OffsetResetStrategy.LATEST))
            //注意：使用SimpleStringSchema进行反序列化，如果读到的消息为空，处理不了，需要自定义返序列化类
            // .setValueOnlyDeserializer(new SimpleStringSchema())
            .setValueOnlyDeserializer(new DeserializationSchema<String>() {
                @Override
                public String deserialize(byte[] message) throws IOException {
                    if(message != null){
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
        return kafkaSource;
    }
}
