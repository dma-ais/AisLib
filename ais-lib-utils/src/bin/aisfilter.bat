@echo OFF
set CLASSPATH=.;lib/*
@echo ON
java dk.dma.ais.utils.filter.AisFilter %*
