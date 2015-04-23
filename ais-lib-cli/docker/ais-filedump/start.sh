#!/usr/bin/env bash
JAR=`ls /archive/ais-lib-cli/target/ais-lib-cli*SNAPSHOT.jar`
echo "Running: "
echo "java -jar $JAR filedump $SOURCES -directory $DIRECTORY"
java -jar $JAR filedump $SOURCES -directory $DIRECTORY
