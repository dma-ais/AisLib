#!/bin/bash

SCRIPTPATH=`dirname $0`
cd $SCRIPTPATH

stop () {
	# Find pid
	PID=`./getpid.pl aisbus.AisBusLauncher`
	if [ -z $PID ]; then
		echo "AisBus not running"
		exit 1
	fi
	echo "Stopping AisBus"
	kill $PID
    exit 0
}

case "$1" in
start)
	PID=`./getpid.pl aisbus.AisBusLauncher`
	if [ ! -z $PID ]; then
		echo "AisBus already running"
		exit 1
	fi
    echo "Starting AisBus"
    ./aisbus.sh -file aisbus.xml > /dev/null 2>&1 &
    ;;
stop)
    stop
    ;;
restart)
    $0 stop
    sleep 1
    $0 start
    ;;
*)
    echo "Usage: $0 (start|stop|restart|help)"
esac




