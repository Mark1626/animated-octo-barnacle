parser grammar PLParser;

@header { package com.mark.grammar; }
options { tokenVocab=PLLexer; }

program
  : block DOT
  ;

block
  : consts? vars? procedure* statement
  ;

consts
  : CONST ID EQUAL NUMBER (COMMA ID EQUAL NUMBER)* COLON
  ;

vars
  : VAR ID (COMMA ID)* COLON
  ;

procedure
  : PROCEDURE ID COLON block COLON
  ;

statement
  : ID ASSIGNMENT expression                    #assignmentStmt
  | CALL ID                                     #callStmt
  | QUESTION ID                                 #questionStmt
  | WRITE expression                            #writeStmt
  | BEGIN statement (COLON statement)* END      #beginStmt
  | IF condition THEN statement                 #ifStmt
  | WHILE condition DO statement                #whileStmt
  ;

condition
  : ODD expression // Not operator
  | expression
    opr = ( EQUAL
          | NOTEQUAL
          | LESSTHAN
          | LESSTHANEQUAL
          | GREATORTHAN
          | GREATORTHANEQUAL
          )
    expression
  ;

expression
  : unary=(PLUS | MINUS)? term (addOpr term)*
  ;

term
  : factor (mulOpr factor)*
  ;

addOpr
  : PLUS
  | MINUS
  ;

mulOpr
  : STAR
  | DIV
  ;

factor
  : ID                              #variableFactor
  | NUMBER                          #numberFactor
  | OBRACKET expression CBRACKET    #nestedFactor
  ;
