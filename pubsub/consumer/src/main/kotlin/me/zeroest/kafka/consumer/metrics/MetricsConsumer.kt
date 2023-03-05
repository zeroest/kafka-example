package me.zeroest.kafka.consumer.metrics

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.Metric
import org.apache.kafka.common.MetricName
import org.apache.kafka.common.errors.WakeupException
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.Properties

class MetricsConsumer

private val log = LoggerFactory.getLogger(MetricsConsumer::class.java)
private const val TOPIC_NAME = "hello.kafka"
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"
private const val GROUP_ID = "test-group"

private lateinit var consumer: KafkaConsumer<String, String>

fun main() {
    Runtime.getRuntime().addShutdownHook(ShutdownTread())

    val configs = Properties()
    configs[ConsumerConfig.GROUP_ID_CONFIG] = GROUP_ID
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS
    configs[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
    configs[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

    configs[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = false

    consumer = KafkaConsumer<String, String>(configs)
    consumer.subscribe(listOf(TOPIC_NAME))

    try {
        while (true) {
            for (metric: MutableMap.MutableEntry<MetricName, out Metric> in consumer.metrics()) {
                log.error("metric: {}", metric)
                if (
                    "records-lag-max" == metric.key.name() ||
                    "records-lag" == metric.key.name() ||
                    "records-lag-avg" == metric.key.name()
                ) {
                    val value: Metric = metric.value
                    log.info("${metric.key.name()} : ${value.metricValue()}")
                }
            }


            val records: ConsumerRecords<String, String> = consumer.poll(Duration.ofSeconds(3))

//        for (record in records) {
//            log.info("record: {}", record)
//        }

//        consumer.commitSync()
        }
    } catch (wakeup: WakeupException) {
        log.warn("Wakeup consumer")
    } finally {
        consumer.close()
    }
}

class ShutdownTread : Thread() {
    override fun run() {
        log.info("Shutdown hook")
        consumer.wakeup()
    }
}

/*
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag, group=consumer-fetch-manager-metrics, description=The latest lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=1}]=org.apache.kafka.common.metrics.KafkaMetric@3543df7d
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag, group=consumer-fetch-manager-metrics, description=The latest lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=2}]=org.apache.kafka.common.metrics.KafkaMetric@5b1f29fa
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag, group=consumer-fetch-manager-metrics, description=The latest lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=3}]=org.apache.kafka.common.metrics.KafkaMetric@62f68dff
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag, group=consumer-fetch-manager-metrics, description=The latest lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=0}]=org.apache.kafka.common.metrics.KafkaMetric@7c541c15
[main] INFO me.zeroest.kafka.consumer.metrics.MetricsConsumer - records-lag : 0.0

[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-max, group=consumer-fetch-manager-metrics, description=The max lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=3}]=org.apache.kafka.common.metrics.KafkaMetric@7096b474
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-max, group=consumer-fetch-manager-metrics, description=The max lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=2}]=org.apache.kafka.common.metrics.KafkaMetric@11981797
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-max, group=consumer-fetch-manager-metrics, description=The max lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=1}]=org.apache.kafka.common.metrics.KafkaMetric@45905bff
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-max, group=consumer-fetch-manager-metrics, description=The max lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=0}]=org.apache.kafka.common.metrics.KafkaMetric@2a2c13a8


[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-avg, group=consumer-fetch-manager-metrics, description=The average lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=2}]=org.apache.kafka.common.metrics.KafkaMetric@625abb97
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-avg, group=consumer-fetch-manager-metrics, description=The average lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=3}]=org.apache.kafka.common.metrics.KafkaMetric@5d1659ea
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-avg, group=consumer-fetch-manager-metrics, description=The average lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=0}]=org.apache.kafka.common.metrics.KafkaMetric@2ccca26f
[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-avg, group=consumer-fetch-manager-metrics, description=The average lag of the partition, tags={client-id=consumer-test-group-1, topic=hello_kafka, partition=1}]=org.apache.kafka.common.metrics.KafkaMetric@66b7550d

[main] ERROR me.zeroest.kafka.consumer.metrics.MetricsConsumer - metric: MetricName [name=records-lag-max, group=consumer-fetch-manager-metrics, description=The maximum lag in terms of number of records for any partition in this window. NOTE: This is based on current offset and not committed offset, tags={client-id=consumer-test-group-1}]=org.apache.kafka.common.metrics.KafkaMetric@dd0c991
*/
