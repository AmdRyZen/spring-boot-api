# -XX:+PrintGCDetails
#/Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home/bin/java -jar -XX:+UseZGC -XX:+UseNUMA -Xms8G -Xmx8G -XX:ReservedCodeCacheSize=128m -XX:InitialCodeCacheSize=128m -XX:ConcGCThreads=2 -XX:ParallelGCThreads=6 -XX:ZCollectionInterval=120 -XX:ZAllocationSpikeTolerance=5  ./target/spring-boot-api-0.0.1-SNAPSHOT.jar

sudo sh ../nacos/bin/shutdown.sh
sudo sh ../nacos/bin/startup.sh -m standalone

sleep 3

sh ../rocketmq-all-5.1.4-bin-release/bin/mqshutdown namesrv
sh ../rocketmq-all-5.1.4-bin-release/bin/mqshutdown broker

sleep 3

nohup sh ../rocketmq-all-5.1.4-bin-release/bin/mqnamesrv &
nohup sh ../rocketmq-all-5.1.4-bin-release/bin/mqbroker -n localhost:9876 autoCreateTopicEnable=true &

sleep 1
#nohup /Library/Java/JavaVirtualMachines/jdk-20.jdk/Contents/Home/bin/java -jar ../zipkin/zipkin.jar &
nohup java -jar ../zipkin/zipkin.jar &


sleep 1
#nohup /Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home/bin/java -server -XX:+UseParallelGC -XX:+UseNUMA -Xms2G -Xmx2G  -XX:-UseBiasedLocking -XX:+UseStringDeduplication -jar ../spring-boot-api-rpc/target/spring-boot-api-rpc-0.0.1-SNAPSHOT.jar &
#nohup /Library/Java/JavaVirtualMachines/jdk-19.jdk/Contents/Home/bin/java -server -XX:+UseParallelGC -XX:+UseNUMA -Xms2G -Xmx2G  -XX:-UseBiasedLocking -XX:+UseStringDeduplication -jar ../spring-cloud-gateway/target/spring-cloud-gateway-0.0.1-SNAPSHOT.jar &

