package me.zeroest.kafka.consumer.gracefulstop

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

/**
 * 정상 종료되지 않은 컨슈머는 세션 타임아웃이 발생할때까지 컨슈머 그룹에 남게 된다
 * 이로 인해 실제로 종료되었지만 더는 동작 하지 않는 컨슈머가 존재해
 * 파티션의 데이터는 소모되지 못하고 컨슈머 랙이 늘어나게 된다
 * */
class GracefulStopConsumer

private val log = LoggerFactory.getLogger(GracefulStopConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"

private lateinit var consumer: KafkaConsumer<String, String>

fun main() {
    // 셧다운 훅: 사용자 또는 운영체제로부터 종료 요청을 바ㄷ으면 실행하는 스레드
    // kill {-term} {pid}
    // 위와 같이 셧다운 훅이 발생하면 ShutdownThread에 wakeup() 메서드가 호출되어 컨슈머를 안전하게 종료
    Runtime.getRuntime().addShutdownHook(ShutdownTread())

    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    consumer = KafkaConsumer<String, String>(configs)
    consumer.subscribe(listOf(TOPIC_NAME))

    try {
        while (true) {
            val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(1))

            for (record in records) {
                log.info("record: {}", record)
            }

            consumer.commitSync()
        }
    } catch (wakeup: WakeupException) {
        log.warn("Wakeup consumer")
        // wakeup() 메서드가 실행된 이후 poll() 메서드가 호출되면 WakeupException 예외가 발생

        // 리소스 종료 처리...

    } finally {
        // close() 메서드를 호출하여 카프카 클러스터에 컨슈머가 안전하게 종료되었음을 명시적으로 알려주면 종료가 완료
        consumer.close()
    }
}

class ShutdownTread : Thread() {
    override fun run() {
        log.info("Shutdown hook")
        consumer.wakeup()
    }
}
