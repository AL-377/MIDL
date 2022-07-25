lexer grammar MIDLLex;
fragment LETTER : [a-z]
       | [A-Z]
       ;

fragment DIGIT : [0-9];

fragment UNDERLINE: '_';

fragment INTEGER_TYPE_SUFFIX :   'l' | 'L';  //单引号字符串字面量

fragment EXPONENT:( 'e' | 'E') ( '+' | '-' )? [0-9]+;

fragment FLOAT_TYPE_SUFFIX:  'f' | 'F' | 'd' | 'D';

fragment ESCAPE_SEQUENCE :  '\\'( 'b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\');

INTEGER : ('0' | [1-9] [0-9]*) INTEGER_TYPE_SUFFIX?;    //单引号字符串字面量


FLOATING_PT:    [0-9]+'.'[0-9]*  EXPONENT?  FLOAT_TYPE_SUFFIX?
   				|  '.'[0-9]+  EXPONENT?  FLOAT_TYPE_SUFFIX?
   				|  [0-9]+  EXPONENT  FLOAT_TYPE_SUFFIX?
   				|  [0-9]+  EXPONENT?  FLOAT_TYPE_SUFFIX
   				;


CHAR: '\''(ESCAPE_SEQUENCE |  ~('\\' | '\'') ) '\'';

STRING :    '"' (ESCAPE_SEQUENCE |  ~('\\' | '"') )* '"';

BOOLEAN :  'TRUE' | 'true' | 'FALSE' | 'false';

ID :  LETTER ((UNDERLINE)?( LETTER | DIGIT))* ;  //问号表示非贪婪模式

WS : [ \t\r\n]+ -> skip;    // 忽略空格、Tab、换行以及\r （Windows）