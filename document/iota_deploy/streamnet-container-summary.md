# StreamNet中服务容器化总结 #

## iota service ##

```bash
1.build image
git clone https://github.com/triasteam/StreamNet.git
cd StreamNet/
docker build -t ${NAME}:${TAG} .

2.start container
sudo docker run -d -p 14700:14700 -p 13700:13700 --name ${NAME} -v ${DATAPATH}:/iri/data -v ${CONFPATH}:/iri/conf/neighbors ${NAME}:${TAG} /entrypoint.sh
#sudo docker run -itd --net=host  --name ${NAME} -v ${SCRIPTPATH}/data:/iri/data -v ${SCRIPTPATH}/conf/neighbors:/iri/conf/neighbors ${NAME}:${TAG} /entrypoint.sh

```  
## go client service ##
```bash
1.build image
cd StreamNet/scripts/front_end
sudo docker build -t ${NAME}:${TAG}  -f  go_docker/Dockerfile .

2.start container
sudo  docker run -itd -p 8000:8000 -e "HOST_IP=$IP" --name  ${NAME}  ${NAME}:${TAG}
#sudo  docker run -itd --net=host -e "HOST_IP=127.0.0.1" --name  ${NAME}  ${NAME}:${TAG}

```  
## Leviatom web service ##
```bash
1.build image
cd StreamNet/scripts/front_end
sudo docker build -t ${NAME}:${TAG} --build-arg HOST_IP=${HOST_IP} .

2.start container
sudo docker run -itd -p 80:80 -p 8000:8000  --name ${NAME} ${NAME}:${TAG}
```  

## mysql service ##
```bash
1.build image
cd StreamNet/scripts/front_end/trias-oauth/
sudo docker build -t ${NAME}:${TAG} -f mysql_docker/Dockerfile .

2.start container
sudo docker run -itd -p 3306:3306 --name ${NAME} ${NAME}:${TAG}

```  

## Parameter Description ##
```bash
1.以上各个服务中{NAME}为镜像名字,${TAG}为镜像标签，所起名字最好和业务服务相关，做到见名知意
2.iota service 中${DATAPATH}为数据挂载目录,${CONFPATH}为配置文件目录，主要用于neighbor node配置
3.go client  service 中HOST_IP为要访问的iota的服务的端口地址
```  
