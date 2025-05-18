#!/bin/bash

case $1 in
"start")
        echo " --------启动 hadoop104 业务数据flume-------"
        ssh hadoop104 "nohup /opt/module/flume/bin/flume-ng agent -n a1 -c /opt/module/flume/conf -f /opt/module/flume/job/tms_kafka_to_hdfs.conf > /opt/module/flume/tms-f1.log 2>&1 &"
;;
"stop")

        echo " --------停止 hadoop104 业务数据flume-------"
        ssh hadoop104 "ps -ef | grep tms_kafka_to_hdfs | grep -v grep |awk '{print \$2}' | xargs -n1 kill"
;;
esac

