grammar SourceFilter; 

prog: filterExpression EOF;

filterExpression:
        's.id' equalityTest valueList           # sourceId
    |   's.bs' equalityTest valueList           # sourceBasestation
    |   's.country' equalityTest valueList      # sourceCountry
    |   's.type' equalityTest valueList         # sourceType
    |   's.region' equalityTest valueList       # sourceRegion
    
    |   'm.id' operator valueSpec               # messageId
    |   'm.mmsi' operator valueSpec             # messageMmsi
    |   'm.imo' operator valueSpec              # messageImo
    |   'm.name' operator valueSpec             # messageName
    |   'm.sog' comparison (INT | FLOAT)        # messageSpeedOverGround
    |   'm.cog' comparison (INT | FLOAT)        # messageCourseOverGround
    |   'm.hdg' operator valueSpec              # messageHeading
    |   'm.lon' comparison (INT | FLOAT)        # messageLongitude
    |   'm.lat' comparison (INT | FLOAT)        # messageLatitude
    |   'm.draught' comparison (INT | FLOAT)    # messageDraught

    |   'messagetype' equalityTest valueList    # AisMessagetype
    |   filterExpression op=('&'|'|') filterExpression      # OrAnd
    |   '(' filterExpression ')'                # parens
    ;

equalityTest : '!='|'=';

comparison : '!='|'='|'>'|'>='|'<='|'<' ;
inListOrRange : '@' | 'in' | 'IN' ;
operator : (comparison | inListOrRange) ;

valueList : '('? value (',' value)* ')'? ;
valueRange :'('? value '..' value ')'? ;
value : (INT | FLOAT | SIXBITSTRING );
valueSpec : (value | valueList | valueRange );

EQUALS    :   '=' ;
NEQUALS   :   '!=' ;
WS        :   [ \t]+ -> skip ; // toss out whitespace
AND       :   '&' ;
OR        :   '|' ;
INT       :   '-'? [0-9]+ ;
FLOAT     :   '-'? [0-9]+ '.' [0-9]+ ;
SIXBITCHAR   :  . ; // @ A-Z [ \ ] ^ _ ! " # $ % & ' ( ) * + , - . / 0-9 : ; < = > ?
SIXBITSTRING :  [a-zA-Z0-9]+ ; // (SIXBITCHAR)+ ;
