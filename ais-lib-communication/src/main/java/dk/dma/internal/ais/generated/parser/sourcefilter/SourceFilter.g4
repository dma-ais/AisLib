grammar SourceFilter; 

prog: filterExpression EOF;

filterExpression:
        's.id' equalityTest valueList      # sourceId
    |   's.bs' equalityTest valueList      # sourceBasestation
    |   's.country' equalityTest valueList # sourceCountry
    |   's.type' equalityTest valueList    # sourceType
    |   's.region' equalityTest valueList  # sourceRegion
    
    |   'm.id' operator valueSpec          # messageId
    |   'm.mmsi' operator valueSpec        # messageMmsi
    |   'm.sog' comparison valueSpec       # messageSpeedOverGround

    |   'messagetype' equalityTest valueList  # AisMessagetype
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
