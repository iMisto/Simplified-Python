grammar T;

options{backtrack = true;}

@members {
        ActionRoutines h = new ActionRoutines();
 }
// 
prog : (assignment)+
     ;

assignment : start | print | Comments
           ;

Comments : ('#')~('\n'|'\r')* {skip();} ;
        
//(x1,x2,x3) = T unpack into vars

start  : ID  '=' expr | unpacking '=' ID 
      ;

expr :  val | indexing  | slicing
      ;

// tail_addID, tail_addInt, tail_concatString, and tail_concatTuple 
// used for addition/concatenation
// can either contain multiple vals or be empty (single assignment)

val   : ID 
      | Integer  tail_addInt 
      | String  tail_concatString 
      | tuple  tail_concatTuple 
                             
                             
      ;

//T[i] where i is integer

indexing : ID '[' Integer ']' 
         ;

// T[i:j] where i and j integers
// T[i: ] where i integer
// T[ : i] where i integer

slicing  : ID '[' Integer ':' ']' 
         | ID '[' ':' Integer ']' 
         | ID '[' Integer ':' Integer ']'
         ;

unpacking : '(' tail_unpacking
          ;

// (x1) = T or (x1,x2,x3....) = T

tail_unpacking : ID tail_unpacking | ',' tail_unpacking | ')' 
               ;

// Either adding 1 or more integers, or just assigning single integer 
// (the or represents empty)

tail_addInt : '+' Integer  i1 = tail_addInt  |  //empty
        ;

// Either concatenating 1 or more strings, or just assigning single string
// (the or represents empty)

// Strings are not being ordered properly in concatenation and I couldn't get rid of the quotes
tail_concatString : '+' String string1 = tail_concatString |  //empty
              ;

// Either concatenating 1 or more tuples, or just assigning single tuple
// (the or represents empty)

tail_concatTuple   : '+' tuple tail_concatTuple | //empty
              ;

// print(1) printing a val



tuple : '(' tail_tuple
      ;

tail_tuple   : val tail_tuple  | ',' tail_tuple  | ')'
      ;

print : 'print' '(' val ')'  
       | 'print' '(' ID '[' Integer ']' ')'
      ;

fragment Letter: ('a'..'z'|'A'..'Z');
fragment Digit: '0'..'9';


String : '\'' (Digit|Letter|' ')* '\'';

// - is optional as integer can either be negative or positive (i.e, no sign)

Integer : '-'?(Digit)+;
ID: Letter(Digit|Letter)*;
WS: (' '|'\t'|'\n'|'\r') + {skip();};