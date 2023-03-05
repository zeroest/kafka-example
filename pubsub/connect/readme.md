
# Kafka connect

## [단일 모드 커넥트](./standalone.md)

## Rest API 커넥트 상태 조회

[[Doc] Connect REST Interface](https://docs.confluent.io/platform/current/connect/references/restapi.html)

커넥터 플러그인 종류, 태스크 상태, 커넥터 상태 등을 조회할 수 있다.

[Rest API example](./rest-api-example.md)

| Method | Url                                                 | Description           |
| ------ | --------------------------------------------------- | --------------------- |
| GET    | /                                                   | 실행 중인 커넥트 정보 확인       |
| GET    | /connectors                                         | 실행 중인 커넥터 이름 확인       |
| POST   | /connectors                                         | 새로운 커넥터 생성 요청         |
| GET    | /connectors/{connectorName}                         | 실행 중인 커넥터 정보 확인       |
| GET    | /connectors/{connectorName}/config                  | 실행 중인 커넥터의 설정값 확인     |
| PUT    | /connectors/{connectorName}/config                  | 실행 중인 커넥터 설정값 변경 요청   |
| GET    | /connectors/{connectorName}/status                  | 실행 중인 커넥터 상태 확인       |
| POST   | /connectors/{connectorName}/restart                 | 실행 중인 커넥터 재시작 요청      |
| PUT    | /connectors/{connectorName}/pause                   | 커넥터 일시 중시 요청          |
| PUT    | /connectors/{connectorName}/resume                  | 일시 중지된 커넥터 실행 요청      |
| DELETE | /connectors/{connectorName}/                        | 실행 중인 커넥터 종료          |
| GET    | /connectors/{connectorName}/tasks                   | 실행 중인 커넥터의 태스크 정보 확인  |
| GET    | /connectors/{connectorName}/tasks/{taskId}/status  | 실행 중인 커넥터의 태스크 상태 확인  |
| POST   | /connectors/{connectorName}/tasks/{taskId}/restart | 실행 중인 커넥터의 태스크 재시작 요청 |
| GET    | /connectors/{connectorName}/topics                  | 커넥터별 연동된 토픽 정보 확인     |
| GET    | /connector-plugins/                                 | 커넥트에 존재하는 커넥터 플러그인 확인 |
| PUT    | /connector-plugins/{pluginName}/config/validate     | 커넥터 생성 시 설정값 유효 여부 확인 |
