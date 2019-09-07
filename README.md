# Jesque-Demo

This project shows the usage of Jesque.

# How to Run

First, you need config the redis in `application.properties`. Here are the defaults:
```
# redis config
redis.url=localhost
redis.port=26379
redis.password=
redis.connect.timeoutInMilis=1000

jesque.namespace=demo
jesque.queue=demo-queue

batch.task.namespace=tasks
```

Second, use `curl` （or other HTTP post tool) to post some data.
` curl 'http://localhost:8080/v1/submitDemoJobUpgrade' -H "Content-Type: application/json" -d '["hello","world"]
`