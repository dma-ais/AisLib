grammar SourceFilter; 

prog: expr;

expr:   'id' EQUALS idList      # sourceId
    |   'bs' EQUALS intList     # sourceBasestation
    |   'country' EQUALS idList # sourceCountry
    |   'type' EQUALS ID        # sourceType
    |   'region' EQUALS idList  # sourceRegion
    |   expr op=('&'|'|') expr  # OrAnd
    |   '(' expr ')'            # parens
    ;


intList : INT (',' INT)* ;
idList : ID (',' ID)* ;

EQUALS :   '=' ;
WS     :   [ \t]+ -> skip ; // toss out whitespace
AND    :   '&' ;
OR     :   '|' ;
INT    :   [0-9]+ ;         // match integers
ID     :   [a-zA-Z][a-zA-Z0-9]+ ;      // match identifiers
