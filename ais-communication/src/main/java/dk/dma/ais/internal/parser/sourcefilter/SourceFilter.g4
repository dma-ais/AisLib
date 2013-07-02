grammar SourceFilter; 

prog: expr EOF;

expr:   'id' EQUALS idList      # sourceId
    |   'bs' EQUALS idList      # sourceBasestation
    |   'country' EQUALS idList # sourceCountry
    |   'type' EQUALS ID        # sourceType
    |   'region' EQUALS idList  # sourceRegion
    |   expr op=('&'|'|') expr  # OrAnd
    |   '(' expr ')'            # parens
    ;


idList : ID (',' ID)* ;

EQUALS :   '=' ;
WS     :   [ \t]+ -> skip ; // toss out whitespace
AND    :   '&' ;
OR     :   '|' ;
ID     :   [a-zA-Z0-9]+ ;      // match identifiers
