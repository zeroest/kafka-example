package me.zeroest.kafka.streams.filter

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.LoggerFactory
import java.util.Properties

class StreamsFilter

private val log = LoggerFactory.getLogger(StreamsFilter::class.java)
private const val APPLICATION_NAME = "streams-filter-application"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val STREAM_LOG = "stream_log"
private const val STREAM_LOG_FILTER = "stream_log_filter"

fun main() {
    val configs = Properties()
    configs[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_NAME
    configs[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    configs[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java

    val builder = StreamsBuilder()
    val streamLog = builder.stream<String, String>(STREAM_LOG)
    val filteredStream = streamLog.filter { _/*key*/, value -> value.length > 5 }
    filteredStream.to(STREAM_LOG_FILTER)

    val streams = KafkaStreams(builder.build(), configs)
    log.info("Before start")
    streams.start()
    log.info("After start")
}
