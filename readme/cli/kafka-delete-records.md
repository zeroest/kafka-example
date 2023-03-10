
# kafka-delete-records.sh

[A Short Guide to Removing Messages from a Kafka Topic](https://www.oak-tree.tech/blog/kafka-admin-remove-messages)

> 삭제 할 시간에 reset-offsets 방식이 더 안전할 수 있음  
> [kafka-consumer-groups 명령어 --reset-offsets 옵션을 확인할것](./kafka-consumer-groups.md)

가장 오래된 데이터(가장 낮은 숫자의 오프셋)부터 특정 시점의 오프셋까지 삭제

test 토픽의 0번 파티션에 0부터 100까지 데이터가 들어있을때  
test 토픽의 0번 파티션에 저장된 데이터중 0부터 50 오프셋 데이터까지 지우고 싶다면 아래와 같이 입력

```bash
vim delete-records.json

{"partitions": [{"topic": "test", "partition": 0, "offset": 50}], "version": 1}
```

```bash
./kafka-delete-records.sh \
    --bootstrap-server kafka1:9092 \
    --offset-json-file delete-records.json
```
