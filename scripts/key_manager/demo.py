#!/usr/bin/env python

#--------------------------------------------------------------------------------------------------------------------
#This demo is used helping to lean how to invoke "addNode" api.
#Fist, sign up your acount of privilege server to retrieve address and private key which can sign you tranction data
#and a privilege signature which is a perission to access the Go client.
#second, using signmessage.sign_and_verify method to sign your data within address and private key which can be got
#got from step 1.
#Third, build the request.
#--------------------------------------------------------------------------------------------------------------------

import json
from hashlib import sha256
import requests
import signmessage

#build conmmon parems
def buildRequest():
      req_json = json.loads("{}");
      req_json['attester'] = "10.0.0.4"
      req_json['attestee'] = "10.0.0.5"
      req_json['score'] = 1
      req_json['time'] = "2213223191"
      req_json['nonce'] = 1
      req_json['address'] = "13KQh492C4V7ZeaVHkqoPxRS1fRpMrhqQB"

      return req_json


#do post request, nothing throws means success.
#this is a success case, to mock a failure case, you can modify any of address, private key or authSign
def main():
  data = buildRequest()
  #params should be sorted before dumps to string
  msg = json.dumps(data, sort_keys=True)
  print msg.replace(" ","")
  #must have no space
  message = sha256(msg.replace(" ","")).hexdigest()
  print message
  address = "13KQ1h492C4V7ZeaVHkqoPxRS1fRpMrhqQB"
  privateKey = "KxSgY5kZnTYQfVc2eXnkqPuLwVyY221N3uSsQtRJx5kf2o2Eyde4"
  #the sign_and_verify method used to generage a sign, if private does not match address, it gos wrong
  sign =  signmessage.sign_and_verify(privateKey, message, address)
  print sign
  data['sign'] = sign
  #this is used to prove the node has privilege to access go client.
  data['authSign'] = "HB1LqLgEOWKDMhXpFVND4VFdxAdYWSSx7kUpXsZwsFxTUGt1BqZnRbRfgx/9Fy6S3OkYMWDGhICo9t1dZWFdukFd6areoDbyb27n+GgOatti54GB91xxzN+UtRpFUfHL9Vxr88EvavJXncpRoa9k/VXulj5u2VzZ4GFLsnia8rI="
  url = "http://127.0.0.1:8000/AddNode"
  res = requests.post(url,json=data)
  #returns 200 indicat success
  print res


if __name__ == '__main__':
  main()
