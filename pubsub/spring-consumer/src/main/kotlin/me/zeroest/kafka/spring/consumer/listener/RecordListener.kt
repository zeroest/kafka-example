package me.zeroest.kafka.spring.consumer.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.PartitionOffset
import org.springframework.kafka.annotation.TopicPartition


/**
 * application.yml에 spring.kafka.listener.type: SINGLE 로 설정하여 단건 레코드를 받는다
 */
//@Configuration
open class RecordListener {
    companion object {
        private val log = LoggerFactory.getLogger(RecordListener::class.java)
    }
/*
./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic test
*/

    /**
     * poll()이 호출되어 가져온 레코드들은 차례대로 개별 레코드의 메시지 값을 파라미터로 받게 된다
     */
    @KafkaListener(topics = ["test"], groupId = "test-group-00")
    fun recordListener(record: ConsumerRecord<String, String>) {
        log.info("recordListener: $record")
    }
/*
2023-03-05T18:34:59.995+09:00  INFO 17002 --- [ntainer#1-0-C-1] m.z.k.s.c.listener.RecordListener        : recordListener: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 72, CreateTime = 1678008898926, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = a)
2023-03-05T18:34:59.995+09:00  INFO 17002 --- [ntainer#1-0-C-1] m.z.k.s.c.listener.RecordListener        : recordListener: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 73, CreateTime = 1678008899336, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = b)
2023-03-05T18:34:59.995+09:00  INFO 17002 --- [ntainer#1-0-C-1] m.z.k.s.c.listener.RecordListener        : recordListener: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 74, CreateTime = 1678008899868, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = c)
*/

    /**
     * 메세지 값을 파라미터로 받는 리스너
     * StringDeserializer를 사용하여 String 클래스로 메세지 값을 전달받았다
     */
    @KafkaListener(topics = ["test"], groupId = "test-group-01")
    fun singleTopicListener(messageValue: String) {
        log.info("singleTopicListener: $messageValue")
    }
/*
2023-03-05T18:34:59.998+09:00  INFO 17002 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : singleTopicListener: a
2023-03-05T18:34:59.998+09:00  INFO 17002 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : singleTopicListener: b
2023-03-05T18:34:59.998+09:00  INFO 17002 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : singleTopicListener: c
*/

    /**
     * 개별 리스너에 카프카 컨슈머 옵션값을 부여하고 싶다면 properties 옵션을 사용
     */
    @KafkaListener(
        topics = ["test"], groupId = "test-group-02", properties = [
            "max.poll.interval.ms:60000",
            "auto.offset.reset:earliest"
        ]
    )
    fun singleTopicWithPropertiesListener(messageValue: String) {
        log.info("singleTopicWithPropertiesListener: $messageValue")
    }
/*
2023-03-05T18:45:52.969+09:00  INFO 18003 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : singleTopicWithPropertiesListener: a
2023-03-05T18:45:52.969+09:00  INFO 18003 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : singleTopicWithPropertiesListener: b
2023-03-05T18:45:52.969+09:00  INFO 18003 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : singleTopicWithPropertiesListener: c
*/

    /**
     * concurrency 옵션값에 해당하는 만큼 컨슈머 스레드를 만들어서 병렬처리
     * 10개 파티션에 10개의 컨슈머 스레드가 각각 할당되어 병렬처리량이 늘어남
     * */
    @KafkaListener(topics = ["test"], groupId = "test-group-03", concurrency = "3")
    fun concurrentTopicListener(messageValue: String) {
        log.info("concurrentTopicListener: $messageValue")
    }
/*
2023-03-05T18:48:44.234+09:00  INFO 18167 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : concurrencyTopicListener: a
2023-03-05T18:48:44.234+09:00  INFO 18167 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : concurrencyTopicListener: b
2023-03-05T18:48:44.234+09:00  INFO 18167 --- [ntainer#0-0-C-1] m.z.k.s.c.listener.RecordListener        : concurrencyTopicListener: c
*/

    /**
     * 특정 토픽의 특정 파티션만 구독하고 싶다면 topicPartitions 파라미터를 사용한다
     * partitionOffsets 를 사용하면 추가적으로 특정 파티션의 특정 오프셋까지 지정 가능
     * */
    @KafkaListener(
        groupId = "test-group-04", topicPartitions = [
            TopicPartition(topic = "test01", partitions = ["0", "1"]),
            TopicPartition(
                topic = "test02", partitionOffsets = [
                    PartitionOffset(partition = "0", initialOffset = "3")
                ]
            )
        ]
    )
    fun specificPartitionListener(record: ConsumerRecord<String, String>) {
        log.info("specificPartitionListener: $record")
    }
/*
./kafka-topics.sh \
    --delete \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic test01
./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --partitions 2 \
    --topic test01

./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic test01
 */
}
