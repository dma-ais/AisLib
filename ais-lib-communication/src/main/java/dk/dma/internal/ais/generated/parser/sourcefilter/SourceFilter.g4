grammar SourceFilter; 

prog: expr EOF;

expr:   'id' equalityTest idList      # sourceId
    |   'bs' equalityTest idList      # sourceBasestation
    |   'country' equalityTest idList # sourceCountry
    |   'type' equalityTest ID        # sourceType
    |   'region' equalityTest idList  # sourceRegion
    
    |   'm.mmsi' comparison ID        # comparesToInt
    |   'm.mmsi' inListOrRange idList  # inIntList
    |   'm.mmsi' inListOrRange idRange # inIntRange

    |   'messagetype' equalityTest idList  # AisMessagetype
    |   expr op=('&'|'|') expr      # OrAnd
    |   '(' expr ')'                # parens
    ;

equalityTest : '!='|'=';
comparison : '!='|'='|'>'|'>='|'<='|'<' ;
inListOrRange : '@' | 'in' | 'IN' ;

idList : '('? ID (',' ID)* ')'? ;
idRange : '('? ID '..' ID ')'? ;

EQUALS :   '=' ;
NEQUALS :   '!=' ;
WS     :   [ \t]+ -> skip ; // toss out whitespace
AND    :   '&' ;
OR     :   '|' ;
ID     :   [a-zA-Z0-9]+ ;      // match identifiers
