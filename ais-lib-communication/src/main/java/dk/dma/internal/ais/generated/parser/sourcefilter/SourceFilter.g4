grammar SourceFilter; 

prog: expr EOF;

expr:   'id' equalityTest idList      # sourceId
    |   'bs' equalityTest idList      # sourceBasestation
    |   'country' equalityTest idList # sourceCountry
    |   'type' equalityTest ID        # sourceType
    |   'region' equalityTest idList  # sourceRegion
    
    |   'mmsi' equalityTest idList  # mmsi
    |   'messagetype' equalityTest idList  # AisMessagetype
    |   expr op=('&'|'|') expr      # OrAnd
    |   '(' expr ')'                # parens
    ;

equalityTest : '!='|'=';

idList : ID (',' ID)* ;

EQUALS :   '=' ;
NEQUALS :   '!=' ;
WS     :   [ \t]+ -> skip ; // toss out whitespace
AND    :   '&' ;
OR     :   '|' ;
ID     :   [a-zA-Z0-9]+ ;      // match identifiers
