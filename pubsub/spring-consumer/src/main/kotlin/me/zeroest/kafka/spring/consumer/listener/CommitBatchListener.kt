package me.zeroest.kafka.spring.consumer.listener

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment

/**
 * application.yml에
 * spring.kafka.listener.type: BATCH 로 설정하여 배치 단위 레코드를 받는다
 * spring.kafka.listener.ack-mode: MANUAL_IMMEDIATE 로 설정하여 acknowledge() 호출 즉시 커밋한다
 */
//@Configuration
open class CommitBatchListener {
    companion object {
        private val log = LoggerFactory.getLogger(CommitBatchListener::class.java)
    }

    @KafkaListener(topics = ["test"], groupId = "test-group-00")
    fun commitBatchListener(records: ConsumerRecords<String, String>, ack: Acknowledgment) {
        records.forEach { record ->
            log.info("commitBatchListener: $record")
            ack.acknowledge()
            // MANUAL or MANUAL_IMMEDIATE 옵션 사용시 수동 커밋을 하기 위해 Acknowledgment 인스턴스를 받아야 한다
            // acknowledge() 메서드 호출함으로써 커밋을 수행
        }
    }

    @KafkaListener(topics = ["test"], groupId = "test-group-01")
    fun consumerCommitBatchListener(records: ConsumerRecords<String, String>, consumer: Consumer<String, String>) {
        records.forEach { record ->
            log.info("consumerCommitBatchListener: $record")
//            consumer.commitSync()
            consumer.commitAsync()
            // consumer 인스턴스의 commitSync() or commitAsync() 메서드를 호출하여 사용자가 원하는 타이밍에 커밋할 수 있도록 로직을 추가할 수 있다
            // !! 리스너가 커밋을 하지 않도록 AckMode는 MANUAL or MANUAL_IMMEDIATE로 설정해야 한다
        }
    }
}
