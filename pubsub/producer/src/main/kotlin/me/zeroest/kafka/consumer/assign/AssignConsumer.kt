package me.zeroest.kafka.consumer.assign

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Collections
import java.util.Properties

/**
 * 직접 파티션을 컨슈머에 명시적으로 할당하여 운영
 */
class AssignConsumer

private val log = LoggerFactory.getLogger(AssignConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val PARTITION_NUMBER = 0
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"

fun main() {
    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    val consumer = KafkaConsumer<String, String>(configs)
    // 어떤 토픽, 파티션을 할당할지 명시적으로 선언
    // 직접 할당하기에 리밸런싱하는 과정이 없다
    consumer.assign(Collections.singleton(TopicPartition(TOPIC_NAME, PARTITION_NUMBER)))

    while (true) {
        val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(1))

        for (record in records) {
            log.info("record: {}", record)

            // Logic...

            consumer.commitSync()
        }
    }
}
