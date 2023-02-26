package me.zeroest.kafka.admin.broker

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.ConfigResource
import org.slf4j.LoggerFactory
import java.util.Collections
import java.util.Properties

class BrokersByAdmin

private val log = LoggerFactory.getLogger(BrokersByAdmin::class.java)
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"

fun main() {
    val configs = Properties()
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS

    val admin = AdminClient.create(configs)

    log.info("Get broker information")
    for (node in admin.describeCluster().nodes().get()) {
        log.info("node: {}", node)
        val cr = ConfigResource(ConfigResource.Type.BROKER, node.idString())
        val describeConfigs = admin.describeConfigs(Collections.singleton(cr))
        describeConfigs.all().get().forEach { _/*broker*/, config ->
            config.entries().forEach { configEntry ->
                log.info("${configEntry.name()} = ${configEntry.value()}")
            }
        }
    }

    admin.close()
}
