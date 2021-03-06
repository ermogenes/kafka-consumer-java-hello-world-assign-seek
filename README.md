# kafka-consumer-java-hello-world-assign-seek
A simple Java Kafka consumer, with assign and seek strategy.

## Dependencies (`pom.xml`)

- [kafka-clients](https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients/2.8.0)
- [slf4j-simple](https://mvnrepository.com/artifact/org.slf4j/slf4j-simple/1.7.30)

```xml
        <!-- https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients -->
        <dependency>
            <groupId>org.apache.kafka</groupId>
            <artifactId>kafka-clients</artifactId>
            <version>2.8.0</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.30</version>
            <!-- <scope>test</scope> -->
        </dependency>
```

## Code

```java
Logger logger = LoggerFactory.getLogger(App.class.getName());

String server = "<your.server.ip.address>:9092";
String topic = "first_topic";

Properties properties = new Properties();
properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

int partitionNumber = 2;
int firstOffset = 0; 

TopicPartition partition = new TopicPartition(topic, partitionNumber);
consumer.assign(Arrays.asList(partition));
consumer.seek(partition, firstOffset);

int messagesToRead = 5;
int messagesRead = 0;
boolean done = false;

while (!done) {
    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
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
```
