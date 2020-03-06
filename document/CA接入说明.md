## 准备环境
```
# 更新代码
git clone https://github.com/triasteam/StreamNet.git

# 切换到分支
git checkout add_ca

# 启动服务
cd ~/StreamNet/scripts/example/one_node

# 启动14700端口
bash conflux_dag.sh
```

## 功能回归
 ```
 cd ~/StreamNet/scripts/front_end/server
 go run main.go -host=http://localhost:14700 -ca=false
 
 # 新增证实交易
 curl -s http://localhost:8000/AddNode -X POST -H "Content-Type: application/json" -d "{\"Attester\":\"10.0.1.3\",\"Attestee\":\"10.0.0.2\",\"Score\":\"1\",\"Time\":\"2213223190\",\"Nonce\":\"1\"}"
 
 # 查询
 curl -s -X POST http://localhost:8000/QueryNodes -H 'Content-Type:application/json' -H 'cache-control: no-cache' -d "{\"period\":1,\"numRank\":100}"
 ```
 
## 开启CA Certification valid

开启CA开关之后访问请求需要携带```Sign```和```OriData```。```OriData```是待签名字符串，```Sign```是使用私钥签名后的字符串，并使用```url.QueryEscape```做编码。

### 单元测试方式
```
# 执行单元测试可以模拟生成签名数据，如需接口测试则需要将签名拷贝使用
go test -v main_test.go main.go
```

### Restful 接口测试方式
 
```
 cd ~/StreamNet/scripts/front_end/server
 go run main.go -host=http://localhost:14700
 
 # 新增正式交易
 curl -s http://localhost:8000/AddNode -X POST -H "Content-Type: application/json" -d "{\"Attester\":\"10.0.0.3\",\"Attestee\":\"10.0.0.2\",\"Score\":\"1\",\"Time\":\"2213223190\",\"Nonce\":\"1\",\"Sign\":\"%7B%22cert%22%3A%22LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNkVENDQWh1Z0F3SUJBZ0lVZGZ2Sk9EZnhNeDFpUW1vbjk3czdIZ2NnV2Nzd0NnWUlLb1pJemowRUF3SXcKY3pFTE1Ba0dBMVVFQmhNQ1ZWTXhFekFSQmdOVkJBZ1RDa05oYkdsbWIzSnVhV0V4RmpBVUJnTlZCQWNURFZOaApiaUJHY21GdVkybHpZMjh4R1RBWEJnTlZCQW9URUc5eVp6RXVaWGhoYlhCc1pTNWpiMjB4SERBYUJnTlZCQU1UCkUyTmhMbTl5WnpFdVpYaGhiWEJzWlM1amIyMHdIaGNOTWpBd016QTFNVEV5TnpBd1doY05NakV3TXpBMU1URXkKTnpBd1dqQlVNUXN3Q1FZRFZRUUdFd0pEVGpFUU1BNEdBMVVFQ0JNSFFtVnBhbWx1WnpFUU1BNEdBMVVFQnhNSApRbVZwYW1sdVp6RU9NQXdHQTFVRUNoTUZWSEpwWVhNeEVUQVBCZ05WQkFzVENHVnVaMmx1WldWeU1Ga3dFd1lICktvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUU0cXlWOVJlMEs2bHZLUTB3YnlmQ0R6cFNUcEhsK2RCcTk5dG8KN0hha2ZlTFdYK2pkYTMwck9mNkQ5TVdqZTFOaUp4N3Byc3d0MC83UlhrNUd3SkNKN2FPQnF6Q0JxREFPQmdOVgpIUThCQWY4RUJBTUNCYUF3SFFZRFZSMGxCQll3RkFZSUt3WUJCUVVIQXdFR0NDc0dBUVVGQndNQ01Bd0dBMVVkCkV3RUIvd1FDTUFBd0hRWURWUjBPQkJZRUZIckNhY2ZKNzNOQ1B0Y2FTQzBpMHRPelNuWjdNQ3NHQTFVZEl3UWsKTUNLQUlIS1dKRFVMclg3WTFpRjVzZkxtTzlwSEJSbkRhS0lnRGJtSUxrdWNlZWsxTUIwR0ExVWRFUVFXTUJTQwpFalE1TGpJek15NHhPVEV1TmpBNk9EZzRPREFLQmdncWhrak9QUVFEQWdOSUFEQkZBaUVBOFl2TEVMM2ZLZ3J6CkhOOVJSOENMOGkrQnh3K0E5eFMyeE9Wd0FqOE4zWWdDSUJiOFNlMGhkV01MWi91NUpVYWc4a3BkWG00QndNWFoKcnJDTWtxQlBiM2d0Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K%22%2C%22signature%22%3A%22MEYCIQDBFuLv%2F7KitH%2FA7ln64m1gCPAQigsWXZxg1%2BclF0vN%2FwIhAIPmo2RBEFMBoE7WPYecY8yK3ki5oBT0ok9SxeSqCxP6%22%7D\",\"OriData\":\"abc\"}"
 
 # 查询
 curl -s -X POST http://localhost:8000/QueryNodes -H 'Content-Type:application/json' -H 'cache-control: no-cache' -d "{\"period\":1,\"numRank\":100,\"Sign\":\"%7B%22cert%22%3A%22LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUNkVENDQWh1Z0F3SUJBZ0lVZGZ2Sk9EZnhNeDFpUW1vbjk3czdIZ2NnV2Nzd0NnWUlLb1pJemowRUF3SXcKY3pFTE1Ba0dBMVVFQmhNQ1ZWTXhFekFSQmdOVkJBZ1RDa05oYkdsbWIzSnVhV0V4RmpBVUJnTlZCQWNURFZOaApiaUJHY21GdVkybHpZMjh4R1RBWEJnTlZCQW9URUc5eVp6RXVaWGhoYlhCc1pTNWpiMjB4SERBYUJnTlZCQU1UCkUyTmhMbTl5WnpFdVpYaGhiWEJzWlM1amIyMHdIaGNOTWpBd016QTFNVEV5TnpBd1doY05NakV3TXpBMU1URXkKTnpBd1dqQlVNUXN3Q1FZRFZRUUdFd0pEVGpFUU1BNEdBMVVFQ0JNSFFtVnBhbWx1WnpFUU1BNEdBMVVFQnhNSApRbVZwYW1sdVp6RU9NQXdHQTFVRUNoTUZWSEpwWVhNeEVUQVBCZ05WQkFzVENHVnVaMmx1WldWeU1Ga3dFd1lICktvWkl6ajBDQVFZSUtvWkl6ajBEQVFjRFFnQUU0cXlWOVJlMEs2bHZLUTB3YnlmQ0R6cFNUcEhsK2RCcTk5dG8KN0hha2ZlTFdYK2pkYTMwck9mNkQ5TVdqZTFOaUp4N3Byc3d0MC83UlhrNUd3SkNKN2FPQnF6Q0JxREFPQmdOVgpIUThCQWY4RUJBTUNCYUF3SFFZRFZSMGxCQll3RkFZSUt3WUJCUVVIQXdFR0NDc0dBUVVGQndNQ01Bd0dBMVVkCkV3RUIvd1FDTUFBd0hRWURWUjBPQkJZRUZIckNhY2ZKNzNOQ1B0Y2FTQzBpMHRPelNuWjdNQ3NHQTFVZEl3UWsKTUNLQUlIS1dKRFVMclg3WTFpRjVzZkxtTzlwSEJSbkRhS0lnRGJtSUxrdWNlZWsxTUIwR0ExVWRFUVFXTUJTQwpFalE1TGpJek15NHhPVEV1TmpBNk9EZzRPREFLQmdncWhrak9QUVFEQWdOSUFEQkZBaUVBOFl2TEVMM2ZLZ3J6CkhOOVJSOENMOGkrQnh3K0E5eFMyeE9Wd0FqOE4zWWdDSUJiOFNlMGhkV01MWi91NUpVYWc4a3BkWG00QndNWFoKcnJDTWtxQlBiM2d0Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K%22%2C%22signature%22%3A%22MEYCIQDBFuLv%2F7KitH%2FA7ln64m1gCPAQigsWXZxg1%2BclF0vN%2FwIhAIPmo2RBEFMBoE7WPYecY8yK3ki5oBT0ok9SxeSqCxP6%22%7D\",\"OriData\":\"abc\"}"
``` 

## 待完善功能
 - 证书过期
 - 证书更新


