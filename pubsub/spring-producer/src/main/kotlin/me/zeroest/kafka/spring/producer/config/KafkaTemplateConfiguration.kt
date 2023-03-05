package me.zeroest.kafka.spring.producer.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate

@Configuration
open class KafkaTemplateConfiguration {
    companion object {
        private val log = LoggerFactory.getLogger(KafkaTemplateConfiguration::class.java)
        private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
    }

    @Bean
    open fun customKafkaTemplate(): KafkaTemplate<String, String> {
        val props = mutableMapOf<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.ACKS_CONFIG] = "all"

        val producerFactory = DefaultKafkaProducerFactory<String, String>(props)
        return KafkaTemplate(producerFactory)
        // ReplyKafkaTemplate: 컨슈머가 특정 데이터를 전달받았는지 여부를 확인할 수 있다.
        // RoutingKafkaTemplate: 전송하는 토픽별로 옵션을 다르게 설정할 수 있다.
    }

}