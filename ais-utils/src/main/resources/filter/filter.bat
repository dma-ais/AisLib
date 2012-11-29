@echo OFF
set CLASSPATH=.;../lib/*;../../extlib/*
@echo ON
java dk.frv.ais.utils.filter.AisFilter %*
