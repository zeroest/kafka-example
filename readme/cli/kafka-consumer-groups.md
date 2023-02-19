
# kafka-consumer-groups.sh

## Consumer group list

```bash
./kafka-consumer-groups.sh \
    --bootstrap-server kafka1:9092 \
    --list
```

## Consumer group detail

```bash
./kafka-consumer-groups.sh \
    --bootstrap-server kafka1:9092 \
    --group hello-group \
    --describe

GROUP           TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID     HOST            CLIENT-ID
hello-group     hello.kafka     0          0               0               0               -               -               -
hello-group     hello.kafka     1          2               2               0               -               -               -
hello-group     hello.kafka     2          1               1               0               -               -               -
hello-group     hello.kafka     3          0               0               0               -               -               -
```

## Consumer group offset reset

offset commit이 되지 않아 동일 오프셋으로 지속 컨슘시 사용
[A Short Guide to Removing Messages from a Kafka Topic](https://www.oak-tree.tech/blog/kafka-admin-remove-messages)

```bash
./kafka-consumer-groups.sh \
    --bootstrap-server {broker end point} \
    --group {consumer-group-name} \
    --topic {topic명} \
    --reset-offsets --to-latest --execute
```
