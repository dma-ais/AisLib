grammar SourceFilter; 

prog: filterExpression EOF;

filterExpression:

    //
    // Tokens related to source
    //

        's.id' (in|notin) stringList        # sourceIdInStringList

    |   's.bs' compareTo INT                # sourceBasestation
    |   's.bs' in intRange                  # sourceBasestationInIntRange
    |   's.bs' in intList                   # sourceBasestationInIntList

    |   's.country' (in|notin) stringList   # sourceCountryInStringList

    |   's.type' (in|notin) stringList      # sourceTypeInStringList

    |   's.region' (in|notin) stringList    # sourceRegionInStringList

    //
    // Tokens related to message contents
    //

    |   'm.id' compareTo INT                # messageId
    |   'm.id' in intRange                  # messageIdInIntRange
    |   'm.id' in intList                   # messageIdInIntList

    |   'm.mmsi' compareTo INT              # messageMmsi
    |   'm.mmsi' in intRange                # messageMmsiInIntRange
    |   'm.mmsi' in intList                 # messageMmsiInIntList

    |   'm.imo' compareTo INT               # messageImo
    |   'm.imo' in intRange                 # messageImoInIntRange
    |   'm.imo' in intList                  # messageImoInIntList

    |   'm.type' compareTo string           # messageShiptype
    |   'm.type' in intRange                # messageShiptypeInIntRange
    |   'm.type' in intList                 # messageShiptypeInIntList
    |   'm.type' in stringList              # messageShiptypeInStringList

    |   'm.navstat' compareTo string        # messageNavigationalStatus
    |   'm.navstat' in intRange             # messageNavigationalStatusInIntRange
    |   'm.navstat' in intList              # messageNavigationalStatusInIntList
    |   'm.navstat' in stringList           # messageNavigationalStatusInStringList

    |   'm.name' compareTo string           # messageName
    |   'm.name' in stringList              # messageNameInStringList

    |   'm.cs' compareTo string             # messageCallsign
    |   'm.cs' in stringList                # messageCallsignInStringList

    |   'm.sog' compareTo number            # messageSpeedOverGround
    |   'm.sog' in numberRange              # messageSpeedOverGroundInNumberRange

    |   'm.cog' compareTo number            # messageCourseOverGround
    |   'm.cog' in numberRange              # messageCourseOverGroundInNumberRange

    |   'm.lat' compareTo number            # messageLatitude
    |   'm.lat' in numberRange              # messageLatitudeInNumberRange

    |   'm.lon' compareTo number            # messageLongitude
    |   'm.lon' in numberRange              # messageLongitudeInNumberRange

    |   'm.hdg' compareTo number            # messageTrueHeading
    |   'm.hdg' in numberRange              # messageTrueHeadingInNumberRange

    |   'm.draught' compareTo number        # messageDraught
    |   'm.draught' in numberRange          # messageDraughtInNumberRange

    //
    // Tokens related to message contents
    //

    //
    // Other tokens
    //

    |   'messagetype' in stringList     # AisMessagetype

    |   filterExpression (op=(AND|OR) filterExpression)+  # OrAnd
    |   '(' filterExpression ')'                          # parens
    ;

compareTo : '!='|'='|'>'|'>='|'<='|'<' ;

in : '@'|'in'|'IN'|'=' ;
notin : '!@'|'not in'|'NOT IN'|'!=';

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
