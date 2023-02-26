package me.zeroest.kafka.consumer.rebalance

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

/**
 * poll을 통해 반환받은 데이터를 모두 처리하기 전에 리밸런스가 발생하면 데이터를 중복처리할 수 있다. (데이터를 일부 처리했으나 커밋하지 않은 경우)
 * 리밸런스 발생 시 데이터를 중복 처리하지 않게 하기 위해서 리밸런스 발생 시 처리한 데이터를 기준으로 커밋을 시도해야 한다.
 * */
class RebalanceListenerConsumer

private val log = LoggerFactory.getLogger(RebalanceListenerConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"

private lateinit var consumer: KafkaConsumer<String, String>
val currentOffsets = mutableMapOf<TopicPartition, OffsetAndMetadata>()

fun main() {
    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    consumer = KafkaConsumer<String, String>(configs)
    consumer.subscribe(listOf(TOPIC_NAME), RebalanceListener())

    while (true) {
        val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(1))

        for (record in records) {
            log.info("record: {}", record)

            // Logic...

            currentOffsets[TopicPartition(record.topic(), record.partition())] =
                OffsetAndMetadata(record.offset() + 1, null)
            consumer.commitSync(currentOffsets)
        }
    }
}

class RebalanceListener : ConsumerRebalanceListener {
    // 리밸런스가 끝난 뒤에 파티션이 할당 완료되면 호출되는 메서드
    override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>?) {
        log.warn("Partitions are assigned")
    }

    // 리밸런스가 시작되기 직전에 호출되는 메서드
    override fun onPartitionsRevoked(partitions: MutableCollection<TopicPartition>?) {
        log.warn("Partitions are revoked")
        // 리밸런스가 발생하면 가장 마지막으로 처리 완료한 레코드를 기준으로 커밋을 실시한다.
        // 이를 통해 데이터 처리의 중복을 방지
        consumer.commitSync(currentOffsets)
    }
}
