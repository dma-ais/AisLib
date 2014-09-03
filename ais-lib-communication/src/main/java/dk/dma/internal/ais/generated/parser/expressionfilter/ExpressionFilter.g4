grammar ExpressionFilter;

filter: filterExpression EOF;

filterExpression:

    //
    // Tokens related to source
    //

        SRC_ID (in|notin) stringList                            # sourceIdIn

    |   SRC_BASESTATION compareTo INT                           # sourceBasestation
    |   SRC_BASESTATION (in|notin) (intRange|intList)           # sourceBasestationIn

    |   SRC_COUNTRY (in|notin) stringList                       # sourceCountryIn

    |   SRC_TYPE (in|notin) stringList                          # sourceTypeIn

    |   SRC_REGION (in|notin) stringList                        # sourceRegionIn

    //
    // Tokens related to message contents
    //
    
    |   MSG_MSGID compareTo INT                                 # messageId
    |   MSG_MSGID (in|notin) (intRange|intList)                 # messageIdIn

    |   MSG_MMSI compareTo INT                                  # messageMmsi
    |   MSG_MMSI (in|notin) (intRange|intList)                  # messageMmsiIn

    |   MSG_IMO compareTo INT                                   # messageImo
    |   MSG_IMO (in|notin) (intRange|intList)                   # messageImoIn

    |   MSG_TYPE compareTo string                               # messageShiptype
    |   MSG_TYPE (in|notin) (intRange|intList|stringList)       # messageShiptypeIn
    
    |   MSG_COUNTRY (in|notin) stringList                       # messageCountryIn    

    |   MSG_NAVSTAT compareTo string                            # messageNavigationalStatus
    |   MSG_NAVSTAT (in|notin) (intRange|intList|stringList)    # messageNavigationalStatusIn

    |   MSG_NAME (compareTo|LIKE) string                        # messageName
    |   MSG_NAME (in|notin) stringList                          # messageNameIn

    |   MSG_CALLSIGN (compareTo|LIKE) string                    # messageCallsign
    |   MSG_CALLSIGN (in|notin) stringList                      # messageCallsignIn

    |   MSG_SPEED compareTo number                              # messageSpeedOverGround
    |   MSG_SPEED (in|notin) numberRange                        # messageSpeedOverGroundIn

    |   MSG_COURSE compareTo number                             # messageCourseOverGround
    |   MSG_COURSE (in|notin) numberRange                       # messageCourseOverGroundIn

    |   MSG_HEADING compareTo INT                               # messageTrueHeading
    |   MSG_HEADING (in|notin) intRange                         # messageTrueHeadingIn

    |   MSG_DRAUGHT compareTo number                            # messageDraught
    |   MSG_DRAUGHT (in|notin) numberRange                      # messageDraughtIn

    |   MSG_LATITUDE compareTo number                           # messageLatitude
    |   MSG_LATITUDE (in|notin) numberRange                     # messageLatitudeIn

    |   MSG_LONGITUDE compareTo number                          # messageLongitude
    |   MSG_LONGITUDE (in|notin) numberRange                    # messageLongitudeIn

    |   MSG_POSITION WITHIN (circle|bbox)                       # messagePositionInside

    |   MSG_TIME_YEAR compareTo INT                             # messageTimeYear
    |   MSG_TIME_MONTH compareTo (INT|string)                   # messageTimeMonth
    |   MSG_TIME_DAY compareTo INT                              # messageTimeDay
    |   MSG_TIME_WEEKDAY compareTo (INT|string)                 # messageTimeWeekday
    |   MSG_TIME_HOUR compareTo INT                             # messageTimeHour
    |   MSG_TIME_MINUTE compareTo INT                           # messageTimeMinute

    |   MSG_TIME_YEAR (in|notin) (intRange|intList)             # messageTimeYearIn
    |   MSG_TIME_MONTH (in|notin) (intRange|intList|stringList) # messageTimeMonthIn
    |   MSG_TIME_DAY (in|notin) (intRange|intList)              # messageTimeDayIn
    |   MSG_TIME_WEEKDAY (in|notin) (intRange|intList|stringList) # messageTimeWeekdayIn
    |   MSG_TIME_HOUR (in|notin) (intRange|intList)             # messageTimeHourIn
    |   MSG_TIME_MINUTE (in|notin) (intRange|intList)           # messageTimeMinuteIn

    //
    // Tokens related to target
    //

    |   TGT_IMO compareTo INT                                   # targetImo
    |   TGT_IMO (in|notin) (intRange|intList)                   # targetImoIn

    |   TGT_TYPE compareTo string                               # targetShiptype
    |   TGT_TYPE (in|notin) (intRange|intList|stringList)       # targetShiptypeIn
    
    |   TGT_COUNTRY (in|notin) stringList                       # targetCountryIn

    |   TGT_NAVSTAT compareTo string                            # targetNavigationalStatus
    |   TGT_NAVSTAT (in|notin) (intRange|intList|stringList)    # targetNavigationalStatusIn

    |   TGT_NAME (compareTo|LIKE) string                        # targetName
    |   TGT_NAME (in|notin) stringList                          # targetNameIn

    |   TGT_CALLSIGN (compareTo|LIKE) string                    # targetCallsign
    |   TGT_CALLSIGN (in|notin) stringList                      # targetCallsignIn

    |   TGT_SPEED compareTo number                              # targetSpeedOverGround
    |   TGT_SPEED (in|notin) numberRange                        # targetSpeedOverGroundIn

    |   TGT_COURSE compareTo number                             # targetCourseOverGround
    |   TGT_COURSE (in|notin) numberRange                       # targetCourseOverGroundIn

    |   TGT_HEADING compareTo INT                               # targetTrueHeading
    |   TGT_HEADING (in|notin) intRange                         # targetTrueHeadingIn

    |   TGT_DRAUGHT compareTo number                            # targetDraught
    |   TGT_DRAUGHT (in|notin) numberRange                      # targetDraughtIn

    |   TGT_LATITUDE compareTo number                           # targetLatitude
    |   TGT_LATITUDE (in|notin) numberRange                     # targetLatitudeIn

    |   TGT_LONGITUDE compareTo number                          # targetLongitude
    |   TGT_LONGITUDE (in|notin) numberRange                    # targetLongitudeIn

    |   TGT_POSITION WITHIN (circle|bbox)                       # targetPositionInside

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
LIKE    : [Ll][Ii][Kk][Ee]|'~' ;
BBOX    : [Bb][Bb][Oo][Xx] ;
CIRCLE  : [Cc][Ii][Rr][Cc][Ll][Ee] ;
WITHIN  : [Ww][Ii][Tt][Hh][Ii][Nn] ;
INT     : '-'? [0-9]+;
FLOAT   : '-'? [0-9]* '.' [0-9]+ ;
STRING  : [a-zA-Z0-9_?\*]+ | '\'' .*? '\'' ;
WS      : [ \n\r\t]+ -> skip ; // toss out whitespace

PREFIX_SOURCE   : 's.';
PREFIX_MESSAGE  : 'm.';
PREFIX_TARGET   : 't.';

SRC_ID          : PREFIX_SOURCE 'id';
SRC_BASESTATION : PREFIX_SOURCE 'bs';
SRC_COUNTRY     : PREFIX_SOURCE 'country';
SRC_TYPE        : PREFIX_SOURCE 'type';
SRC_REGION      : PREFIX_SOURCE 'region';

MSG_MSGID       : PREFIX_MESSAGE 'id';
MSG_MMSI        : PREFIX_MESSAGE 'mmsi';
MSG_IMO         : PREFIX_MESSAGE 'imo';
MSG_TYPE        : PREFIX_MESSAGE 'type';
MSG_COUNTRY     : PREFIX_MESSAGE 'country';
MSG_NAVSTAT     : PREFIX_MESSAGE 'navstat';
MSG_NAME        : PREFIX_MESSAGE 'name';
MSG_CALLSIGN    : PREFIX_MESSAGE 'cs';
MSG_SPEED       : PREFIX_MESSAGE 'sog';
MSG_COURSE      : PREFIX_MESSAGE 'cog';
MSG_HEADING     : PREFIX_MESSAGE 'hdg';
MSG_DRAUGHT     : PREFIX_MESSAGE 'draught';
MSG_LATITUDE    : PREFIX_MESSAGE 'lat';
MSG_LONGITUDE   : PREFIX_MESSAGE 'lon';
MSG_POSITION    : PREFIX_MESSAGE 'pos';
MSG_TIME_YEAR   : PREFIX_MESSAGE 'year';
MSG_TIME_MONTH  : PREFIX_MESSAGE 'month';
MSG_TIME_DAY    : PREFIX_MESSAGE 'dom';
MSG_TIME_WEEKDAY: PREFIX_MESSAGE 'dow';
MSG_TIME_HOUR   : PREFIX_MESSAGE 'hour';
MSG_TIME_MINUTE : PREFIX_MESSAGE 'minute';

TGT_IMO         : PREFIX_TARGET 'imo';
TGT_TYPE        : PREFIX_TARGET 'type';
TGT_COUNTRY     : PREFIX_TARGET 'country';
TGT_NAVSTAT     : PREFIX_TARGET 'navstat';
TGT_NAME        : PREFIX_TARGET 'name';
TGT_CALLSIGN    : PREFIX_TARGET 'cs';
TGT_SPEED       : PREFIX_TARGET 'sog';
TGT_COURSE      : PREFIX_TARGET 'cog';
TGT_HEADING     : PREFIX_TARGET 'hdg';
TGT_DRAUGHT     : PREFIX_TARGET 'draught';
TGT_LATITUDE    : PREFIX_TARGET 'lat';
TGT_LONGITUDE   : PREFIX_TARGET 'lon';
TGT_POSITION    : PREFIX_TARGET 'pos';
