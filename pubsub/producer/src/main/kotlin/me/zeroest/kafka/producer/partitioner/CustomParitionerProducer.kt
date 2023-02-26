package me.zeroest.kafka.producer.partitioner

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

class CustomParitionerProducer

private val log = LoggerFactory.getLogger(CustomParitionerProducer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"

fun main() {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    // 커스텀 파티셔너 지정
    configs[ProducerConfig.PARTITIONER_CLASS_CONFIG] = CustomPartitioner::class.java

    val producer = KafkaProducer<String, String>(configs)

    val messageKey = "P0"
    val messageValue = "this message must be in partition number 0"
    val record = ProducerRecord<String, String>(TOPIC_NAME, messageKey, messageValue)

    log.info("### Before send")
    producer.send(record)
    log.info("record: {}", record)
    log.info("### After send")

    producer.flush()
    producer.close()
}
