package me.zeroest.kafka.spring.consumer.listener

import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener

/**
 * application.yml에 spring.kafka.listener.type: BATCH 로 설정하여 배치 단위 레코드를 받는다
 */
//@Configuration
open class BatchListener {
    companion object {
        private val log = LoggerFactory.getLogger(BatchListener::class.java)
    }

    /**
     * poll()이 호출되어 가져온 ConsumerRecords를 리턴받아 사용하는 것과 동일하다
     */
    @KafkaListener(topics = ["test"], groupId = "test-group-00")
    fun batchListener(records: ConsumerRecords<String, String>) {
        records.forEach { record ->
            log.info("batchListener by record: $record")
        }
    }
/*
2023-03-05T19:39:44.457+09:00  INFO 29563 --- [ntainer#0-0-C-1] m.z.k.s.consumer.listener.BatchListener  : batchListener by record: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 81, CreateTime = 1678012783388, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = a)
2023-03-05T19:39:53.313+09:00  INFO 29563 --- [ntainer#0-0-C-1] m.z.k.s.consumer.listener.BatchListener  : batchListener by record: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 82, CreateTime = 1678012792293, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = b)
2023-03-05T19:39:58.210+09:00  INFO 29563 --- [ntainer#0-0-C-1] m.z.k.s.consumer.listener.BatchListener  : batchListener by record: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 83, CreateTime = 1678012797184, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = c)
*/

    @KafkaListener(topics = ["test"], groupId = "test-group-01")
    fun batchListener(messageValues: List<String>) {
        messageValues.forEach { messageValue ->
            log.info("batchListener by value: $messageValue")
        }
    }
/*
2023-03-05T19:39:44.456+09:00  INFO 29563 --- [ntainer#1-0-C-1] m.z.k.s.consumer.listener.BatchListener  : batchListener by value: a
2023-03-05T19:39:53.312+09:00  INFO 29563 --- [ntainer#1-0-C-1] m.z.k.s.consumer.listener.BatchListener  : batchListener by value: b
2023-03-05T19:39:58.209+09:00  INFO 29563 --- [ntainer#1-0-C-1] m.z.k.s.consumer.listener.BatchListener  : batchListener by value: c
*/

    @KafkaListener(topics = ["test"], groupId = "test-group-02", concurrency = "3")
    fun concurrentBatchListener(records: ConsumerRecords<String, String>) {
        records.forEach { record ->
            log.info("concurrentBatchListener by record: $record")
        }
    }
/*
2023-03-05T19:39:44.456+09:00  INFO 29563 --- [ntainer#2-0-C-1] m.z.k.s.consumer.listener.BatchListener  : concurrentBatchListener by record: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 81, CreateTime = 1678012783388, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = a)
2023-03-05T19:39:53.312+09:00  INFO 29563 --- [ntainer#2-0-C-1] m.z.k.s.consumer.listener.BatchListener  : concurrentBatchListener by record: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 82, CreateTime = 1678012792293, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = b)
2023-03-05T19:39:58.209+09:00  INFO 29563 --- [ntainer#2-0-C-1] m.z.k.s.consumer.listener.BatchListener  : concurrentBatchListener by record: ConsumerRecord(topic = test, partition = 0, leaderEpoch = 5, offset = 83, CreateTime = 1678012797184, serialized key size = -1, serialized value size = 1, headers = RecordHeaders(headers = [], isReadOnly = false), key = null, value = c)
*/
}
