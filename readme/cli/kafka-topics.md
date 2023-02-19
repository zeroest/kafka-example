
# kafka-topics.sh

## Create 

```bash
./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092 \
    --partitions 3 \ # 파티션 개수
    --replication-factor 1 \ # 토픽의 파티션을 복제할 복제 개수
    --config retention.ms=172800000 \ # 토픽 데이터 유지기간
    --topic hello.kafka2

./kafka-topics.sh --create --bootstrap-server kafka1:9092 --partitions 3 --replication-factor 1 --config retention.ms=172800000 --topic hello.kafka2
```

## List

```bash
./kafka-topics.sh \
    --bootstrap-server kafka1:9092 \
    --list

./kafka-topics.sh --bootstrap-server kafka1:9092 --list
```

## Detail

```bash
./kafka-topics.sh \
    --bootstrap-server kafka1:9092 \
    --describe \
    --topic hello.kafka2

./kafka-topics.sh --bootstrap-server kafka1:9092 --describe --topic hello.kafka2

Topic: hello.kafka2	TopicId: mRRJI0iBQy2ugMPLwYpurQ	PartitionCount: 3	ReplicationFactor: 1	Configs: retention.ms=172800000
	Topic: hello.kafka2	Partition: 0	Leader: 3	Replicas: 3	Isr: 3
	Topic: hello.kafka2	Partition: 1	Leader: 1	Replicas: 1	Isr: 1
	Topic: hello.kafka2	Partition: 2	Leader: 2	Replicas: 2	Isr: 2
```

## Change Config

### Partition count

```bash
./kafka-topics.sh \
    --bootstrap-server kafka1:9092 \
    --topic hello.kafka \
    --alter \
    --partitions 4

./kafka-topics.sh --bootstrap-server kafka1:9092 --topic hello.kafka --alter --partitions 4
```

### Retention

```bash
./kafka-configs.sh \
    --bootstrap-server kafka1:9092 \
    --entity-type topics \
    --entity-name hello.kafka \
    --alter \
    --add-config retention.ms=86400000

./kafka-configs.sh \
    --bootstrap-server kafka1:9092 \
    --entity-type topics \
    --entity-name hello.kafka \
    --describe

Dynamic configs for topic hello.kafka are:
  retention.ms=86400000 sensitive=false synonyms={DYNAMIC_TOPIC_CONFIG:retention.ms=86400000}
```
