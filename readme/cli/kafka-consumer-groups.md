
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
