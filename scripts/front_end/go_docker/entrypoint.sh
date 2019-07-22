#!/bin/bash
echo $HOST_IP
go run main.go -host http://${HOST_IP}:14700 &> go.log &
sleep 2
tail -f /dev/null
