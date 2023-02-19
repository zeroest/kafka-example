
# kafka-verifiable

String 타입 메세지 값을 코드 없이 주고 받을 수 있다.  
토픽에 데이터를 전송하여 간단한 네트워크 통신 테스트 진행.  

## Producer

```bash
./kafka-verifiable-producer.sh \
    --bootstrap-server kafka1:9092 \
    --max-messages 10 \ # -1일시 무제한
    --topic verify-test

{"timestamp":1676819590041,"name":"startup_complete"}
[2023-02-20 00:13:10,167] WARN [Producer clientId=producer-1] Error while fetching metadata with correlation id 1 : {verify-test=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
{"timestamp":1676819590323,"name":"producer_send_success","key":null,"value":"0","offset":0,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"1","offset":1,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"2","offset":2,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"3","offset":3,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"4","offset":4,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"5","offset":5,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"6","offset":6,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"7","offset":7,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"8","offset":8,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590325,"name":"producer_send_success","key":null,"value":"9","offset":9,"partition":0,"topic":"verify-test"}
{"timestamp":1676819590329,"name":"shutdown_complete"}
{"timestamp":1676819590329,"name":"tool_data","sent":10,"acked":10,"target_throughput":-1,"avg_throughput":34.602076124567475}
```

## Consumer

```bash
./kafka-verifiable-consumer.sh \
    --bootstrap-server kafka2:9092 \
    --topic verify-test \
    --group-id test-group

{"timestamp":1676819845463,"name":"startup_complete"}
{"timestamp":1676819845698,"name":"partitions_assigned","partitions":[{"topic":"verify-test","partition":0}]}

{"timestamp":1676819822468,"name":"records_consumed","count":356,"partitions":[{"topic":"verify-test","partition":0,"count":356,"minOffset":550506,"maxOffset":550861}]}
{"timestamp":1676819822476,"name":"offsets_committed","offsets":[{"topic":"verify-test","partition":0,"offset":550862}],"success":true}

^C{"timestamp":1676819851627,"name":"partitions_revoked","partitions":[{"topic":"verify-test","partition":0}]}
{"timestamp":1676819851654,"name":"shutdown_complete"}
```
