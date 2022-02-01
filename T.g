grammar T;

options{backtrack = true;}

@header{

      import java.util.ArrayList;
}

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

start : ID  '=' expr { h.assign($ID.text,$expr.valexpr);}  | unpacking '=' ID 
      ;

expr returns [Object valexpr, Object valexpr2] :  val {$valexpr=$val.value;}| indexing {$valexpr = $indexing.indexVal;} | slicing {$valexpr = $slicing.list;}
      ;

// tail_addID, tail_addInt, tail_concatString, and tail_concatTuple 
// used for addition/concatenation
// can either contain multiple vals or be empty (single assignment)

val returns [Object value, Object val2]   : ID 
                             | Integer  /*System.out.println($Integer.text)*/ tail_addInt {$value = $Integer.text; $value= h.makeInt($Integer.text); $value = $tail_addInt.addIntVal + (Integer)$value ; }
                             | String /*{System.out.println($String.text)*/  tail_concatString {$value = h.removeQuoteString($String.text); $value = $value + $tail_concatString.concatStringVal ;}
                             | tuple /*{System.out.println($tuple.text)*/  tail_concatTuple { if ($tail_concatTuple.flag == 1){$value = $tuple.text;} else{ $value = $tail_concatTuple.concatTupleVal;}}
                             
                             
      ;

//T[i] where i is integer

indexing returns [Object indexVal, int check]  : ID '[' Integer ']' { $check = h.makeInt($Integer.text); if ($check >= 0) {$indexVal = h.getTupleVal(h.makeInt($Integer.text));} else {$indexVal = h.getNegTupleVal(h.makeInt($Integer.text));}}
         ;

// T[i:j] where i and j integers
// T[i: ] where i integer
// T[ : i] where i integer

slicing returns [ArrayList<Object> list] : ID '[' Integer ':' ']' {$list = h.SliceLeft($Integer.text);}
         | ID '[' ':' Integer ']' {$list = h.SliceRight($Integer.text);}
         | ID '[' i1 = Integer ':' i2 = Integer ']' {$list = h.Slice2Ops($i1.text,$i2.text);}
         ;

unpacking : '(' tail_unpacking
          ;

// (x1) = T or (x1,x2,x3....) = T

tail_unpacking : ID {h.assign($ID.text,h.assignIDs());} tail_unpacking | ',' tail_unpacking | ')' 
               ;

// Either adding 1 or more integers, or just assigning single integer 
// (the or represents empty)

tail_addInt returns [int addIntVal] : '+' Integer  i1 = tail_addInt {$addIntVal = h.makeInt($Integer.text)+ $i1.addIntVal;} | {$addIntVal = 0;} //empty
        ;

// Either concatenating 1 or more strings, or just assigning single string
// (the or represents empty)


tail_concatString returns [String concatStringVal]: '+' String string1 = tail_concatString{$concatStringVal = h.removeQuoteString($String.text) + h.removeQuoteString($string1.concatStringVal);} | {$concatStringVal = "";} //empty
              ;

// Either concatenating 1 or more tuples, or just assigning single tuple
// (the or represents empty)

tail_concatTuple returns [Object concatTupleVal, int flag]  : '+' {$flag = 0;} tuple  tuple1 = tail_concatTuple {h.addToList(); $concatTupleVal = h.concatTuples();}  | {$concatTupleVal = ""; $flag = 1;}//empty
              ;

// print(1) printing a val



tuple : '(' tail_tuple
      ;

tail_tuple   : val {h.assignTupleIndex($val.text);} tail_tuple {h.assignNegTupleIndex($val.text);} | ',' tail_tuple  | ')' 
      ;

print returns [Object printed] : 'print' '(' val ')'  {h.checkForPrinting($val.text);}
       | 'print' '(' ID '[' Integer ']' ')' {if ($Integer.text.startsWith("-")){$printed = h.getNegTupleVal(h.makeInt($Integer.text)); h.print((String)$printed);} else {$printed = h.getTupleVal(h.makeInt($Integer.text)); h.print((String)$printed);}}
       | 'print' '(' ID '[' (i3 = Integer)? ':' (i4 = Integer)? ']' ')' {if ($i3.text != null & $i4.text != null) {System.out.println(h.Slice2Ops($i3.text, $i4.text));}
                                                                         else if ($i3.text != null & $i4.text == null) {System.out.println(h.SliceLeft($i3.text));}
                                                                         else if ($i3.text == null & $i4.text != null) {System.out.println(h.SliceRight($i4.text));}
                                                                          
      
      }
      ;

fragment Letter: ('a'..'z'|'A'..'Z');
fragment Digit: '0'..'9';


String : '\'' (Digit|Letter|' ')* '\'';

// - is optional as integer can either be negative or positive (i.e, no sign)

Integer : '-'?(Digit)+;
ID: Letter(Digit|Letter)*;
WS: (' '|'\t'|'\n'|'\r') + {skip();};