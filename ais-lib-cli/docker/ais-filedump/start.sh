cd /AisLib
JAR=`ls ais-lib-cli/target/ais-lib-cli*.jar`
java -jar $JAR filedump $SOURCES -directory $DIRECTORY
