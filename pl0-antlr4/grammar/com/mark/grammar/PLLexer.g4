lexer grammar PLLexer;

@header { package com.mark.grammar; }

CONST:            'const' | 'CONST';
VAR:              'var' | 'VAR';
PROCEDURE:        'procedure' | 'PROCEDURE';
CALL:             'call' | 'CALL';
BEGIN:            'begin' | 'BEGIN';
IF:               'if' | 'IF';
THEN:             'then' | 'THEN';
WHILE:            'while' | 'WHILE';
COLON:            ';';
DO:               'do' | 'DO';
ODD:              'odd' | 'ODD';
ASSIGNMENT:       ':=';
QUESTION:         '?';
WRITE:            'write' | 'WRITE' | '!';
END:              'end' | 'END';
OBRACKET:         '(';
CBRACKET:         ')';
EQUAL:            '=';
NOTEQUAL:         '#';
LESSTHAN:         '<';
LESSTHANEQUAL:    '<=';
GREATORTHAN:      '>';
GREATORTHANEQUAL: '>=';
PLUS:             '+';
MINUS:            '-';
COMMA:            ',';
STAR:             '*';
DIV:              '/';
DOT:              '.';
ID:               [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER:           [0-9]+;
WS:               [\n\t\r ] -> skip;
