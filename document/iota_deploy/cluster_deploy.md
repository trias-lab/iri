## 多节点集群部署 

### 部署环境 
用户:trust  
操作系统：Ubuntu 18.04  
节点个数：5 
docker：18.06.1-ce  
python：2.7  

### 部署步骤

在每个节点执行如下步骤

```bash
git clone https://github.com/triasteam/StreamNet.git
cd StreamNet/scripts/examples/
vim run_go_iota_docker.sh 
# 修改tcp://ip_address:13700，把ip_address替换为节点ip,每个节点IP单独一行，形成多行，然后保存.
sh run_go_iota_docker.sh
```  
