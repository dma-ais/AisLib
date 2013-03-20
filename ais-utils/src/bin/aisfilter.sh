#!/bin/sh

function readlink() {
  DIR=$(echo "${1%/*}")
  (cd "$DIR" && echo "$(pwd -P)")
}
SCRIPT_DIR="$(readlink ${BASH_SOURCE[0]})"

CLASSPATH="$SCRIPT_DIR:$SCRIPT_DIR/lib/*"
LOG_CONF="file://$SCRIPT_DIR/log4j-filter.xml"

java -Dlog4j.configuration=$LOG_CONF -cp "$CLASSPATH" dk.dma.ais.utils.filter.AisFilter $@
