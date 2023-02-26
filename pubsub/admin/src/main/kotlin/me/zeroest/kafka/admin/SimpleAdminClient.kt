package me.zeroest.kafka.admin

import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.DescribeClusterResult
import org.apache.kafka.clients.admin.NewPartitions
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.slf4j.LoggerFactory
import java.util.Properties

/**
 * 카프카 클라이언트 내부 옵션들을 설정하거나 조회하기 위해 AdminClient 클래스 제공
 * 클러스터의 옵션과 관련된 부분을 자동화 할 수 있다
 */
class SimpleAdminClient

private val log = LoggerFactory.getLogger(SimpleAdminClient::class.java)
private const val BOOTSTRAP_SERVERS = "kafka1:9092,kafka2:9092,kafka3:9092"

fun main() {
    val configs = Properties()
    configs[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = BOOTSTRAP_SERVERS

    val admin = AdminClient.create(configs)

    // 브로커 정보 조회
    val describeCluster: DescribeClusterResult = admin.describeCluster()
    log.info("controller: {}", describeCluster.controller().get().toString())
    log.info("nodes: {}", describeCluster.nodes().get().toString())
    // 토픽 리스트 조회
    val listTopics = admin.listTopics()
    log.info("listTopics: {}", listTopics.names().get().toString())
    // 컨슈머 그룹 조회
    val listConsumerGroups = admin.listConsumerGroups()
    log.info("consumerGroups: {}", listConsumerGroups.all().get().toString())
    // 신규 토픽 생성
    val createTopics: CreateTopicsResult = admin.createTopics(mutableListOf(NewTopic("topicName", 3, 2)))
    log.info("createTopics: {}", createTopics.all().get().toString())
    // 파티션 개수 변경
    val createPartitions = admin.createPartitions(mutableMapOf(Pair("topicName", NewPartitions.increaseTo(4))))
    log.info("partitions: {}", createPartitions.all().get())
    // 접근 제어 규칙 생성
//    admin.createAcls(mutableListOf(AclBinding()))

    admin.close()
}
