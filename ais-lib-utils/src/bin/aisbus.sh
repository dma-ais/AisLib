#!/bin/sh

SCRIPTPATH=`dirname $0`
cd $SCRIPTPATH

CLASSPATH=".:lib/*"
LOG_CONF="file:log4j-aisbus.xml"

java -Dlog4j.configuration=$LOG_CONF -cp "$CLASSPATH" dk.dma.ais.utils.aisbus.AisBusLauncher $@
