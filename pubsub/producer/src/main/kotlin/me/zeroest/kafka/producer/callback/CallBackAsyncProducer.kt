package me.zeroest.kafka.producer.callback

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import java.util.Properties

class CallBackAsyncProducer

private val log = LoggerFactory.getLogger(CallBackAsyncProducer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"

fun main() {
    val configs = Properties()
    configs[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
    configs[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name

    val producer = KafkaProducer<String, String>(configs)

    val messageKey = "P3"
    val messageValue = "message"
    val record = ProducerRecord<String, String>(TOPIC_NAME, messageKey, messageValue)

    log.info("### Before send")
    // CallBack Async
//    producer.send(record, ProducerCallback())
    producer.send(record) { metadata, exception ->
        if (exception != null) {
            log.error(exception.message, exception)
        } else {
            log.info("metadata.partition: {}", metadata.partition())
            log.info("metadata.offset: {}", metadata.offset())
        }
    }
    log.info("record: {}", record)
    log.info("### After send")

    producer.flush()
    producer.close()
}
