#!/bin/bash

CLASSPATH=.:/home/hadoop/ctfan/jade/jar:/home/hadoop/jade/add-ons/migration/lib/migration.jar:/home/hadoop/jade/lib/commons-codec/commons-codec-1.3.jar:/home/hadoop/jade/lib/jade.jar:/home/hadoop/hadoop/hadoop-ant-1.2.1.jar:/home/hadoop/hadoop/hadoop-tools-1.2.1.jar:/home/hadoop/hadoop/ivy/ivy-2.1.0.jar:/home/hadoop/hadoop/src/contrib/thriftfs/lib/hadoopthriftapi.jar:/home/hadoop/hadoop/src/contrib/thriftfs/lib/libthrift.jar:/home/hadoop/hadoop/hadoop-client-1.2.1.jar:/home/hadoop/hadoop/lib/jackson-mapper-asl-1.8.8.jar:/home/hadoop/hadoop/lib/kfs-0.2.2.jar:/home/hadoop/hadoop/lib/mockito-all-1.8.5.jar:/home/hadoop/hadoop/lib/commons-math-2.1.jar:/home/hadoop/hadoop/lib/jersey-server-1.8.jar:/home/hadoop/hadoop/lib/commons-beanutils-core-1.8.0.jar:/home/hadoop/hadoop/lib/log4j-1.2.15.jar:/home/hadoop/hadoop/lib/commons-logging-api-1.0.4.jar:/home/hadoop/hadoop/lib/hadoop-thriftfs-1.2.1.jar:/home/hadoop/hadoop/lib/jets3t-0.6.1.jar:/home/hadoop/hadoop/lib/jsp-2.1/jsp-api-2.1.jar:/home/hadoop/hadoop/lib/jsp-2.1/jsp-2.1.jar:/home/hadoop/hadoop/lib/xmlenc-0.52.jar:/home/hadoop/hadoop/lib/jersey-json-1.8.jar:/home/hadoop/hadoop/lib/commons-lang-2.4.jar:/home/hadoop/hadoop/lib/asm-3.2.jar:/home/hadoop/hadoop/lib/commons-httpclient-3.0.1.jar:/home/hadoop/hadoop/lib/servlet-api-2.5-20081211.jar:/home/hadoop/hadoop/lib/jasper-runtime-5.5.12.jar:/home/hadoop/hadoop/lib/commons-logging-1.1.1.jar:/home/hadoop/hadoop/lib/jetty-util-6.1.26.jar:/home/hadoop/hadoop/lib/commons-digester-1.8.jar:/home/hadoop/hadoop/lib/commons-daemon-1.0.1.jar:/home/hadoop/hadoop/lib/commons-configuration-1.6.jar:/home/hadoop/hadoop/lib/commons-io-2.1.jar:/home/hadoop/hadoop/lib/junit-4.5.jar:/home/hadoop/hadoop/lib/hadoop-capacity-scheduler-1.2.1.jar:/home/hadoop/hadoop/lib/aspectjrt-1.6.11.jar:/home/hadoop/hadoop/lib/core-3.1.1.jar:/home/hadoop/hadoop/lib/jackson-core-asl-1.8.8.jar:/home/hadoop/hadoop/lib/hsqldb-1.8.0.10.jar:/home/hadoop/hadoop/lib/commons-codec-1.4.jar:/home/hadoop/hadoop/lib/aspectjtools-1.6.11.jar:/home/hadoop/hadoop/lib/jdeb-0.8.jar:/home/hadoop/hadoop/lib/commons-el-1.0.jar:/home/hadoop/hadoop/lib/commons-beanutils-1.7.0.jar:/home/hadoop/hadoop/lib/commons-cli-1.2.jar:/home/hadoop/hadoop/lib/slf4j-api-1.4.3.jar:/home/hadoop/hadoop/lib/jasper-compiler-5.5.12.jar:/home/hadoop/hadoop/lib/jersey-core-1.8.jar:/home/hadoop/hadoop/lib/oro-2.0.8.jar:/home/hadoop/hadoop/lib/jsch-0.1.42.jar:/home/hadoop/hadoop/lib/slf4j-log4j12-1.4.3.jar:/home/hadoop/hadoop/lib/jetty-6.1.26.jar:/home/hadoop/hadoop/lib/commons-collections-3.2.1.jar:/home/hadoop/hadoop/lib/hadoop-fairscheduler-1.2.1.jar:/home/hadoop/hadoop/lib/commons-net-3.1.jar:/home/hadoop/hadoop/hadoop-core-1.2.1.jar:/home/hadoop/hadoop/hadoop-test-1.2.1.jar:/home/hadoop/hadoop/hadoop-minicluster-1.2.1.jar:/home/hadoop/hadoop/hadoop-examples-1.2.1.jar:/home/hadoop/hadoop/contrib/gridmix/hadoop-gridmix-1.2.1.jar:/home/hadoop/hadoop/contrib/datajoin/hadoop-datajoin-1.2.1.jar:/home/hadoop/hadoop/contrib/index/hadoop-index-1.2.1.jar:/home/hadoop/hadoop/contrib/failmon/hadoop-failmon-1.2.1.jar:/home/hadoop/hadoop/contrib/vaidya/hadoop-vaidya-1.2.1.jar:/home/hadoop/hadoop/contrib/hdfsproxy/hdfsproxy-2.0.jar:/home/hadoop/hadoop/contrib/streaming/hadoop-streaming-1.2.1.jar
export CLASSPATH;

#/home/hadoop/hadoop/bin/hadoop fs -rmr /usr/ctfan/output;

NOW=$(date +%s)

FILE="/home/hadoop/ctfan/scripLog/${NOW}.log"

NOW=$(date +%c)

echo $NOW

echo $NOW >> $FILE

java jade.Boot -host 120.126.145.102 -container -container-name private202 -jade_core_messaging_MessageManager_maxqueuesize 40000000 -jade_http_mtp_proxyHost 120.126.145.102 MPI202Admin:tw.idv.ctfan.cloud.middleware.MPI.MPIAdminAgent\("/home/hadoop/ctfan, 120.126.145.102, hdp207, hdp208"\) 2>&1 | tee $FILE | tee

