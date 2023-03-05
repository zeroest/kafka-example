package me.zeroest.kafka.spring.producer.runner

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.core.KafkaTemplate

/*
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic test \
    --property print.key=true \
    --property key.separator="-" \
    --group local \
    --from-beginning
*/
//@Component
class DefaultTemplate(
    val template: KafkaTemplate<String, String>
): CommandLineRunner {
    companion object {
        private val log = LoggerFactory.getLogger(DefaultTemplate::class.java)
        private const val TOPIC_NAME = "test"
    }

    override fun run(vararg args: String?) {
        for (i in 0 until 10) {
            log.info("Send record: $i")
            template.send(TOPIC_NAME, "$i", "test:$i")
        }
    }
}
