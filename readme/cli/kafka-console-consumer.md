
# kafka-console-consumer.sh

## Consume

```bash
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092 \
    --topic hello.kafka \
    --from-beginning
```

```bash
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092 \
    --topic hello.kafka \
    --property print.key=true \
    --property key.separator="-" \
    --group hello-group \
    --from-beginning
```
