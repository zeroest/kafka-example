
# Create
./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --partitions 3 \
    --replication-factor 2 \
    --config retention.ms=172800000 \
    --topic hello.kafka

# Describe
./kafka-topics.sh \
  --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
  --describe \
  --topic hello.kafka

# Console Consume
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic hello.kafka \
    --property print.key=true \
    --property key.separator="-" \
    --group local \
    --from-beginning
