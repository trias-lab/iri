## 接口文档

## 环境准备：
### StreamNet环境，配置和启动请参照项目Readme,端口设置为14700
### Go client环境， 请参照Go官方文档配置好Go运行环境。
  1. 启动
    ```
    cd ~/StreamNet/scripts/front_end/server
    go run main.go -host http://{ip/host}:14700
    ```

### AddNode接口文档
AuthSign: 权限签名，请登陆权限服务端获取；
sign：请求参数签名方法
1. 构造 attester，attestee，score, time, nonce, address参数的对象，并序列化成json字符串，按字母从小到大排序（json.marsle方法自带)，
2. 使用sha256对以上字符串进行散列得到字符串message;
3. 登陆权限服务器获取sk, address;
3. 并使用signmessage.sign_input_message方法对以上message进行签名

请求链接：http://localhost:8000/AddNode
请求参数：

|field | type | remark |
| -- | --| --|
|Attester | string |  |
|Attestee | string | attestee |
|Score | string | string of numberic, may be any int |
|Time   | string | string of timestamp |
|Nonce  | string | string of a int, generate by random|
|Address | string | generate by privilege server|
|Sign  | string | sign of attester,attestee,score,time,nonce,address with sort
|AuthSign | string | sign of address, using for validate privilege|


请求示例:
```
curl -s http://localhost:8000/AddNode -X POST -H "Content-Type: application/json" -d "{\"Attester\":\"10.0.0.1\",\"Attestee\":\"10.0.0.2\",\"Score\":1,\"Time\":\"2213223190\",\"Nonce\":1,\"Address\":\"1vofXj4Vf2cgJDQrbbN2Zc6gG9qmRmk96\",\"Sign\":\"H7PRixGEiR/EbFkLb5ZPR0vT4dwyo6RipNicjyiT7zE1dT2wggyosSlsPeKLvAMOmi3Tcr6enfbHrRNYkfUEJRo=\"}"
```


### queryNode接口文档
请求链接：http://localhost:8000/queryNodes
请求参数：
|field | type | remark
|--|--|--|
|period | int | all blocks is divide into periods per 100 count
|numrank | int | retrive how many blocks in one query
|address | string | generate by privilege server
|AuthSign | string | sign of address, using for validate privilege

示例：
```
curl -s http://localhost:8001/QueryNodes -X POST -H "Content-Type: application/json" -d "{\"period\":1,\"numRank\":100,\"Address\":\"13KQh492C4V7ZeaVHkqoPxRS1fRpMrhqQB\",\"AuthSign\":\"HB1LqLgEOWKDMhXpFVND4VFdxAdYWSSx7kUpXsZwsFxTUGt1BqZnRbRfgx/9Fy6S3OkYMWDGhICo9t1dZWFdukFd6areoDbyb27n+GgOatti54GB91xxzN+UtRpFUfHL9Vxr88EvavJXncpRoa9k/VXulj5u2VzZ4GFLsnia8rI=\"}"
```

## 附
以上，Address, AuthSign 需要从权限服务器获取：
（这里不演示权限服务器的部署启动步骤）
打开链接：
http://192.168.199.130/trias-dag/#/
找到右上角名字点击，进入后选择设置，选择地址和权限签名作为请求参数：
![图片2](https://raw.githubusercontent.com/jdyuankai/document/master/StreamNet/images/pic_trias_privilege_gen_sk.jpg)

## demo:
```
#!/usr/bin/env python

# --------------------------------------------------------------------------------------------------------------------
# This demo is used helping to lean how to invoke "addNode" api.
# Fist, sign up your acount of privilege server to retrieve address and private key which can sign you tranction data
# and a privilege signature which is a perission to access the Go client.
# Second, using signmessage.sign_and_verify method to sign your data within address and private key which can be got 
# got from step 1.
# Third, build the request.
# --------------------------------------------------------------------------------------------------------------------

import time
import json
from hashlib import sha256
import requests
import signmessage

# build conmmon parems
def buildRequest():
      req_json = json.loads("{}");
      req_json['attester'] = "10.0.0.4"
      req_json['attestee'] = "10.0.0.5"
      req_json['score'] = 1
      req_json['time'] = "2213223191"
      req_json['nonce'] = 1
      req_json['address'] = "13KQh492C4V7ZeaVHkqoPxRS1fRpMrhqQB"
                    
      return req_json
                    


# do post request, nothing throws means success.
# this is a success case, to mock a failure case, you can modify any of address, private key or authSign
def main():
  data = buildRequest()
  # params should be sorted before dumps to string
  msg = json.dumps(data, sort_keys=True)
  print msg.replace(" ","")
  # must have no space
  message = sha256(msg.replace(" ","")).hexdigest()
  print message
  address = "13KQ1h492C4V7ZeaVHkqoPxRS1fRpMrhqQB"
  privateKey = "KxSgY5kZnTYQfVc2eXnkqPuLwVyY221N3uSsQtRJx5kf2o2Eyde4"
  # the sign_and_verify method used to generage a sign, if private does not match address, it gos wrong
  sign =  signmessage.sign_and_verify(privateKey, message, address)
  print sign
  data['sign'] = sign
  # this is used to prove the node has privilege to access go client.
  data['authSign'] = "HB1LqLgEOWKDMhXpFVND4VFdxAdYWSSx7kUpXsZwsFxTUGt1BqZnRbRfgx/9Fy6S3OkYMWDGhICo9t1dZWFdukFd6areoDbyb27n+GgOatti54GB91xxzN+UtRpFUfHL9Vxr88EvavJXncpRoa9k/VXulj5u2VzZ4GFLsnia8rI="
  url = "http://127.0.0.1:8000/AddNode"
  res = requests.post(url,json=data)
  # returns 200 indicat success
  print res


if __name__ == '__main__':
  main()
```
