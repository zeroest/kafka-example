package me.zeroest.kafka.consumer.multithread

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties
import java.util.concurrent.Executors

class MultiConsumerThread

private val log = LoggerFactory.getLogger(MultiConsumerThread::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"
private const val CONSUMER_COUNT = 4

class MultiConsumer(
    val idx: Int,
    val configs: Properties,
): Runnable {
    private val log = LoggerFactory.getLogger(MultiConsumer::class.java)

    override fun run() {
        log.error("$idx")
        val consumer = KafkaConsumer<String, String>(configs)
        consumer.subscribe(listOf(TOPIC_NAME))

        while (true) {
            val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(10))

            // poll()을 통해 받은 데이터를 병렬 처리함으로써 속도의 이점을 얻을 수 있다
            for (record in records) {
                log.info("[${Thread.currentThread().name} - $idx] record: ${record.value()}")
            }

            consumer.commitSync()
        }
    }
}

/**
 * 스레드 세이프 로직, 변수 고려 필요
 * 프로세스 내부에 스레드가 여러개 생성되어 실행되기 때문에 하나의 스레드에서 OOM 과 같은 예외 발생시 다른 컨슈머 스레드에까지 영향 발생
 */
fun main() {
    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    val executorService = Executors.newCachedThreadPool()
    for (i in 1..CONSUMER_COUNT) {
        log.info("$i")
        val consumer = MultiConsumer(i, configs)
        executorService.execute(consumer)
    }
}

/*
[pool-1-thread-4] INFO me.zeroest.kafka.consumer.multithread.MultiConsumer - [pool-1-thread-4 - 4] record: ef
[pool-1-thread-4] INFO me.zeroest.kafka.consumer.multithread.MultiConsumer - [pool-1-thread-4 - 4] record: asef
[pool-1-thread-2] INFO me.zeroest.kafka.consumer.multithread.MultiConsumer - [pool-1-thread-2 - 2] record: ab
[pool-1-thread-2] INFO me.zeroest.kafka.consumer.multithread.MultiConsumer - [pool-1-thread-2 - 2] record: bc
[pool-1-thread-1] INFO me.zeroest.kafka.consumer.multithread.MultiConsumer - [pool-1-thread-1 - 1] record: 123
[pool-1-thread-1] INFO me.zeroest.kafka.consumer.multithread.MultiConsumer - [pool-1-thread-1 - 1] record: value
*/
