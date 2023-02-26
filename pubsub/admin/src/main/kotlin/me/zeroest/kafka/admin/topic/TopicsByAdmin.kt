package me.zeroest.kafka.admin.topic

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.TopicDescription
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.LoggerFactory
import java.util.Properties

class TopicsByAdmin

private val log = LoggerFactory.getLogger(TopicsByAdmin::class.java)
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"

fun main() {
    val configs = Properties()
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS

    val admin = AdminClient.create(configs)

    log.info("Get topics information")
//    val topicInformations: MutableMap<String, TopicDescription> = admin.describeTopics(listOf("hello.kafka", "test")).all().get()
//    val topicInformations: MutableMap<Uuid, TopicDescription> = admin.describeTopics(listOf("hello.kafka", "test")).allTopicIds().get()
    val topicInformations: MutableMap<String, TopicDescription> =
        admin.describeTopics(listOf("hello.kafka", "test")).allTopicNames().get()

    log.info("topics: {}", topicInformations)

    admin.close()
}
