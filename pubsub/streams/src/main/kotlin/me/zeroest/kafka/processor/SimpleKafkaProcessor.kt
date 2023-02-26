package me.zeroest.kafka.processor

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.Topology
import org.slf4j.LoggerFactory
import java.util.Properties

class SimpleKafkaProcessor

private val log = LoggerFactory.getLogger(SimpleKafkaProcessor::class.java)
private const val APPLICATION_NAME = "streams-application"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val STREAM_LOG = "stream_log"
private const val STREAM_LOG_FILTER = "stream_log_filter"

fun main() {
    val configs = Properties()
    configs[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_NAME
    configs[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    configs[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java

    val topology = Topology()
    topology.addSource("Source", STREAM_LOG)
        .addProcessor("Process", FilterProcessor(), "Source")
        .addSink("Sink", STREAM_LOG_FILTER, "Process")

    val streams = KafkaStreams(topology, configs)
    log.info("Before start")
    streams.start()
    log.info("After start")
}
