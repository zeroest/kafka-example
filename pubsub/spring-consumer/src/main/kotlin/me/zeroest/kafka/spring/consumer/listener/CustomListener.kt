package me.zeroest.kafka.spring.consumer.listener

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener

@Configuration
open class CustomListener {
    companion object {
        private val log = LoggerFactory.getLogger(CustomListener::class.java)
    }

    /**
     * @see me.zeroest.kafka.spring.consumer.config.CustomContainer.customContainerFactory
     */
    @KafkaListener(topics = ["test"], groupId = "test-group-00", containerFactory = "customContainerFactory")
    fun customListener(record: ConsumerRecord<String, String>) {
        log.info("customListener: $record")
    }
}
