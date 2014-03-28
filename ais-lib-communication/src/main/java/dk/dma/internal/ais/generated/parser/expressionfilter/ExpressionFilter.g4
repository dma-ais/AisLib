grammar ExpressionFilter;

filter: filterExpression EOF;

filterExpression:

    //
    // Tokens related to source
    //

        's.id' (in|notin) stringList                            # sourceIdIn

    |   's.bs' compareTo INT                                    # sourceBasestation
    |   's.bs' (in|notin) (intRange|intList)                    # sourceBasestationIn

    |   's.country' (in|notin) stringList                       # sourceCountryIn

    |   's.type' (in|notin) stringList                          # sourceTypeIn

    |   's.region' (in|notin) stringList                        # sourceRegionIn

    //
    // Tokens related to message contents
    //

    |   'm.id' compareTo INT                                    # messageId
    |   'm.id' (in|notin) (intRange|intList)                    # messageIdIn

    |   'm.mmsi' compareTo INT                                  # messageMmsi
    |   'm.mmsi' (in|notin) (intRange|intList)                  # messageMmsiIn

    |   'm.imo' compareTo INT                                   # messageImo
    |   'm.imo' (in|notin) (intRange|intList)                   # messageImoIn

    |   'm.type' compareTo string                               # messageShiptype
    |   'm.type' (in|notin) (intRange|intList|stringList)       # messageShiptypeIn

    |   'm.navstat' compareTo string                            # messageNavigationalStatus
    |   'm.navstat' (in|notin) (intRange|intList|stringList)    # messageNavigationalStatusIn

    |   'm.name' compareTo string                               # messageName
    |   'm.name' (in|notin) stringList                          # messageNameIn

    |   'm.cs' compareTo string                                 # messageCallsign
    |   'm.cs' (in|notin) stringList                            # messageCallsignIn

    |   'm.sog' compareTo number                                # messageSpeedOverGround
    |   'm.sog' (in|notin) numberRange                          # messageSpeedOverGroundIn

    |   'm.cog' compareTo number                                # messageCourseOverGround
    |   'm.cog' (in|notin) numberRange                          # messageCourseOverGroundIn

    |   'm.lat' compareTo number                                # messageLatitude
    |   'm.lat' (in|notin) numberRange                          # messageLatitudeIn

    |   'm.lon' compareTo number                                # messageLongitude
    |   'm.lon' (in|notin) numberRange                          # messageLongitudeIn

    |   'm.pos' WITHIN (circle|bbox)                            # messagePositionInside

    |   'm.hdg' compareTo INT                                   # messageTrueHeading
    |   'm.hdg' (in|notin) intRange                             # messageTrueHeadingIn

    |   'm.draught' compareTo number                            # messageDraught
    |   'm.draught' (in|notin) numberRange                      # messageDraughtIn

    |   ( 'm.year' | 'm.month' | 'm.dom' |
          'm.dow'  | 'm.hour'  | 'm.minute' )
        compareTo (INT|string)                                  # messageTime
    |   ( 'm.year' | 'm.month' | 'm.dom' |
          'm.dow'  | 'm.hour'  | 'm.minute' )
        (in|notin) (intRange|intList|stringList)                # messageTimeIn

    //
    // Tokens related to message contents
    //

    //
    // Other tokens
    //

    |   'messagetype' in stringList                             # AisMessagetype

    |   filterExpression (op=(AND|OR) filterExpression)+        # OrAnd
    |   '(' filterExpression ')'                                # parens
    ;

compareTo : '!='|'='|'>'|'>='|'<='|'<' ;
in : '@'|'in'|'IN'|'=' ;
notin : '!@'|'not in'|'NOT IN'|'!=';

intList  : '('? INT (',' INT)* ')'? ;
stringList : '('? string (',' string)* ')'? ;

intRange : '('? INT RANGE INT ')'? ;
numberRange : '('? number RANGE number ')'? ;

number : INT|FLOAT;
string : number|STRING;

bbox : BBOX '('? number ',' number ',' number ',' number ')'? ;
circle : CIRCLE '('? number ',' number ',' number ')'? ;

AND     : '&' ;
OR      : '|' ;
RANGE   : '..';
BBOX    : [Bb][Bb][Oo][Xx] ;
CIRCLE  : [Cc][Ii][Rr][Cc][Ll][Ee] ;
WITHIN  : [Ww][Ii][Tt][Hh][Ii][Nn] ;
INT     : '-'? [0-9]+;
FLOAT   : '-'? [0-9]* '.' [0-9]+ ;
STRING  : [a-zA-Z0-9_?]+ | '\'' .*? '\'' ;
WS      : [ \n\r\t]+ -> skip ; // toss out whitespace