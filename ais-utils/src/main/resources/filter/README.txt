Usage
=====

Change directory where SH/BAT file is located

Run BAT or SH file to get help on usage

Usage: AisFilter <-t|-f> <filename/host1:port1,...,hostN,portN> [-b b1,...,bN] [-c c1,...,cN] [-T seconds] [-d] [-C seconds]
        -t TCP round robin connection to host1:port1 ... hostN:portN
        -f Read from file filename
        -C TCP read timeout in seconds, default none
        -b b1,...,bN comma separated list of base station MMSI's
        -c c1,...,cN comma separated list of country codes in two letter ISO 3166
        -T time to run in seconds (default 1 hour)
        -d Dump message content (default false)
        -S Start time in format yyyy-MM-dd-HH:mm (Local time)
        -E End time in format yyyy-MM-dd-HH:mm (Local time)

Examples
========

* Dump messages originating from base stations: 2190069, 2190073, 2190072 or 2190074.
* TCP host:port: ais43.sealan.dk:65262
* Do it for 24 hours
* Do not dump the parsed messages
* Dump to file: frejlev_laesoe_hirsthals_skagen.txt

filter.bat -t ais43.sealan.dk:65262 -b 2190069,2190073,2190072,2190074 -T 86400 > frejlev_laesoe_hirsthals_skagen.txt

* Dump messages from TCP host:port: ais43.sealan.dk:4712
* Only messages originating from Faeroe Islands (FO) and Iceland (IS)
* Do it for one minute
* Dump message content
* Dump to stdout

filter.bat -t ais43.sealan.dk:4712 -c FO,IS -d -T 60

