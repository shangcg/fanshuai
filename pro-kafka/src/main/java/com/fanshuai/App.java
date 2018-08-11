package com.fanshuai;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args )
    {
        App app = new App();
        app.init();
        app.producerThread.run();
        app.consunerThread.run();

    }

    public Properties initProducer() {
        String ips = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", ips);
        properties.setProperty("acks", "all");
        properties.setProperty("retries", "0");
        properties.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return properties;
    }

    public Properties initConsumer() {
        String ips = "127.0.0.1:9092";
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", ips);
        properties.setProperty("group.id", "test");
        properties.setProperty("enable.auto.commit", "true");
        properties.setProperty("auto.commit.interval.ms", "1000");
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        return properties;
    }

    private Runnable producerThread = null;
    private Runnable consunerThread = null;

    public void init() {
        producerThread = new Runnable() {
            @Override
            public void run() {
                Properties prop = initProducer();
                //producer初始化
                Producer<String, String> producer = new KafkaProducer<String, String>(prop);
                for (int i=0;i< 100;i++) {

                    //发送消息
                    String key = "key" + i;
                    String value = "value "+ i;
                    String topic = "test1";
                    producer.send(new ProducerRecord<String, String>(topic, key, value));
                    logger.info(String.format("produce message, topic=%s, key=%s, value=%s",topic, key, value));
                }
            }
        };

        consunerThread = new Runnable() {
            @Override
            public void run() {

                Properties prop = initConsumer();
                KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(prop);
                //订阅消息
                String topic = "test1";
                consumer.subscribe(Arrays.asList(topic), new ConsumerRebalanceListener() {
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> collection) {

                    }
                });

                ConsumerRecords<String, String> records = consumer.poll(10000);
                for (ConsumerRecord<String, String> record : records) {
                    logger.info(String.format("consume message, key=%s, value=%s, offset=%s", record.key(), record.value(), record.offset()));
                }
            }
        };
    }
}
