#!/bin/sh

if [ -z "$ANTLR_HOME" ] ; then
    echo "ANTLR_HOME not set."
    exit;
fi

java -jar ${ANTLR_HOME}/antlr-4.2-complete.jar -no-listener -visitor -package dk.dma.internal.ais.generated.parser.expressionfilter ExpressionFilter.g4
