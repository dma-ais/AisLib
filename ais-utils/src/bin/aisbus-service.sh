#!/bin/bash

SCRIPTPATH=`dirname $0`
cd $SCRIPTPATH

if [ -z $2 ]
then
	CONFFILE=aisbus.xml
else
	CONFFILE=$2
fi

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
    ./aisbus.sh -file $CONFFILE > /dev/null 2>&1 &
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
    echo "Usage: $0 (start|stop|restart|help) [conffile]"
esac




