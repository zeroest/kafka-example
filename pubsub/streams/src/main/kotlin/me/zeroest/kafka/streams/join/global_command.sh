
# Create topics
./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --partitions 2 \
    --topic address_v2

./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --partitions 3 \
    --topic order

./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --partitions 3 \
    --topic order_join

# Produce data
./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic address_v2 \
    --property "parse.key=true" \
    --property "key.separator=:"

./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic order \
    --property "parse.key=true" \
    --property "key.separator=:"

# Consume join data
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic order_join \
    --property print.key=true \
    --property key.separator="-" \
    --group local \
    --from-beginning
