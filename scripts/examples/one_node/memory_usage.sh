#!/usr/bin/env bash

pid=$(ps -aux | grep java | grep -v grep | awk '{print $2}')
nohup pidstat -r -p $pid 1 | tee memory_usage_log.txt
