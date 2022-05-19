# -XX:+PrintGCDetails
#java -jar -XX:+UseZGC -XX:+UseNUMA -Xms8G -Xmx8G -XX:ReservedCodeCacheSize=128m -XX:InitialCodeCacheSize=128m -XX:ConcGCThreads=2 -XX:ParallelGCThreads=6 -XX:ZCollectionInterval=120 -XX:ZAllocationSpikeTolerance=5  ./target/spring-boot-api-0.0.1-SNAPSHOT.jar

sh ../nacos/bin/shutdown.sh
sh ../nacos/bin/startup.sh -m standalone


sh ../rocketmq-4.9.3/bin/mqshutdown namesrv
sh ../rocketmq-4.9.3/bin/mqshutdown broker

sleep 3

nohup sh ../rocketmq-4.9.3/bin/mqnamesrv &
nohup sh ../rocketmq-4.9.3/bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true &
