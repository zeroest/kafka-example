package me.zeroest.kafka.producer.callback

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import java.lang.Exception

class ProducerCallback: Callback {
    companion object {
        private val log = LoggerFactory.getLogger(ProducerCallback::class.java)
    }
    override fun onCompletion(metadata: RecordMetadata, exception: Exception?) {
        if (exception != null) {
            log.error(exception.message, exception)
        } else {
            log.info("metadata.partition: {}", metadata.partition())
            log.info("metadata.offset: {}", metadata.offset())
        }
    }
}