
# kafka-console-producer.sh

## Produce

UTF-8 기반 Byte로 변환 ByteArraySerializer로만 직렬화 됨  
즉, String이 아닌 타입으로는 직렬화하여 전송할 수 없다.

```bash
./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092 \
    --topic hello.kafka

>hello kafka
```

key.separator 설정하지 않으면 기본 설정은 Tab delimiter(\t)

```bash
./kafka-console-producer.sh \
    --bootstrap-server kafka1:9092 \
    --topic hello.kafka \
    --property "parse.key=true" \
    --property "key.separator=:"

>k1:v1
>k2:v2
```

