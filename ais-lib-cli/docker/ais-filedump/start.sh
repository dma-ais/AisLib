JAR=`ls /archive/ais-lib-cli/target/ais-lib-cli*SNAPSHOT.jar`
java -jar $JAR filedump $SOURCES -directory $DIRECTORY
