package me.zeroest.reactor.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOffset;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Sample consumer application using Reactive API for Kafka.
 * To run sample consumer
 * <ol>
 *   <li> Start Zookeeper and Kafka server
 *   <li> Update {@link #BOOTSTRAP_SERVERS} and {@link #TOPIC} if required
 *   <li> Create Kafka topic {@link #TOPIC}
 *   <li> Send some messages to the topic, e.g. by running {@link SampleProducer}
 *   <li> Run {@link SampleConsumer} as Java application with all dependent jars in the CLASSPATH (eg. from IDE).
 *   <li> Shutdown Kafka server and Zookeeper when no longer required
 * </ol>
 */
public class SampleConsumer {

    private static final Logger log = LoggerFactory.getLogger(SampleConsumer.class.getName());

    private static final String BOOTSTRAP_SERVERS = "localhost:29092";
    private static final String TOPIC = "demo-topic";

    private final ReceiverOptions<Integer, String> receiverOptions;
    private final DateTimeFormatter dateFormat;

    public SampleConsumer(String bootstrapServers) {

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "sample-consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "sample-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        receiverOptions = ReceiverOptions.create(props);
        dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS")
                .withZone(ZoneId.systemDefault());
    }

    public Disposable consumeMessages(String topic, CountDownLatch latch) {

        ReceiverOptions<Integer, String> options = receiverOptions.subscription(Collections.singleton(topic))
                .addAssignListener(partitions -> log.debug("onPartitionsAssigned {}", partitions))
                .addRevokeListener(partitions -> log.debug("onPartitionsRevoked {}", partitions));
        Flux<ReceiverRecord<Integer, String>> kafkaFlux = KafkaReceiver.create(options).receive();
        return kafkaFlux.subscribe(receiverRecord -> {
            ReceiverOffset offset = receiverRecord.receiverOffset();
            Instant timestamp = Instant.ofEpochMilli(receiverRecord.timestamp());
            System.out.printf("Received message: topic-partition=%s offset=%d timestamp=%s key=%d value=%s\n",
                    offset.topicPartition(),
                    offset.offset(),
                    dateFormat.format(timestamp),
                    receiverRecord.key(),
                    receiverRecord.value());
            offset.acknowledge();
            latch.countDown();
        });
    }

    public static void main(String[] args) throws Exception {
        int count = 20;
        CountDownLatch latch = new CountDownLatch(count);
        SampleConsumer consumer = new SampleConsumer(BOOTSTRAP_SERVERS);
        Disposable disposable = consumer.consumeMessages(TOPIC, latch);
        latch.await();
//        boolean await = latch.await(10, TimeUnit.SECONDS);
        disposable.dispose();
    }
}