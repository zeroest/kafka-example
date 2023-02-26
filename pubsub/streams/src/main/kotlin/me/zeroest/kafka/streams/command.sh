
# Create topic
./kafka-topics.sh \
    --create \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --partitions 3 \
    --replication-factor 2 \
    --config retention.ms=172800000 \
    --topic stream_log

# Produce log data
./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic stream_log
#    --property "parse.key=true" \
#    --property "key.separator=:"

# Consume copy data
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic stream_log_copy \
    --property print.key=true \
    --property key.separator="-" \
    --group local \
    --from-beginning