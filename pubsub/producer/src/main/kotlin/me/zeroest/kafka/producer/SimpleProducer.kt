package me.zeroest.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

class SimpleProducer

private val log = LoggerFactory.getLogger(SimpleProducer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"

fun main() {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

    /**
     * @see org.apache.kafka.clients.producer.ProducerConfig.ACKS_DOC
     * 0: 프로듀서가 전송한 즉시 브로커에 데이터 저장 여부와 상관 없이 성공으로 판단
     * 1: 리더 파티션에 데이터가 저장되면 전송 성공으로 판단
     * -1, all: min.insync.replicas 개수에 해당하는 리더 파티션과 팔로워 파티션에 데이터가 저장되면 성공으로 판단
     * */
    configs[ProducerConfig.ACKS_CONFIG] = "1" // default
    /**
     * @see org.apache.kafka.clients.producer.ProducerConfig.BUFFER_MEMORY_DOC
     * 브로커로 전송할 데이터를 배치로 모으기 위해 설정할 버퍼 메모리 사이즈
     * If records are sent faster than they can be delivered to the server the producer will block for 'MAX_BLOCK_MS_CONFIG' after which it will throw an exception.
     * */
    configs[ProducerConfig.BUFFER_MEMORY_CONFIG] = 33554432 // default 32MB
    configs[ProducerConfig.RETRIES_CONFIG] = 2147483647 // default Integer.MAX_VALUE | 재전송 시도 횟수
    /**
     * @see org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_DOC
     * 배치로 전송할 레코드 최대 용량을 지정
     */
    configs[ProducerConfig.BATCH_SIZE_CONFIG] = 16384 // default 16KiB
    configs[ProducerConfig.LINGER_MS_CONFIG] = 0 // default | 배치를 전송하기 전까지 기다리는 최소 시간
//    configs[ProducerConfig.PARTITIONER_CLASS_CONFIG] = DefaultPartitioner::class.java // default
    /**
     * @see org.apache.kafka.clients.producer.ProducerConfig.ENABLE_IDEMPOTENCE_DOC
     * 멱등성 프로듀서로 동작할지 여부 설정
     */
    configs[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = false // default
    /**
     * @see org.apache.kafka.clients.producer.ProducerConfig.TRANSACTIONAL_ID_DOC
     * 프로듀서가 레코드를 전송할 때 레코드를 트랜잭션 단위로 묶을지 여부를 설정
     * 프로듀서의 고유한 트랜잭션 아이디를 설정할 수 있다
     * 이 값을 설정하면 트랜잭션 프로듀서로 동작한다
     */
//    configs[ProducerConfig.TRANSACTIONAL_ID_CONFIG] = null // default

    val producer = KafkaProducer<String, String>(configs)

    val messageKey = "hello"
    val messageValue = "Hello, Kafka producer"
    val record = ProducerRecord<String, String>(TOPIC_NAME, messageKey, messageValue)

    log.info("### Before send")
    producer.send(record)
    log.info("record: {}", record)
    log.info("### After send")

    producer.flush()
    producer.close()
}
