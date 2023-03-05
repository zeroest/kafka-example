
# Standalone connect

## 커넥트 설정

`~/kafka/kafka_2.12-3.4.0/config/connect-standalone.properties`

```shell
# These are defaults. This file just demonstrates how to override some settings.
# 
# 커넥트와 연돌할 카프카 클러스터의 호스트와 포트
bootstrap.servers=kafka1:9092,kafka2:9092,kafka3:9092

# The converters specify the format of data in Kafka and how to translate it into Connect data. Every Connect user will
# need to configure these based on the format they want their data in when loaded from or stored into Kafka
# 
# 데이터를 카프카에 저장할 때 가져올 때 변환시 사용
# JsonConverter, StringConverter, ByteArrayConverter를 기본 제공
key.converter=org.apache.kafka.connect.json.JsonConverter
value.converter=org.apache.kafka.connect.json.JsonConverter

# Converter-specific settings can be passed in by prefixing the Converter's setting with the converter we want to apply
# it to
# 
# 스키마 형태를 사용하고 싶지 않다면 enable 옵션을 false로 설정
key.converter.schemas.enable=true
value.converter.schemas.enable=true

# 단일 모드 커넥트는 로컬 파일에 오프셋 정보를 저장
# 오프셋 정보는 소스 커넥터 또는 싱크 커넥터가 데이터 처리 시점을 저장하기 위해 사용
# 
# ex) 파일 소스 커넥터
# 파일을 읽어 토픽에 저장할 때 몇번째 줄까지 읽었는지를 오프셋에 저장한다
# 소스 커넥터가 종료되고 재시작하면 오프셋 정보를 확인하여 마지막으로 읽은 파일 위치부터 다시 읽어 토픽으로 데이터를 저장
# 해당 정보는 데이터를 처리하는 데에 있어서 중요한 역할을 하므로 다른 사용자나 시스템이 접근하지 않도록 주의해야 한다.
offset.storage.file.filename=/tmp/connect.offsets
# Flush much faster than normal, which is useful for testing/debugging
# 
# 태스크가 처리 완료한 오프셋을 커밋하는 주기를 설정한다
offset.flush.interval.ms=10000

# Set to a list of filesystem paths separated by commas (,) to enable class loading isolation for plugins
# (connectors, converters, transformations). The list should consist of top level directories that include
# any combination of:
# a) directories immediately containing jars with plugins and their dependencies
# b) uber-jars with plugins and their dependencies
# c) directories immediately containing the package directory structure of classes of plugins and their dependencies
# Note: symlinks will be followed to discover dependencies or plugins.
# Examples:
# plugin.path=/usr/local/share/java,/usr/local/share/kafka/plugins,/opt/connectors,
# 
# 플러그인 형태로 추가할 커넥터의 디렉토리 주소를 입력한다
# 오픈소스로 받거나 개발한 커넥터의 jar파일이 위치하는 디렉토리를 값으로 입력한다
# 커넥터가 실행될 때 디렉토리로부터 jar파일을 로드한다
# 커넥터 이외에도 직접 converter, transform 또한 플러그인으로 추가할 수 있다
plugin.path=/usr/local/share/java,/usr/local/share/kafka/plugins,/Users/zero/Program/kafka_2.12-3.4.0/libs
```

## 파일 소스 커넥터 설정

`~/kafka/kafka_2.12-3.4.0/config/connect-file-source.properties`

```shell
# 커넥터의 이름을 지정
# 커넥터의 이름은 커넥트에서 유일해야 한다
name=local-file-source
# 사용할 커넥터의 클래스 이름을 지정
# 카프카에서 제공하는 기본 클래스 중 하나인 FileStreamSource 클래스를 지정
# 파일 소스 커넥터를 구현한 클래스 파일
connector.class=FileStreamSource
# 커넥터로 실행할 태스크 개수
# 태스크 개수를 늘려서 병렬처리를 할 수 있다
# 다수의 파일을 읽어서 토픽에 저장하고 싶다면 태스크 개수를 늘려서 병렬처리 할 수 있다
tasks.max=1
# 읽을 파일의 위치를 지정한다
file=/tmp/test.txt
# 읽은 파일의 데이터를 저장할 토픽의 이름을 지정한다
topic=connect-test
```

## 단일 모드 커넥트 실행

```shell
echo 'hi\nhello\nkafka\nconnect\nis\nnew\nworld\ngood' > /tmp/test.txt
```

```shell
./connect-standalone.sh config/connect-standalone.properties \
  config/connect-file-source.properties
  
./connect-standalone.sh ~/Program/kafka_2.12-3.4.0/config/connect-standalone.properties \
  ~/Program/kafka_2.12-3.4.0/config/connect-file-source.properties
```

```shell
./kafka-console-consumer.sh \
    --bootstrap-server kafka1:9092,kafka2:9092,kafka3:9092 \
    --topic connect-test \
    --property print.key=true \
    --property key.separator="-" \
    --group local \
    --from-beginning
    
# null-{"schema":{"type":"string","optional":false},"payload":"hi"}
# null-{"schema":{"type":"string","optional":false},"payload":"hello"}
# null-{"schema":{"type":"string","optional":false},"payload":"kafka"}
# null-{"schema":{"type":"string","optional":false},"payload":"connect"}
# null-{"schema":{"type":"string","optional":false},"payload":"is"}
# null-{"schema":{"type":"string","optional":false},"payload":"new"}
# null-{"schema":{"type":"string","optional":false},"payload":"world"}
# null-{"schema":{"type":"string","optional":false},"payload":"good"}
```

```shell
echo newline >> /tmp/test.txt

# null-{"schema":{"type":"string","optional":false},"payload":"newline"}
```
