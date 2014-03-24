grammar SourceFilter; 

prog: filterExpression EOF;

filterExpression:
        's.id' equalityTest valueList               # sourceId
    |   's.bs' equalityTest valueList               # sourceBasestation
    |   's.country' equalityTest valueList          # sourceCountry
    |   's.type' equalityTest valueList             # sourceType
    |   's.region' equalityTest valueList           # sourceRegion
    
    |   'm.id' compareTo INT                        # messageId
    |   'm.id' inListOrRange intRange               # messageIdInRange
    |   'm.id' inListOrRange intList                # messageIdInList
    |   'm.mmsi' compareTo INT                      # messageMmsi
    |   'm.mmsi' inListOrRange intRange             # messageMmsiInRange
    |   'm.mmsi' inListOrRange intList              # messageMmsiInList
    |   'm.imo' compareTo INT                       # messageImo
    |   'm.imo' inListOrRange intRange              # messageImoInRange
    |   'm.imo' inListOrRange intList               # messageImoInList
    |   'm.name' compareTo string                   # messageName
    |   'm.name' inListOrRange stringList           # messageNameInList
    |   'm.sog' compareTo number                    # messageSpeedOverGround
    |   'm.sog' inListOrRange numberRange           # messageSpeedOverGroundInRange
    |   'm.cog' compareTo number                    # messageCourseOverGround
    |   'm.cog' inListOrRange numberRange           # messageCourseOverGroundInRange
    |   'm.lat' compareTo number                    # messageLatitude
    |   'm.lat' inListOrRange numberRange           # messageLatitudeInRange
    |   'm.lon' compareTo number                    # messageLongitude
    |   'm.lon' inListOrRange numberRange           # messageLongitudeInRange
    |   'm.hdg' compareTo number                    # messageTrueHeading
    |   'm.hdg' inListOrRange numberRange           # messageTrueHeadingInRange
    |   'm.draught' compareTo number                # messageDraught
    |   'm.draught' inListOrRange numberRange       # messageDraughtInRange

    |   'messagetype' equalityTest valueList            # AisMessagetype
    |   filterExpression op=('&'|'|') filterExpression  # OrAnd
    |   '(' filterExpression ')'                        # parens
    ;

equalityTest : '!='|'=';
value  : (INT | FLOAT | WORD /* | STRING */);
valueList : '('? value (',' value)* ')'? ;

compareTo : '!='|'='|'>'|'>='|'<='|'<' ;
inListOrRange : '@' | 'in' | 'IN' ;
complies : (compareTo | inListOrRange) ;

intList  : '('? INT (',' INT)* ')'? ;
stringList : '('? string (',' string)* ')'? ;

intRange : '('? INT RANGE INT ')'? ;
numberRange : '('? number RANGE number ')'? ;

number : (INT | FLOAT);
string : (number | WORD | STRING);

AND     : '&' ;
OR      : '|' ;
RANGE   : '..';
INT     : '-'? [0-9]+;
FLOAT   : '-'? [0-9]+ '.' [0-9]+ ;
WORD    : [a-zA-Z0-9?]+ ;
STRING : '\'' .*? '\'' ; // '\'' ( ~('\n'|'\r') )*? '\'';
WS      : [ \n\r\t]+ -> skip ; // toss out whitespace
