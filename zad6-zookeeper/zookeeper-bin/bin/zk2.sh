ZOOCFG=$ZOOCFGDIR$1
ZOOMAIN=org.apache.zookeeper.server.quorum.QuorumPeerMain
java "-Dzookeeper.log.dir=$ZOO_LOG_DIR" "-Dzookeeper.root.logger=$ZOO_LOG4J_PROP" -cp "$CLASSPATH" $ZOOMAIN "$ZOOCFG"

