package me.zeroest.kafka.spring.producer.runner

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture

@Component
class CustomTemplate(
    private val customKafkaTemplate: KafkaTemplate<String, String>
): CommandLineRunner {
    companion object {
        private val log = LoggerFactory.getLogger(CustomTemplate::class.java)
        private const val TOPIC_NAME = "test"
    }

    override fun run(vararg args: String?) {
        log.info("Send message by custom template")
        val future: CompletableFuture<SendResult<String, String>> = customKafkaTemplate.send(TOPIC_NAME, "custom", "test")

        future.whenCompleteAsync { sendResult, exception ->
            if (exception != null) {
                log.error(exception.message)
                throw exception
            }

            log.info("result : $sendResult")
        }

        Thread.sleep(5000L)
    }
}

/*
2023-03-05T17:50:25.144+09:00  INFO 14414 --- [onPool-worker-1] m.z.k.s.producer.runner.CustomTemplate   : result : SendResult [producerRecord=ProducerRecord(topic=test, partition=null, headers=RecordHeaders(headers = [], isReadOnly = true), key=custom, value=test, timestamp=null), recordMetadata=test-0@71]
*/
