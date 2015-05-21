ais2csv
=======

Introduction
------------
ais2csv is a Windows-based command line tool, which can convert raw NMEA-armoured AIS data into CSV format. 

I.e. it can convert this:

    $PGHP,1,2015,2,1,0,0,0,31,230,,,1,25*22
    !ABVDM,1,1,0,B,3D4cAaU001R3QhVRQ<F003ul2000,0*25
    $PGHP,1,2015,2,1,0,0,0,96,265,,,1,0D*5C
    !ABVDM,1,1,,B,33u=R1Pu000nF1jQ0GiP07kl010@,0*0D
    $PGHP,1,2015,2,1,0,0,0,179,219,,2190074,1,61*2D
    !BSVDM,2,1,9,B,53B8bn000000uGCKCV1>sCC21@PDTiD>2222220N0P<224O6d022QCH5,0*61
    !BSVDM,2,2,9,B,2C853iDiDSQH880,2*32

into CSV format like this:

    timestamp_pretty,timestamp,targetType,mmsi,msgid,posacc,lat,lon,sog,cog,draught,name,dimBow,dimPort,dimStarboard,dimStern,shipTypeCargoTypeCode,shipType,shipCargo,destination,eta,imo,callsign
    01/02/2015 00:00:00,1422748800031,A,273338790,3,1,60.325695,28.732618,0.1,0.0,null,null,null,null,null,null,null,null,null,null,null,null,null
    01/02/2015 00:00:00,1422748800096,A,265511430,3,0,57.681824,11.871668,0.0,0.0,null,null,null,null,null,null,null,null,null,null,null,null,null
    01/02/2015 00:00:00,1422748800179,A,220343000,5,null,null,null,null,null,0.0,S.440 THEILUC,4,2,2,12,30,FISHING,UNDEFINED,HJEM TIL TOESERNE,30/01 06:44 UTC,null,OU4649

Each line in the CSV file is triggered by one AIS-message.

The conversion is _stateful_. This means, that the converter "remembers" (tracks) the most recent information received about each target. Each CSV line 
is filled with all known information - not just the information contained in the AIS message which triggered the CSV line. More specifically: If an AIS 
type 1 message is received, then the corresponding CVS line will contain information from this type 1 message - but also from the most recently received 
AIS message of type 5 (if any).

The user must consider, whether the time gap between two CSV lines is too large to make this consolidated information untrustworthy.

Command line arguments
----------------------
The tool is launched from the command line like this:

    .\ais2csv.exe [-coloumns <colspec>] <inputfile>
    
Where <colspec> defines the list of columns in the CSV file separated by semicolons. If omitted, all supported columns are included in
the CSV file. 

The following columns are supported:

    - timestamp_pretty
    - timestamp
    - targetType
    - mmsi
    - msgid
    - posacc
    - lat
    - lon
    - sog
    - cog
    - draught
    - name
    - dimBow
    - dimPort
    - dimStarboard
    - dimStern
    - shipTypeCargoTypeCode
    - shipType
    - shipCargo
    - destination
    - eta
    - imo
    - callsign

Example invocations
-------------------
Assuming that file sample.ais contains NMEA armoured AIS data, ais2csv can be invoked like this from the Windows Command Shell:

    ais2csv sample.ais
    ais2csv.exe -columns mmsi;timestamp sample.ais
    ais2csv.exe -columns mmsi;timestamp_pretty;lat;lon;sog;cog sample.ais
    
The latter example will produce output like this:

    C:\Users\Thomas\ais2csv>ais2csv.exe -columns mmsi;timestamp_pretty;lat;lon;sog;cog sample.ais
    13:44:05,316 DEBUG [FileConvert] Started processing file sample.ais.txt
    13:44:05,316 DEBUG [FileConvert] Output File: converted\sample.ais.txt\sample.ais.csv
    13:44:05,326 INFO  [AisPacketCSVOutputSink] AisPacketCSVOutputSink created.
    13:44:05,336 INFO  [AisPacketCSVOutputSink] Exporting CSV columns: mmsi;timestamp_pretty;lat;lon;sog;cog
    13:44:05,346 INFO  [AisPacketCSVStatefulOutputSink] AisPacketCSVStatefulOutputSink created.
    C:\Users\Thomas\ais2csv>

After which the generated CSV file looks like this:

    C:\Users\Thomas\ais2csv>type converted\sample.ais\sample.ais.csv
    mmsi,timestamp_pretty,lat,lon,sog,cog
    273338790,01/02/2015 00:00:00,60.325695,28.732618,0.1,0.0
    265511430,01/02/2015 00:00:00,57.681824,11.871668,0.0,0.0
    220343000,01/02/2015 00:00:00,null,null,null,null
    219000028,01/02/2015 00:00:00,null,null,null,null
    219007511,01/02/2015 00:00:00,56.125916,12.309393,0.0,0.0
    265587800,01/02/2015 00:00:00,58.93932,11.169845,0.0,343.8
    ...

Prerequisites
-------------
ais2csv required Java 1.8.0 runtime or newer installed.

Copyright
---------
ais2csv is (C) Copyright 2015 by the Danish Maritime Authority.

Support
-------
Contact Thomas Borg Salling <tbsalling@tbsalling.dk> for technical support.
