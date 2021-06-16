## DAG 排名结果验证

### 背景
测试同学反映从不同节点调用node rank排名接口得到的排名信息不一致。

### 目的
模拟插入、查询 node rank 排名信息，并对结果进行统计，以确认是否存在不一致问题以及探寻不一致问题规律。

### 环境准备
该脚本使用python3进行测试，需要安装如下包：
```
pandas === 1.1.5
PyYaml === 5.4.1
```

### 测试步骤
首先修改配置文件：
```
# DAG 主机IP地址和 DAG 访问端口
host:
  - address: 172.16.1.3
    port: 8000
  - address: 172.16.1.6
    port: 8000
  - address: 172.16.1.14
    port: 8000    
# 设定测试的TM节点个数
nodeNum: 10
# 设置添加的交易数，投票关系和分数均随机
count: 1000
# 查询的轮数，每一轮查询均会向以上配置的host的节点分别发送查询请求
round: 10
# 定义该节点为模拟创建交易节点
onlyWrite: False
# 定义分析数据的来源，True表示仅从生成的data.csv文件读取，False表示实时查询以上主机。
onlyRead: True
```
测试分两步进行，第一步启动"投票"任务向DAG增加数据；第二步分别向DAG节点发送查询请求。
```
# step 1: 
# 修改env.yaml文件配置 onlyWrite: True
nohup python3 main.py &> write.log &

# step 2: 
# 修改env.yaml文件配置 onlyWrite: False, onlyRead: False
python3 main.py
```

### 数据结果
step 2 的结果如果为0表示无差异，如果有差异可以将onlyRead设置为True，代码中接触 print(df2)然后重新跑该脚本以查看具体那一轮查询时发生的不一致，然后到data.csv详情中具体查看明细。