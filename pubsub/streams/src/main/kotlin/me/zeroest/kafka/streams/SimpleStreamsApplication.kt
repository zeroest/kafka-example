package me.zeroest.kafka.streams

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.LoggerFactory
import java.util.Properties

class SimpleStreamsApplication

private val log = LoggerFactory.getLogger(SimpleStreamsApplication::class.java)
private const val APPLICATION_NAME = "streams-application"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val STREAM_LOG = "stream_log"
private const val STREAM_LOG_COPY = "stream_log_copy"

fun main() {
    val configs = Properties()
    configs[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_NAME
    configs[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    configs[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java

    val builder = StreamsBuilder()
    val streamLog = builder.stream<String, String>(STREAM_LOG)
    streamLog.to(STREAM_LOG_COPY)

    val streams = KafkaStreams(builder.build(), configs)
    log.info("Before start")
    streams.start()
    log.info("After start")
}
