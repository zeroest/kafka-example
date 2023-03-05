package me.zeroest.kafka.spring.consumer.config

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener
import org.springframework.kafka.listener.ContainerProperties

@Configuration
open class CustomContainer {
    companion object {
        private val log = LoggerFactory.getLogger(CustomContainer::class.java)
        private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
    }

    @Bean
    open fun customContainerFactory(): KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> {
        val props = HashMap<String, Any>()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(props)
        // 2개 이상의 컨슈머 리스너를 만들때 사용되며 concurrency를 1로 설정할 경우 1개 컨슈머 스레드로 실행된다
        val containerFactory = ConcurrentKafkaListenerContainerFactory<String, String>()

        // 리밸런스 리스너를 선언하기 위해 setConsumerRebalanceListener 메서드 호출
        // onPartitionsRevokedBeforeCommit - 커밋이 되기 전에 리밸런스가 발생했을 때 호출
        // onPartitionsRevokedAfterCommit - 커밋이 일어난 이후에 리밸런스가 발생했을 때 호출
        containerFactory.containerProperties.setConsumerRebalanceListener(object : ConsumerAwareRebalanceListener {
            override fun onPartitionsRevokedBeforeCommit(
                consumer: Consumer<*, *>,
                partitions: MutableCollection<TopicPartition>
            ) {
                super.onPartitionsRevokedBeforeCommit(consumer, partitions)
                log.info("onPartitionsRevokedBeforeCommit")
            }

            override fun onPartitionsRevokedAfterCommit(
                consumer: Consumer<*, *>,
                partitions: MutableCollection<TopicPartition>
            ) {
                super.onPartitionsRevokedAfterCommit(consumer, partitions)
                log.info("onPartitionsRevokedAfterCommit")
            }

            override fun onPartitionsAssigned(partitions: MutableCollection<TopicPartition>) {
                super.onPartitionsAssigned(partitions)
                log.info("onPartitionsAssigned")
            }

            override fun onPartitionsLost(partitions: MutableCollection<TopicPartition>) {
                super.onPartitionsLost(partitions)
                log.info("onPartitionsLost")
            }
        })

        containerFactory.setConcurrency(2)
        // 레코드 리스너를 사용함을 명시
        // 배치 리스너를 사용하고 싶다면 true로 설정
        containerFactory.isBatchListener = false
        // AckMode 를 설정
        containerFactory.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
        // 컨슈머 설정값을 가지고 있는 DefaultKafkaConsumerFactory 설정
        containerFactory.consumerFactory = consumerFactory

        return containerFactory
    }
}
