package me.zeroest.kafka.consumer.commit

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class CommitAsyncConsumer

private val log = LoggerFactory.getLogger(CommitAsyncConsumer::class.java)
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

        // poll() 메서드로 리턴된 가장 마지막 레코드의 오프셋을 기준으로 커밋한다
        consumer.commitAsync { offsets, exception ->
            if (exception != null) {
                log.error("Commit failed for offsets: {}", offsets, exception)
            } else {
                log.info("Commit succeeded for offsets: {}", offsets)
            }
        }
        /*
        * [main] INFO me.zeroest.kafka.consumer.commit.CommitAsyncConsumer - Commit succeeded for offsets: {hello.kafka-3=OffsetAndMetadata{offset=4, leaderEpoch=null, metadata=''}, hello.kafka-2=OffsetAndMetadata{offset=6, leaderEpoch=null, metadata=''}, hello.kafka-1=OffsetAndMetadata{offset=25, leaderEpoch=6, metadata=''}, hello.kafka-0=OffsetAndMetadata{offset=16, leaderEpoch=null, metadata=''}}
        * [main] INFO me.zeroest.kafka.consumer.commit.CommitAsyncConsumer - record: ConsumerRecord(topic = hello.kafka, partition = 1, leaderEpoch = 6, offset = 25, CreateTime = 1677424395196, serialized key size = 5, serialized value size = 21, headers = RecordHeaders(headers = [], isReadOnly = false), key = hello, value = Hello, Kafka producer)
        * [main] INFO me.zeroest.kafka.consumer.commit.CommitAsyncConsumer - Commit succeeded for offsets: {hello.kafka-3=OffsetAndMetadata{offset=4, leaderEpoch=null, metadata=''}, hello.kafka-2=OffsetAndMetadata{offset=6, leaderEpoch=null, metadata=''}, hello.kafka-1=OffsetAndMetadata{offset=26, leaderEpoch=6, metadata=''}, hello.kafka-0=OffsetAndMetadata{offset=16, leaderEpoch=null, metadata=''}}
        * */
    }
}
