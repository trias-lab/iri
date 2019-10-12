#!/usr/bin/env bash

nohup go run main.go -host http://127.0.0.1:14700 &> go.log &
