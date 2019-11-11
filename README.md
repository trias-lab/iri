[![Build Status](https://travis-ci.org/iotaledger/iri.svg?branch=dev)](https://travis-ci.org/iotaledger/iri)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/dba5b7ae42024718893991e767390135)](https://www.codacy.com/app/iotaledger/iri?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=iotaledger/iri&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/dba5b7ae42024718893991e767390135)](https://www.codacy.com/app/iotaledger/iri?utm_source=github.com&utm_medium=referral&utm_content=iotaledger/iri&utm_campaign=Badge_Coverage)
![GitHub release](https://img.shields.io/github/release/iotaledger/iri.svg)
![license](https://img.shields.io/github/license/iotaledger/iri.svg)

<p align="center"><img src="https://github.com/triasteam/StreamNet/blob/dev/document/pictures/StreamNet.png" alt="StreamNet Logo" title="StreamNet"/></p>

# Trias StreamNet

The trias-lab/iri repository is the main StreamNet reference implementation, 
for the design details, please see yellow paper [[StreamNet]](https://github.com/triasteam/iri/blob/dev/document/yellow\_paper/StreamNet/StreamNet.pdf). 
The original iri reference could be found at [[iri]](https://github.com/iotaledger/iri).

-* **License:** GPLv3
## 1. Installing

The preferred option is that you compile yourself.
The second option is that you utilize the provided jar, 
which is released whenever there is a new update here: [Github Releases](https://github.com/trias-lab/iri/releases).

### 1.1 Running yourself
Make sure to have Maven and Java 8 installed on your computer.
#### 1.1.1 How to run one  streamnet service node

```bash  
git clone https://github.com/triasteam/StreamNet.git
cd StreamNet/scripts/examples/one_node/
./conflux_dag.sh
cd StreamNet/scripts/front_end/server
./run_go.sh
```  
#### 1.1.2 How to run docker  
Make sure to have docker-ce 18+ installed on your computer.  

```bash
cd  StreamNet/scripts/examples
./run_docker.sh
```  

#### 1.1.3 How to Test 

AddNode
```bash
curl -X POST \  
http://address:8000/AddNode \  
-H 'Content-Type: application/json' \  
-H 'cache-control: no-cache' \  
-d "{\"Attester\":\"<NODE1>\",\"Attestee\":\"<NODE2>\",\"Score\":<SCORE>}"

其中NODE1和NODE2为两个节点，Node1为Node2投了几分，SCORE为分数值
```
QueryNode

```bash
curl -X POST \  
http://address:8000/QueryNode \  
-H 'Content-Type: application/json' \  
-H 'cache-control: no-cache' \  
-d "{\"period\":<PERIOD>,\"numRank\":<MAX_NUMBER>}"

其中period和numrank的意思是找到第几阶段的排名前多少位
```

## 2. MISC

### 2.1 Cluster deployment 

Please refer [[Cluster deployment]](https://github.com/triasteam/StreamNet/blob/dev/document/iota_deploy/README.md) for details of how to deploy multiple nodes.

### 2.2 Frontend deployment

Please refer [[Frontend deployment]](https://github.com/triasteam/StreamNet/blob/dev/scripts/front_end/README.md) for details of how to deploy the frontend

### 2.3 Portainer deployment 

Please refer [[Portainer deployment]](https://github.com/triasteam/StreamNet/blob/dev/document/iota_deploy/portainer-deploy-info.md) for details of how to leverage the portainer to manage containers

## 3  Team

[Zhaoming Yin](https://stplaydog.github.io/), [Junqing Wang](https://wunder3605.github.io/), [Yahui Wang](https://wangyh2016.github.io/wangyahui.github.io/), [Haifeng Li](https://lihaifeng111.github.io/lihaifeng.github.io/), [Huafeng Li](https://lhfbc.github.io/)
