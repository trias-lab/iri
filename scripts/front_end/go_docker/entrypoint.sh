#!/bin/bash

go run main.go > go.log &
sleep 2
tail -f /dev/null
