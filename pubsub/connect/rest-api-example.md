
# Rest API example

```shell
curl --location --request GET 'http://localhost:8083/'

{
    "version": "3.4.0",
    "commit": "2e1947d240607d53",
    "kafka_cluster_id": "RGTubqIUSiaNdDFUFM5LDw"
}
```

```shell
curl --location --request GET 'http://localhost:8083/connectors'

[
    "local-file-source"
]
```

```shell
curl --location --request GET 'http://localhost:8083/connectors/local-file-source/config'

{
    "connector.class": "FileStreamSource",
    "file": "/tmp/test.txt",
    "tasks.max": "1",
    "name": "local-file-source",
    "topic": "connect-test"
}
```

```shell
curl --location --request GET 'http://localhost:8083/connectors/local-file-source/status'

{
    "name": "local-file-source",
    "connector": {
        "state": "RUNNING",
        "worker_id": "127.0.0.1:8083"
    },
    "tasks": [
        {
            "id": 0,
            "state": "RUNNING",
            "worker_id": "127.0.0.1:8083"
        }
    ],
    "type": "source"
}
```

```shell
curl --location --request GET 'http://localhost:8083/connectors/local-file-source/tasks'

[
    {
        "id": {
            "connector": "local-file-source",
            "task": 0
        },
        "config": {
            "connector.class": "FileStreamSource",
            "file": "/tmp/test.txt",
            "task.class": "org.apache.kafka.connect.file.FileStreamSourceTask",
            "tasks.max": "1",
            "name": "local-file-source",
            "topic": "connect-test"
        }
    }
]
```

```shell
curl --location --request GET 'http://localhost:8083/connectors/local-file-source/tasks/0/status'

{
    "id": 0,
    "state": "RUNNING",
    "worker_id": "127.0.0.1:8083"
}
```

```shell
curl --location --request GET 'http://localhost:8083/connectors/local-file-source/topics'

{
    "local-file-source": {
        "topics": [
            "connect-test"
        ]
    }
}
```

```shell
curl --location --request GET 'http://localhost:8083/connector-plugins/'

[
    {
        "class": "org.apache.kafka.connect.file.FileStreamSinkConnector",
        "type": "sink",
        "version": "3.4.0"
    },
    {
        "class": "org.apache.kafka.connect.file.FileStreamSourceConnector",
        "type": "source",
        "version": "3.4.0"
    },
    {
        "class": "org.apache.kafka.connect.mirror.MirrorCheckpointConnector",
        "type": "source",
        "version": "3.4.0"
    },
    {
        "class": "org.apache.kafka.connect.mirror.MirrorHeartbeatConnector",
        "type": "source",
        "version": "3.4.0"
    },
    {
        "class": "org.apache.kafka.connect.mirror.MirrorSourceConnector",
        "type": "source",
        "version": "3.4.0"
    }
]
```
