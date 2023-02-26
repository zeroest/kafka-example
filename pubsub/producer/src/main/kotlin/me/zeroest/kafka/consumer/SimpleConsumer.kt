package me.zeroest.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

/**
 * Auto commit simple consumer
 * */
class SimpleConsumer

private val log = LoggerFactory.getLogger(SimpleConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"

fun main() {
    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_DOC
     * earliest: automatically reset the offset to the earliest offset
     * latest: automatically reset the offset to the latest offset
     * none: throw exception to the consumer if no previous offset is found for the consumer's group
     * anything else: throw exception to the consumer.
     * */
    configs[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest" // default
    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true // default
    configs[ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG] = 5000 // default | AUTO_COMMIT: true 일 경우 오프셋 커밋 간격
    configs[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 500 // default | poll() 메서드를 통해 반환되는 레코드 개수
    /*
    * poll() 메서드를 호출하는 간격의 최대 시간
    * 메서드 호출 이후 해당 시간 이후 데이터 처리시간이 길어져 비정상으로 판단하고 리밸런싱 처리
    * */
    configs[ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG] = 300_000 // default 5min
    /*
    * 컨슈머와 브로커 연결이 끊기는 최대 시간
    * 이 시간 내에 heartbeat 전송하지 않을시 리밸런싱 처리
    * 보통 heartbeat 시간 간격의 3배로 설정
    * */
    configs[ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG] = 10000 // default
    configs[ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG] = 3000 // default
    /**
     * @see org.apache.kafka.clients.consumer.ConsumerConfig.ISOLATION_LEVEL_DOC
     * read_committed: 커밋이 완료된 레코드만 읽음
     * read_uncommitted: 커밋 여부와 관계없이 파티션에 있는 모든 레코드를 읽음
     * */
    configs[ConsumerConfig.ISOLATION_LEVEL_CONFIG] = "read_uncommitted" // default

    val consumer = KafkaConsumer<String, String>(configs)
    consumer.subscribe(listOf(TOPIC_NAME))

    while (true) {
        // Duration - 브로커로부터 데이터를 가져올 때 컨슈 버퍼에 데이터를 기다리기 위한 타임아웃 간격
        val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(1))

        for (record in records) {
            log.info("record: {}", record)
        }
    }
}
