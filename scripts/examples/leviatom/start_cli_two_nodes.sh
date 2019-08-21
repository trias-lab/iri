#!/bin/bash

cp -r ../../front_end/server/ ./server1
cd server1
sed -e 's/8000/8001/g' main.go > main.go.1
mv main.go.1 main.go
go run main.go -host http://127.0.0.1:14700 &> go_cli1.log &

sleep 5

cd ..
cp -r ../../front_end/server/ ./server2
cd server2
sed -e 's/8000/8002/g' main.go > main.go.1
mv main.go.1 main.go
go run main.go -host http://127.0.0.1:14700 &> go_cli2.log &
