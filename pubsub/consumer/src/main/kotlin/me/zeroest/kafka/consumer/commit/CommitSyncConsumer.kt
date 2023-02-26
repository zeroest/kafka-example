package me.zeroest.kafka.consumer.commit

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class CommitSyncConsumer

private val log = LoggerFactory.getLogger(CommitSyncConsumer::class.java)
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

        for (record in records) {
            log.info("record: {}", record)
        }

        // poll() 메서드로 받은 가장 마지막 레코드의 오프셋을 기준으로 커밋한다
        consumer.commitSync()
    }
}
