grammar slashdown;

// Text Alignment


alignLeft
    : SLASH 'left' LBRACE TEXT RBRACE
    |
    ;

alignRight: ;
alignCenter: ;
alignJustified: ;



begin: SLASH 'begin' SCOPE? COMMAND;

end: SLASH 'end' COMMAND;


Text
    :
    ;


COMMAND
    : LBRACE NAME RBRACE
    ;

SCOPE
    : LBRACK NAME RBRACK
    ;

NAME
    :   [a-z][a-z0-9]+
    ;

fragment SLASH
    : '\\'
    ;

fragment LBRACE
    : '{'
    ;

fragment RBRACE
    : '}'
    ;

fragment LBRACK
    : '['
    ;

fragment RBRACK
    : ']'
    ;

fragment LPAREN
    : '('
    ;

fragment RPAREN
    : '('
    ;

fragment CITE_START
    : '`'
    ;

fragment CITE_END
    : '´'
    ;


/*WS:
    [ \t\r\n\u000C]+ -> skip
    ;

COMMENT
    : '#' ~[\r\n]* -> skip
    ;*/

// Digits

FloatSequence
    : ('0' | IntegerSequence ) '.' Digit*
    ;

IntegerSequence
    : NonzeroDigit Digit*
    ;

fragment
Digit
    :   [0-9]
    ;

fragment
NonzeroDigit
    :   [1-9]
    ;