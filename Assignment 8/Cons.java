/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 30 Oct 08; 07 Apr 09
 *          10 Apr 09; 02 Aug 10; 06 Aug 10; 07 Apr 14; 28 Oct 15
 */

/*  Copyright (C) 2015 Gordon Shaw Novak Jr.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.StringTokenizer;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons
{
    // instance variables
    private Object car;
    private Cons cdr;
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }
// safe car, returns null if lst is null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }
// safe cdr, returns null if lst is null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }
    public static Object second (Cons x) { return first(rest(x)); }
    public static Object third (Cons x) { return first(rest(rest(x))); }
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }
    // access functions for expression representation
    public static Object op  (Cons x) { return first(x); }
    public static Object lhs (Cons x) { return first(rest(x)); }
    public static Object rhs (Cons x) { return first(rest(rest(x))); }
    public static boolean numberp (Object x)
       { return ( (x != null) &&
                  (x instanceof Integer || x instanceof Double) ); }
    public static boolean integerp (Object x)
       { return ( (x != null) && (x instanceof Integer ) ); }
    public static boolean floatp (Object x)
       { return ( (x != null) && (x instanceof Double ) ); }
    public static boolean stringp (Object x)
       { return ( (x != null) && (x instanceof String ) ); }

    public boolean equals(Object other) { return equal(this,other); }

    // tree equality
public static boolean equal(Object tree, Object other) {
    if ( tree == other ) return true;
    if ( consp(tree) )
        return ( consp(other) &&
                 equal(first((Cons) tree), first((Cons) other)) &&
                 equal(rest((Cons) tree), rest((Cons) other)) );
    return eql(tree, other); }

    // simple equality test
public static boolean eql(Object tree, Object other) {
    return ( (tree == other) ||
             ( (tree != null) && (other != null) &&
               tree.equals(other) ) ); }

    // convert a list to a string for printing
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    // iterative version of length
public static int length (Cons lst) {
  int n = 0;
  while ( lst != null ) {
    n++;
    lst = rest(lst); }
  return n; }

// member returns null if requested item not found
public static Cons member (Object item, Cons lst) {
  if ( lst == null )
     return null;
   else if ( item.equals(first(lst)) )
           return lst;
         else return member(item, rest(lst)); }

public static Cons union (Cons x, Cons y) {
  if ( x == null ) return y;
  if ( member(first(x), y) != null )
       return union(rest(x), y);
  else return cons(first(x), union(rest(x), y)); }

public static boolean subsetp (Cons x, Cons y) {
    return ( (x == null) ? true
             : ( ( member(first(x), y) != null ) &&
                 subsetp(rest(x), y) ) ); }

public static boolean setEqual (Cons x, Cons y) {
    return ( subsetp(x, y) && subsetp(y, x) ); }

    // combine two lists: (append '(a b) '(c d e))  =  (a b c d e)
public static Cons append (Cons x, Cons y) {
  if (x == null)
     return y;
   else return cons(first(x),
                    append(rest(x), y)); }

    // look up key in an association list
    // (assoc 'two '((one 1) (two 2) (three 3)))  =  (two 2)
public static Cons assoc(Object key, Cons lst) {
  if ( lst == null )
     return null;
  else if ( key.equals(first((Cons) first(lst))) )
      return ((Cons) first(lst));
          else return assoc(key, rest(lst)); }

    public static int square(int x) { return x*x; }
    public static int pow (int x, int n) {
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

public static Object copy_tree(Object tree) {
    if ( consp(tree) )
        return cons(copy_tree(first((Cons) tree)),
                    (Cons) copy_tree(rest((Cons) tree)));
    return tree; }

public static Object subst(Object gnew, String old, Object tree) {
    if ( consp(tree) )
        return cons(subst(gnew, old, first((Cons) tree)),
                    (Cons) subst(gnew, old, rest((Cons) tree)));
    return (old.equals(tree)) ? gnew : tree; }

    // This sublis uses a ( (var value) ...) alist
public static Object sublis(Cons alist, Object tree) {
    if ( consp(tree) )
        return cons(sublis(alist, first((Cons) tree)),
                    (Cons) sublis(alist, rest((Cons) tree)));
    if ( tree == null ) return null;
    Cons pair = assoc(tree, alist);
    return ( pair == null ) ? tree : second(pair); }

public static Cons dummysub = list(list("t", "t"));

public static Cons match(Object pattern, Object input) {
    return matchb(pattern, input, dummysub); }

public static Cons matchb(Object pattern, Object input, Cons bindings) {
    if ( bindings == null ) return null;
    if ( consp(pattern) )
        if ( consp(input) )
            return matchb( rest( (Cons) pattern),
                           rest( (Cons) input),
                           matchb( first( (Cons) pattern),
                                   first( (Cons) input),
                                   bindings) );
        else return null;
    if ( varp(pattern) ) {
        Cons binding = assoc(pattern, bindings);
        if ( binding != null )
            if ( equal(input, second(binding)) )
                return bindings;
            else return null;
        else return cons(list(pattern, input), bindings); }
    if ( eql(pattern, input) )
        return bindings;
    return null; }

public static Object reader(String str) {
    return readerb(new StringTokenizer(str, " \t\n\r\f()'", true)); }

public static Object readerb( StringTokenizer st ) {
    if ( st.hasMoreTokens() ) {
        String nexttok = st.nextToken();
        if ( nexttok.charAt(0) == ' ' ||
             nexttok.charAt(0) == '\t' ||
             nexttok.charAt(0) == '\n' ||
             nexttok.charAt(0) == '\r' ||
             nexttok.charAt(0) == '\f' )
            return readerb(st);
        if ( nexttok.charAt(0) == '(' )
            return readerlist(st);
        if ( nexttok.charAt(0) == '\'' )
            return list("QUOTE", readerb(st));
        return readtoken(nexttok); }
    return null; }

    public static Object readtoken( String tok ) {
        if ( (tok.charAt(0) >= '0' && tok.charAt(0) <= '9') ||
             ((tok.length() > 1) &&
              (tok.charAt(0) == '+' || tok.charAt(0) == '-' ||
               tok.charAt(0) == '.') &&
              (tok.charAt(1) >= '0' && tok.charAt(1) <= '9') ) ||
             ((tok.length() > 2) &&
              (tok.charAt(0) == '+' || tok.charAt(0) == '-') &&
              (tok.charAt(1) == '.') &&
              (tok.charAt(2) >= '0' && tok.charAt(2) <= '9') )  ) {
            boolean dot = false;
            for ( int i = 0; i < tok.length(); i++ )
                if ( tok.charAt(i) == '.' ) dot = true;
            if ( dot )
                return Double.parseDouble(tok);
            else return Integer.parseInt(tok); }
        return tok; }

public static Cons readerlist( StringTokenizer st ) {
    if ( st.hasMoreTokens() ) {
        String nexttok = st.nextToken();
        if ( nexttok.charAt(0) == ' ' ||
             nexttok.charAt(0) == '\t' ||
             nexttok.charAt(0) == '\n' ||
             nexttok.charAt(0) == '\r' ||
             nexttok.charAt(0) == '\f' )
            return readerlist(st);
        if ( nexttok.charAt(0) == ')' )
            return null;
        if ( nexttok.charAt(0) == '(' ) {
            Cons temp = readerlist(st);
            return cons(temp, readerlist(st)); }
        if ( nexttok.charAt(0) == '\'' ) {
            Cons temp = list("QUOTE", readerb(st));
            return cons(temp, readerlist(st)); }
        return cons( readtoken(nexttok),
                     readerlist(st) ); }
    return null; }

    // read a list of strings, producing a list of results.
public static Cons readlist( Cons lst ) {
    if ( lst == null )
        return null;
    return cons( reader( (String) first(lst) ),
                 readlist( rest(lst) ) ); }

public static Object transform(Cons patpair, Cons input) {
    Cons bindings = match(first(patpair), input);
    if ( bindings == null ) return null;
    return sublis(bindings, second(patpair)); }

    // Transform a list of arguments.  If no change, returns original.
public static Cons transformlst(Cons allpats, Cons input) {
    if ( input == null ) return null;
    Cons restt = transformlst(allpats, rest(input));
    Object thist = transformr(allpats, first(input));
    if ( thist == first(input) && restt == rest(input) )
        return input;
    return cons(thist, restt); }

    // Transform a single item.  If no change, returns original.
public static Object transformr(Cons allpats, Object input) {
    //    System.out.println("transformr:  " + input.toString());
    if ( consp(input) ) {
        Cons listt = transformlst(allpats, (Cons) input);
        //       System.out.println("   lst =  " + listt.toString());
        return transformrb(allpats, allpats,
                           transformlst(allpats, listt)); }
    Object res = transformrb(allpats, allpats, input);
    //    System.out.println("   result =  " + res.toString());
    return res; }

    // Transform a single item.  If no change, returns original.
public static Object transformrb(Cons pats, Cons allpats, Object input) {
    if ( pats == null ) return input;
    if ( input == null ) return null;
    Cons bindings = match(first((Cons)first(pats)), input);
    if ( bindings == null ) return transformrb(rest(pats), allpats, input);
    return sublis(bindings, second((Cons)first(pats))); }

    // Transform a single item repeatedly, until fixpoint (no change).
public static Object transformfp(Cons allpats, Object input) {
    //    System.out.println("transformfp: " + input.toString());
    Object trans = transformr(allpats, input);
    if ( trans == input ) return input;
    //    System.out.println("    result = " + trans.toString());
    return transformfp(allpats, trans); }          // potential loop!

public static boolean varp(Object x) {
    return ( stringp(x) &&
             ( ((String) x).charAt(0) == '?' ) ); }

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
public static Cons opposites = 
    list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
          list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
          list( "log", "exp"), list( "exp", "log") );

public static String opposite(String op) {
    Cons pair = assoc(op, opposites);
    if ( pair != null ) return (String) second(pair);
    return "error"; }

public static void javaprint(Object item, int tabs) {
    if ( item == null ) System.out.print("null");
    else if ( consp(item) ) javaprintlist((Cons) item, tabs);
    else if ( stringp(item) )
        if ( item.equals("zlparen") ) System.out.print("(");
        else if ( item.equals("zrparen") ) System.out.print(")");
        else if ( item.equals("zspace") ) System.out.print(" ");
        else if ( item.equals("znothing") ) ;
        else if ( item.equals("ztab") ) System.out.print("\t");
        else if ( item.equals("zreturn") ) System.out.println();
        else System.out.print((String)item);
    else System.out.print(item.toString()); }

public static void javaprintlist(Cons lst, int tabs) {
    if ( lst != null ) {
        if ( stringp(first(lst)) )
            if ( ((String)first(lst)).equals("ztab" ) ) tabs++;
            else if ( ((String)first(lst)).equals("zuntab" ) ) tabs--;
            else if ( ((String)first(lst)).equals("zreturn" ) ) {
                System.out.println();
                for (int i = 0; i < tabs; i++) System.out.print("\t"); }
            else javaprint(first(lst), tabs);
        else javaprint(first(lst), tabs);
        javaprintlist(rest(lst), tabs); } }
           

public static Cons reverse (Cons lst)
{
	Cons answer = null;
	for (; lst!=null; lst = rest(lst))
		answer = cons(first(lst), answer);
	return answer;
}

    // ****** your code starts here ******

    // your program from the previous assignments
public static Cons solve (Cons e, String v){
	Cons list =  solveHelper(e,v);
	if (list == null)
		return solveHelper(list("=", rhs(e), lhs(e)), v);
	return list;
}

public static Cons solveHelper(Cons e, String v) 
{	
	if (v.equals(lhs(e)))
		return e;
	if (v.equals(rhs(e)))
		return list("=", v, lhs(e));
	if (consp(rhs(e)))
	{
		String operator = (String)first((assoc(op((Cons)rhs(e)), opposites)));
		String invOperator = (String)first(rest(assoc(op((Cons)rhs(e)), opposites)));

		if (rhs((Cons)rhs(e)) == null)
		{
			switch(operator)
			{
				case "-":
					return solveHelper(list("=", list(operator, lhs(e)), lhs((Cons)rhs(e))), v);
				case "sqrt":
					return solveHelper(list("=", list(invOperator, lhs(e), 2), lhs((Cons)rhs(e))), v);
				default:
					return solveHelper(list("=", list(invOperator, lhs(e)), lhs((Cons)rhs(e))), v);
			}
		}
		else
		{
			switch (operator)
			{
				case "expt":
					if (Double.parseDouble(rhs((Cons)rhs(e)).toString()) == 2) {
						return solveHelper(list("=", list(invOperator, lhs(e)), lhs((Cons)rhs(e))), v);
					}
					break;
				default:
					Cons eq = solveHelper(list("=", list(invOperator, lhs(e), rhs((Cons)rhs(e))), lhs((Cons)rhs(e))), v);
						if (eq == null)
						{
							switch (operator)
							{
								case "/":
									eq = solveHelper(list("=", list(operator, lhs((Cons)rhs(e)), lhs(e)), rhs((Cons)rhs(e))), v);
									break;
								case "-":
									eq =  solveHelper(list("=", list(operator, lhs((Cons)rhs(e)), lhs(e)), rhs((Cons)rhs(e))), v);
									break;
								default:
									eq = solveHelper(list("=", list(invOperator, lhs(e), lhs((Cons)rhs(e))), rhs((Cons)rhs(e))), v);
									break;
							}
						}
						return eq;
			}
		}
	}
	return null ;
}

    public static Cons vars (Object expr) 
    {
    	Cons variables = null;
    	if (consp(expr))
    		variables = union(vars(lhs((Cons)expr)),vars(rhs((Cons)expr)));
    	else if (stringp(expr))
    		variables = cons(expr,variables);
    	return variables;
    }

    public static Double eval (Object tree, Cons bindings) 
    {
    	Double value = 0.0;
    	if(consp(tree))
    	{
    		String operator = (String) op((Cons)tree);
    		Double left = eval(lhs((Cons)tree),bindings);
    		Double right = eval(rhs((Cons)tree),bindings);
    		
    		switch(operator)
    		{
    			case "+":
    				value = left + right;
    				break;
    			case "-":
    				if (rhs((Cons) tree) == null)
    					value = left * -1;
    				else
    					value = left - right;
    				break;
    			case "*":
    				value = left * right;
    				break;
    			case "/":
    				value = left / right;
    				break;
    			case "expt":
    				value = Math.pow(left,right);
    				break;
    			case "sqrt":
    				value = Math.sqrt(left);
    				break;
    			case "exp":
    				value = Math.exp(left);
    				break;
    			case "log":
    				value = Math.log(left);
    				break;
    			default:
    				break;
    		}
    	}
    	else if (tree != null)
    	{
    		Cons assocVar = assoc(tree,bindings);
    		
    		if (assocVar == null)
    			value = Double.parseDouble(tree.toString());
    		else
    			value = (Double) first(rest(assocVar));
    	}
    	return value;

    }

    // new for this assignment

public static Double solveqns(Cons eqns, Cons vals, String v) 
{
	if (assoc(v,vals) != null)
		return (Double) (first(rest(assoc(v,vals))));
	
	Cons allVars = null;
	Cons tempValues = vals;
	
	while (tempValues != null)
	{
		if (consp(vals))
			allVars = cons(first((Cons)first(tempValues)),allVars);
		tempValues = rest(tempValues);
	}
	
	allVars = reverse(allVars);
	Cons listEq = vars(first(eqns));
	Cons nEq = null;
	while (listEq!=null)
	{
		if (member(first(listEq),allVars) == null)
			nEq = cons(first(listEq),nEq);
		
		listEq = rest(listEq);
	}
	
	if (length(nEq) == 1)
	{
		Double nValue = eval(rhs(solve((Cons)first(eqns),(String)first(nEq))), vals);
		vals = cons(list((String)first(nEq),nValue), vals);
		return solveqns(eqns,vals,v);
	}
	else
		return solveqns(rest(eqns),vals,v);
	
}

    // Question 2 of Assignment 8
    // Code substitutions to be done in 'binaryfn' below
    // ((?function ...) (?combine ...) (?baseanswer ...))
    //    (defun ?function (tree)
    //       (if (consp tree)
    //           (?combine (?function (first tree))
    //                     (?function (rest tree)))
    //           ?baseanswer))
    public static Cons substitutions = readlist( list(
       "( (?function addnums) (?combine +) (?baseanswer (if (numberp tree) tree 0)))",

       // add to the following
       "( (?function countstrings) (?combine +) (?baseanswer (if (stringp tree) tree 0)))",
       "( (?function copytree) (?combine cons) (?baseanswer (tree)))",
       "( (?function mintree) (?combine min) (?baseanswer (if (numberp tree) nil)))",
       "( (?function conses) (?combine add1) (?baseanswer (if (consp tree) 1 0"
       ));

    public static Cons optpats = readlist( list(
    		
       //simplifying for derivative tests
       "( (- 2 1) 1)",
       "( (- 3 1) 2)",  
       "( (/ (* 3 (expt (+ 1 x) 2)) (expt (+ 1 x) 3)) (/ 3 (expt (+ 1 x) 1))) ",
       // addition op		
       "( (+ ?x 0)   ?x)",
       "( (+ 0 ?x)   ?x)",
       // subtraction op
       "( (- ?x 0)   ?x)",
       "( (- 0 ?x) (- ?x))",
       "( (- ?x ?x) (0)",
       "( (- (- ?x)) ?x)",
       "( (- (- ?x ?y)) (- ?y ?x))",
       // multiplication op
       "( (* ?x 1) ?x)",
       "( (* 1 ?x) ?x)",
       "( (* ?x 0) 0)",
       "( (* 0 ?x) 0)",
       "( (* ?x ?x) (expt ?x 2))",
       // exp op
       "( (exp 0) 1)",
	   "( (exp (log ?x)) ?x)",
	   // log op
	   "( (log 1) 0)",
	   "( (log (exp ?x)) ?x)",
       // expt op
       "( (expt ?x 0) 1)",
       "( (expt ?x (0)) 1)",
	   "( (expt 0 ?x) 0)",
	   "( (expt ?x 1) ?x)",
	   "( (expt 1 ?x) 1)",
	   // sqrt op
	   "( (sqrt 1) 1)",
	   "( (sqrt 0) 0)",
	   "( (sqrt (expt ?x 2)) ?x)"
       ));

    public static Cons derivpats = readlist( list(
       // constant 
       "( (deriv ?x ?x)   1)",
       // rules
       "( (deriv (+ ?u ?v) ?x)  (+ (deriv ?u ?x) (deriv ?v ?x)))", // sum
       "( (deriv (- ?u ?v) ?x)  (- (deriv ?u ?x) (deriv ?v ?x)))", // difference
       "( (deriv (* ?u ?v) ?x)  (+ (* ?u (deriv ?v ?x)) (* ?v (deriv ?u ?x)))))", // multiplication
       "( (deriv (/ ?u ?v) ?x)  (/ (- (* ?v (deriv ?u ?x)) (* ?u (deriv ?v ?x))) (expt ?v 2)))", // quotient
       "( (deriv (/ 1 ?u ) ?x)  (/ (- (deriv ?u ?x) (expt ?u 2) ", // reciprocal
       "( (deriv (log ?u) ?x)   (/ (deriv ?u ?x) ?u))", // log
       "( (deriv (exp ?u) ?x)   (* (exp ?u) (deriv ?u ?x)))", // exp
       "( (deriv (expt ?u ?c) ?x) (* (* ?c (expt ?u (- ?c 1))) (deriv ?u ?x)))",//expt
       "( (deriv (sqrt ?u) ?x)  (/ (* (/ 1 2) (deriv ?u ?x)) (sqrt ?u)))", // sqrt
       // trigonometry
       "((deriv (sin ?u) ?x) (* (cos ?u) (deriv ?u ?x)))", //sine
	   "((deriv (cos ?u) ?x) (* (- (sin ?u)) (deriv ?u ?x)))", //cosine
	   "((deriv (tan ?u) ?x) (* (+ 1 (expt (tan ?u) 2) (deriv ?u ?x)))", //tangent
       // second derivative
       "( (deriv2 ?u ?x)  (deriv (deriv ?u ?x) ?x) )",
       "( (deriv ?y ?x)   0)"   // this must be last!
       ));

    public static Cons javarestructpats = readlist( list(
       "( (return (if ?test ?then)) (if ?test (return ?then)) )",
       "( (return (if ?test ?then ?else)) (if ?test (return ?then) (return  ?else)) )",
       "( (defun ?fun ?args ?code) (zdefun ?fun (arglist ?args) (progn (return ?code))) )",
       "( (setq ?x (+ ?x 1)) (incf ?x) )"
       ));

    public static Cons javapats = readlist( list(
       "( (if ?test ?then) (znothing if zspace zlparen ?test zrparen zspace ztab zreturn ?then zreturn ))",
       "( (< ?x ?y)  (zlparen ?x zspace < zspace ?y zrparen))",
       "( (min ?x ?y) (Math.min zlparen ?x , zspace ?y zrparen))",
       "( (cons ?x ?y) (znothing cons zlparen ?x , zspace ?y zrparen))",
       "( (zdefun ?fun ?args ?code) (public zspace static zspace Object zspace ?fun zspace ?args zreturn ?code zreturn) )",
       "( (arglist (?x))   (zlparen Object zspace ?x zrparen))",
       "( (progn ?x) ({ ztab zreturn ?x zuntab zreturn }) )",
       "( (setq ?x ?y) (?x zspace = zspace ?y ; zreturn) )",
       "( (first ?x) (znothing first zlparen zlparen Cons zrparen ?x zrparen) )",
       
       "( (* pi ?y) (Math.PI zspace * zspace ?y))",
       "( (* fum ?y) (fum zspace * zspace ?y ; zreturn))",
       "( (* ?x ?y) (?x zspace * zspace ?y))",
       "( (+ ?x ?y) (?x zspace + zspace ?y))",
       "( (incf ?x) (?x zspace + + ; zreturn)",
       "( (expt ?x ?y) (Math.pow zlparen ?x , ?y zrparen))",
       "( (> ?x ?y)  (zlparen ?x zspace > zspace ?y zrparen))",
       "( (or ?x ?y) (?x zspace || zspace ?y))",
       "( (and ?x ?y) (?x zspace && zspace ?y))",
       "( (not ?x) (! ?x )",
       "( (<= ?x ?y)  (zlparen ?x zspace <= zspace ?y zrparen))",
       "( (- ?x ?y) (?x zspace - zspace ?y))",
       "( (return ?x) (return zspace ?x ; zreturn))",
       "( (if ?test ?then ?else) (if zspace zlparen zspace ?test zspace zrparen zspace { ztab zreturn ?then zuntab } zreturn else zspace { ztab zreturn ?else zuntab zreturn }))",
       "( (add1 ?x ?y) (znothing add1 zlparen ?x , ?y zrparen))",
       "( (?fun ?x)   (znothing ?fun zlparen ?x zrparen))"  // must be last
       ));


    // ****** your code ends here ******

    public static void main( String[] args ) {
    	
        Cons eqnsbat =
            readlist(
              list("(= loss_voltage (* internal_resistance current))",
                   "(= loss_power (* internal_resistance (expt current 2)))",
                   "(= terminal_voltage (- voltage loss_voltage))",
                   "(= power (* terminal_voltage current))",
                   "(= work (* charge terminal_voltage))" ) );
        
        System.out.println("battery = " +
                           solveqns(eqnsbat, (Cons)
             reader("((current 0.3)(internal_resistance 4.0)(voltage 12.0))"),
                                    "terminal_voltage"));
        Cons eqnscirc =
            readlist(
              list("(= acceleration (/ (expt velocity 2) radius))",
                   "(= force        (* mass acceleration))",
                   "(= kinetic_energy   (* (/ mass 2) (expt velocity 2)))",
                   "(= moment_of_inertia (* mass (expt radius 2)))",
                   "(= omega (/ velocity radius))",
                   "(= angular_momentum (* omega moment_of_inertia))" ));
        
        System.out.println("circular = " +
                           solveqns(eqnscirc, (Cons)
                                    reader("((radius 4.0)(mass 2.0)(velocity 3.0))"),
                                    "angular_momentum"));
        Cons eqnslens =
            readlist(
              list("(= focal_length (/ radius 2))",
                   "(= (/ 1 focal_length) (+ (/ 1 image_distance) (/ 1 subject_distance)))",
                   "(= magnification (- (/ image_distance subject_distance)))",
                   "(= image_height (* magnification subject_height))" ));

        
        System.out.println("magnification = " +
                           solveqns(eqnslens, (Cons)
              reader("((subject_distance 6.0)(focal_length 9.0))"),
                                    "magnification"));
 
        Cons eqnslift =
            readlist(
              list("(= gravity     9.80665 )",
                   "(= weight      (* gravity mass))",
                   "(= force       weight)",
                   "(= work        (* force height))",
                   "(= speed       (/ height time))",
                   "(= power       (* force speed))",
                   "(= power       (/ work time)) ))" ));
        
        System.out.println("power = " +
                           solveqns(eqnslift, (Cons)
             reader("((weight 700.0)(height 8.0)(time 10.0)))"),
                                    "power"));
        Cons binaryfn = (Cons) reader(
          "(defun ?function (tree) (if (consp tree) (?combine (?function (first tree)) (?function (rest tree))) ?baseanswer))");

        for ( Cons ptr = substitutions; ptr != null; ptr = rest(ptr) ) {
            Object trans = sublis((Cons) first(ptr), binaryfn);
            System.out.println("sublis:  " + trans.toString()); }

        Cons opttests = readlist( list(
          "(+ 0 foo)",
          "(* fum 1)",
          "(- (- y))",
          "(- (- x y))",
          "(+ foo (* fum 0))"
          ));

        for ( Cons ptr = opttests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("opt:  " + ((Cons)first(ptr)).toString());
            Object trans = transformfp(optpats, first(ptr));
            System.out.println("      " + trans.toString()); }


        Cons derivtests = readlist( list(
          "(deriv x x)",
          "(deriv 3 x)",
          "(deriv z x)",
          "(deriv (+ x 3) x)",
          "(deriv (* x 3) x)",
          "(deriv (* 5 x) x)",
          "(deriv (sin x) x)",
          "(deriv (sin (* 2 x)) x)",
          "(deriv (+ (expt x 2) (+ (* 3 x) 6)) x)",
          "(deriv2 (+ (expt x 2) (+ (* 3 x) 6)) x)",
          "(deriv (sqrt (+ (expt x 2) 2)) x)",
          "(deriv (log (expt (+ 1 x) 3)) x)"

          ));

        for ( Cons ptr = derivtests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("deriv:  " + ((Cons)first(ptr)).toString());
            Object trans = transformfp(derivpats, first(ptr));
            System.out.println("  der:  " + trans.toString());
            Object transopt = transformfp(optpats, trans);
            System.out.println("  opt:  " + transopt.toString()); }

        Cons javatests = readlist( list(
          "(* fum 7)",
          "(setq x y)",
          "(setq x (+ x 1))",
          "(setq area (* pi (expt r 2)))",
          "(if (> x 7) (setq y x))",
          "(if (or (> x 7) (< y 3)) (setq y x))",
          "(if (and (> x 7) (not (< y 3))) (setq y x))",
          "(defun factorial (n) (if (<= n 1) 1 (* n (factorial (- n 1)))))"
          ));

        for ( Cons ptr = javatests; ptr != null; ptr = rest(ptr) ) {
            System.out.println("java:  " + ((Cons)first(ptr)).toString());
            Cons restr = (Cons) transformfp(javarestructpats, first(ptr));
            System.out.println("       " + restr.toString());
            Cons trans = (Cons) transformfp(javapats, restr);
            System.out.println("       " + trans.toString());
            javaprintlist(trans, 0);
            System.out.println(); }


        for ( Cons ptr = substitutions; ptr != null; ptr = rest(ptr) ) {
            Object ltrans = sublis((Cons) first(ptr), binaryfn);
            System.out.println("sublis:  " + ltrans.toString());
            Cons restr = (Cons) transformfp(javarestructpats, ltrans);
            System.out.println("       " + restr.toString());
            Cons trans = (Cons) transformfp(javapats, restr);
            System.out.println("       " + trans.toString());
            javaprintlist(trans, 0);
            System.out.println(); }

      }

}
