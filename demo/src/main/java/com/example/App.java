package com.example;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {
        System.out.println("Kafka Simple Consumer");

        Logger logger = LoggerFactory.getLogger(App.class.getName());

        String server = "172.25.145.150:9092";
        String topic = "first_topic";

        // create consumer config
        // https://kafka.apache.org/documentation/#consumerconfigs

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        // define which partition and offset to read from
        int partitionNumber = 2;
        int firstOffset = 0; 

        // assign
        TopicPartition partition = new TopicPartition(topic, partitionNumber);
        consumer.assign(Arrays.asList(partition));

        // seek
        consumer.seek(partition, firstOffset);

        // loop control
        int messagesToRead = 5;
        int messagesRead = 0;
        boolean done = false;

        // poll for data
        while (!done) {
            ConsumerRecords<String, String> records =
                consumer.poll(Duration.ofMillis(100));
            
            // process the dataset received
            for (ConsumerRecord<String, String> record: records) {
                messagesRead++;
                logger.info(
                    "Message with key: " + record.key() +
                    "\nValue: " + record.value() +
                    "\nPartition: " + record.partition() +
                    "\nOffset: " + record.offset()
                );
                if (messagesRead >= messagesToRead){
                    done = true;
                    break;
                };
            }
        }
    }
}
