#!/bin/bash

# 将昨日格式化为 yyyy-MM-dd 字符串，作为 mock_date 的默认值
yesterday=`date -d "-1 day" +%F`

# 校验参数，不合法警告并退出
if [ $1 = "initial" ]
then 
	# 首次启动 Job 时判断是否有第二个参数，若没有则使用昨天日期，否则将其传递给 mock_date
	if [ q$2 = q ]
	then
		mock_date=$yesterday
	else
		mock_date=$2
	fi
elif [ $1 = "start" ]
then
	# 从检查点或保存点启动需要传入存储路径，若没有则退出
    if [ q$2 = q ]
    then 
        echo "请传入检查点或保存点存储路径"
        exit
    elif [ q$3 = q ]
    then
    	mock_date=$yesterday
    else
    	mock_date=$3
    fi
elif [ $1 = "stop" ]
then
	# 停止 Job 需要传入 JobID 和 application ID，第二个参数为 JobID，没有则退出
    if [ q$2 = q ]
    then 
        echo "请传入Flink-CDC JobID"
        exit
    # 若没有第三个参数则退出
    elif [ q$3 = q ]
    then 
        echo "请传入Flink-CDC 在 Yarn 的 application ID"
        exit
    fi
fi

case $1 in
"initial")
    flink run -t yarn-per-job -p 4 -d \
    -Dyarn.application.name=OdsApp \
    -Dyarn.application.queue=default \
    -Djobmanager.memory.process.size=1g \
    -Dtaskmanager.memory.process.size=1536mb \
    -Dtaskmanager.numberOfTaskSlots=2 \
    -Dtaskmanager.memory.managed.size=0 \
    -Dexecution.checkpointing.interval='30 s' \
    -Dexecution.checkpointing.mode=EXACTLY_ONCE \
    -Dexecution.checkpointing.timeout='1 min' \
    -Dexecution.checkpointing.min-pause='20 s' \
    -Dexecution.checkpointing.externalized-checkpoint-retention=RETAIN_ON_CANCELLATION \
    -Drestart-strategy=failure-rate \
    -Drestart-strategy.failure-rate.delay='3 min' \
    -Drestart-strategy.failure-rate.failure-rate-interval='1 d' \
    -Drestart-strategy.failure-rate.max-failures-per-interval=10 \
    -Dstate.backend=hashmap \
    -Dstate.checkpoint-storage=filesystem \
    -Dstate.checkpoints.dir=hdfs://mycluster/tms/ck/ods_app \
    -Denv.java.opts="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8" \
    -c com.atguigu.tms.realtime.app.ods.OdsApp \
    /opt/module/flink-1.13.6/job/tms-realtime-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --HADOOP_USER_NAME atguigu \
    --mock_date $mock_date
    ;;
"start")
    flink run -t yarn-per-job -p 4 -d \
    -s $2 \
    -Dyarn.application.name=OdsApp \
    -Dyarn.application.queue=default \
    -Djobmanager.memory.process.size=1g \
    -Dtaskmanager.memory.process.size=1536mb \
    -Dtaskmanager.numberOfTaskSlots=2 \
    -Dtaskmanager.memory.managed.size=0 \
    -Dexecution.checkpointing.interval='30 s' \
    -Dexecution.checkpointing.mode=EXACTLY_ONCE \
    -Dexecution.checkpointing.timeout='1 min' \
    -Dexecution.checkpointing.min-pause='20 s' \
    -Dexecution.checkpointing.externalized-checkpoint-retention=RETAIN_ON_CANCELLATION \
    -Drestart-strategy=failure-rate \
    -Drestart-strategy.failure-rate.delay='3 min' \
    -Drestart-strategy.failure-rate.failure-rate-interval='1 d' \
    -Drestart-strategy.failure-rate.max-failures-per-interval=10 \
    -Dstate.backend=hashmap \
    -Dstate.checkpoint-storage=filesystem \
    -Dstate.checkpoints.dir=hdfs://mycluster/tms/ck/ods_app \
    -Denv.java.opts="-Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8" \
    -c com.atguigu.tms.realtime.app.ods.OdsApp \
    /opt/module/flink-1.13.6/job/tms-realtime-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --HADOOP_USER_NAME atguigu \
    --mock_date $mock_date
    ;;
"stop")
    flink stop \
    --savepointPath hdfs://mycluster/tms/sp/ods_app \
    $2 \
    -yid $3
    ;;
"*")
    echo "参数不合法，第一个参数必须为 initial | start | stop"
    ;;
esac
