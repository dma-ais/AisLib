grammar SourceFilter; 

prog: expr EOF;

expr:   'id' ('!='|'=') idList      # sourceId
    |   'bs' ('!='|'=') idList      # sourceBasestation
    |   'country' ('!='|'=') idList # sourceCountry
    |   'type' ('!='|'=') ID        # sourceType
    |   'region' ('!='|'=') idList  # sourceRegion
    |   expr op=('&'|'|') expr  # OrAnd
    |   '(' expr ')'            # parens
    ;


idList : ID (',' ID)* ;

EQUALS :   '=' ;
NEQUALS :   '!=' ;
WS     :   [ \t]+ -> skip ; // toss out whitespace
AND    :   '&' ;
OR     :   '|' ;
ID     :   [a-zA-Z0-9]+ ;      // match identifiers
