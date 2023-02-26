package me.zeroest.kafka.streams.join

import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.slf4j.LoggerFactory
import java.util.Properties

class KStreamJoinGlobalKTable

private val log = LoggerFactory.getLogger(KStreamJoinGlobalKTable::class.java)
private const val APPLICATION_NAME = "global-table-join-application"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val ADDRESS_GLOBAL_TABLE = "address_v2"
private const val ORDER_STREAM = "order"
private const val ORDER_JOIN_STREAM = "order_join"

fun main() {
    val configs = Properties()
    configs[StreamsConfig.APPLICATION_ID_CONFIG] = APPLICATION_NAME
    configs[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG] = Serdes.String()::class.java
    configs[StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG] = Serdes.String()::class.java

    val builder = StreamsBuilder()
    val addressGlobalTable = builder.globalTable<String, String>(ADDRESS_GLOBAL_TABLE)
    val orderStream = builder.stream<String, String>(ORDER_STREAM)

    orderStream
        .join(addressGlobalTable,
            { orderKey, _/*orderValue*/ -> orderKey },
            { order, address -> "$order send to $address" })
        .to(ORDER_JOIN_STREAM)

    val streams = KafkaStreams(builder.build(), configs)
    log.info("Before start")
    streams.start()
    log.info("After start")
}
