package me.zeroest.kafka.consumer.multithread

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties
import java.util.concurrent.Executors

class MultiWorkerThreadConsumer

private val log = LoggerFactory.getLogger(MultiWorkerThreadConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"

class ConsumerWorker(
    val recordValue: String
): Runnable {
    private val log = LoggerFactory.getLogger(ConsumerWorker::class.java)

    override fun run() {
        log.info("thread: ${Thread.currentThread().name}\trecord: $recordValue")
    }
}

/**
 * 스레드를 사용함으로써 데이터 처리가 끝나지 않았음에도 불구하고 커밋을 하기 때문에 리벨런싱, 컨슈머 장애시에 데이터 유실이 발생할 수 있다
 * - 각 레코드의 데이터 처리가 끝났음을 스레드로부터 리턴 받지 않고 바로 그 다음 poll() 메서드를 호출한다
 * 레코드 처리의 역전현상 발생
 * - 스레드 생성은 레코드별 순서대로 진행되지만 스레드 처리시간이 다름
 * - 이전 레코드가 다음 레코드보다 나중에 처리될 수 있다.
 */
fun main() {
    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    val consumer = KafkaConsumer<String, String>(configs)
    consumer.subscribe(listOf(TOPIC_NAME))

    val executorService = Executors.newCachedThreadPool()
    while (true) {
        val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(10))

        // poll()을 통해 받은 데이터를 병렬 처리함으로써 속도의 이점을 얻을 수 있다
        for (record in records) {
            val worker = ConsumerWorker(record.value())
            executorService.execute(worker)
        }

        consumer.commitSync()
    }
}

/*
[pool-1-thread-4] INFO me.zeroest.kafka.consumer.multithread.ConsumerWorker - thread: pool-1-thread-4	record: fas
[pool-1-thread-2] INFO me.zeroest.kafka.consumer.multithread.ConsumerWorker - thread: pool-1-thread-2	record: ef
[pool-1-thread-3] INFO me.zeroest.kafka.consumer.multithread.ConsumerWorker - thread: pool-1-thread-3	record: asef
[pool-1-thread-1] INFO me.zeroest.kafka.consumer.multithread.ConsumerWorker - thread: pool-1-thread-1	record: ase
[pool-1-thread-5] INFO me.zeroest.kafka.consumer.multithread.ConsumerWorker - thread: pool-1-thread-5	record: fa
*/
