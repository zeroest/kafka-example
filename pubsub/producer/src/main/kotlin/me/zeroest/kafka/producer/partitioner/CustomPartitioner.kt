package me.zeroest.kafka.producer.partitioner

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.InvalidRecordException
import org.apache.kafka.common.utils.Utils

class CustomPartitioner: Partitioner{
    override fun partition(
        topic: String,
        key: Any,
        keyBytes: ByteArray?,
        value: Any,
        valueBytes: ByteArray,
        cluster: Cluster
    ): Int {
        if (keyBytes == null) {
            throw InvalidRecordException("Need message key")
        }

        val keyString = (key as String)
        if (keyString.equals("P3") || keyString.equals("3") || keyString.equals("33")) {
            return 0
        }

        val partitions = cluster.partitionsForTopic(topic)
        val numPartitions = partitions.size
        return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions
    }

    override fun configure(configs: MutableMap<String, *>?) {}

    override fun close() {}
}