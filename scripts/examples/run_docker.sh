#!/bin/bash

SCRIPTPATH=$(cd "$(dirname "$0")"; pwd)
NAME=stream-iota
TAG=v0.1-streamnet

sudo rm -rf data
sudo rm -rf conf
mkdir data
mkdir conf
touch conf/neighbors
cd ../../
docker build -t ${NAME}:${TAG} .
sudo docker run -d -p 14700:14700 -p 13700:13700 --name ${NAME} -v ${SCRIPTPATH}/data:/iri/data -v ${SCRIPTPATH}/conf/neighbors:/iri/conf/neighbors ${NAME}:${TAG} /entrypoint.sh

CLINAME=stream-go
cd scripts/front_end
docker build -t ${CLINAME}:${TAG} -f  go_docker/Dockerfile  .
sudo  docker run -itd --net=host -e "HOST_IP=127.0.0.1" --name  ${CLINAME}  ${CLINAME}:${TAG}
