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

    |   'm.type' compareTo INT                      # messageShiptype
    |   'm.type' compareTo string                   # messageShiptypeLabel
    |   'm.type' inListOrRange intRange             # messageShiptypeInRange
    |   'm.type' inListOrRange intList              # messageShiptypeInList
    |   'm.type' inListOrRange stringList           # messageShiptypeInLabelList

    |   'm.navstat' compareTo INT                   # messageNavigationalStatus
    |   'm.navstat' compareTo string                # messageNavigationalStatusLabel
    |   'm.navstat' inListOrRange intRange          # messageNavigationalStatusInRange
    |   'm.navstat' inListOrRange intList           # messageNavigationalStatusInList
    |   'm.navstat' inListOrRange stringList        # messageNavigationalStatusInLabelList

    |   'm.name' compareTo string                   # messageName
    |   'm.name' inListOrRange stringList           # messageNameInList

    |   'm.cs' compareTo string                     # messageCallsign
    |   'm.cs' inListOrRange stringList             # messageCallsignInList

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

    |   'messagetype' equalityTest valueList              # AisMessagetype

    |   filterExpression (op=(AND|OR) filterExpression)+  # OrAnd
    |   '(' filterExpression ')'                          # parens
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
WORD    : [a-zA-Z0-9_?]+ ;
STRING : '\'' .*? '\'' ; // '\'' ( ~('\n'|'\r') )*? '\'';
WS      : [ \n\r\t]+ -> skip ; // toss out whitespace
