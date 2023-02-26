package me.zeroest.kafka.consumer.commit

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class CommitSyncSeparateConsumer

private val log = LoggerFactory.getLogger(CommitSyncSeparateConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
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
    consumer.subscribe(listOf(TOPIC_NAME))

    while (true) {
        val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(1))
        // 토픽의 파티션에 오프셋 정보를 기준으로 커밋
        val currentOffset = mutableMapOf<TopicPartition, OffsetAndMetadata>()

        for (record in records) {
            log.info("record: {}", record)
            currentOffset[TopicPartition(record.topic(), record.partition())] =
                OffsetAndMetadata(record.offset() + 1, null)
            // 특정 토픽 파티션, 오프셋을 파라미터로 넣어 커밋
            consumer.commitSync(currentOffset)
        }
    }
}
