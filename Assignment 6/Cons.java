/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 27 Mar 09; 18 Mar 11; 09 Oct 13
 *          30 Dec 13
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
    // test whether argument is a Cons
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
    public static int pow (int x, int n) {        // x to the power n
        if ( n <= 0 ) return 1;
        if ( (n & 1) == 0 )
            return square( pow(x, n / 2) );
        else return x * pow(x, n - 1); }

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

    // You can use these association lists if you wish.
    public static Cons engwords = list(list("+", "sum"),
                                       list("-", "difference"),
                                       list("*", "product"),
                                       list("/", "quotient"),
                                       list("expt", "power"));

    public static Cons opprec = list(list("=", 1),
                                     list("+", 5),
                                     list("-", 5),
                                     list("*", 6),
                                     list("/", 6) );

    public static Cons reverse (Cons lst)
    {
    	Cons answer = null;
    	for (; lst!=null; lst = rest(lst))
    		answer = cons(first(lst), answer);
    	return answer;
    }
    
    
    // ****** your code starts here ******


public static Integer maxbt (Object tree) 
{
	Integer max = Integer.MIN_VALUE;
	if (consp(tree))
	{	
		if (maxbt(first((Cons)tree)) > max)
		max = maxbt(first((Cons)tree));
		if (maxbt(rest((Cons) tree)) > max)
		max = maxbt(rest((Cons)tree));
	}
	else if (tree!=null && (Integer)tree > max)
		max = (Integer)tree;
	return max;
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

public static boolean occurs(Object value, Object tree) 
{
	boolean occured = false;
	if (consp(tree))
		occured = occurs(value,lhs((Cons)tree))
				|| occurs(value,rhs((Cons)tree));
	else
		occured = value.equals(tree);
	return occured;
}

public static Integer eval (Object tree) 
{
	Integer value = 0;
	if(consp(tree))
	{
		String operator = (String) op((Cons)tree);
		Integer left = eval(lhs((Cons)tree));
		Integer right = eval(rhs((Cons)tree));
		
		switch(operator)
		{
			case "+":
				value = left + right;
				break;
			case "-":
				if (rest((Cons)rhs((Cons) tree)) == null)
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
				value = pow(left,right);
				break;
			default:
				break;
		}
	}
	else if (tree != null)
		value = (Integer) tree;
	return value;
}

public static Integer eval (Object tree, Cons bindings) 
{
	Integer value = 0;
	if(consp(tree))
	{
		String operator = (String) op((Cons)tree);
		Integer left = eval(lhs((Cons)tree),bindings);
		Integer right = eval(rhs((Cons)tree),bindings);
		
		switch(operator)
		{
			case "+":
				value = left + right;
				break;
			case "-":
				if (rest((Cons)rhs((Cons) tree)) == null)
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
				value = pow(left,right);
				break;
			default:
				break;
		}
	}
	else if (tree != null)
	{
		Cons assocVar = assoc(tree,bindings);
		
		if (assocVar == null)
			value = (Integer) tree;
		else
			value = (Integer) first(rest(assocVar));
	}
	return value;
}

public static Cons english (Object tree) 
{
	Cons expression = null;
	if (consp(tree))
	{
		Cons assocVar = assoc((String) op((Cons)tree),engwords);
		Cons left = english(lhs((Cons)tree));
		Cons right = english(rhs((Cons)tree));	
		
		expression = cons("the",expression);
		
		if (first(assocVar).equals("-") && first(right) == null)
			expression = cons("negative",expression);
		else
		expression = cons(first(rest(assocVar)),expression);
		expression = cons("of",expression);
		
		while(left!=null)
		{
			expression = (cons(first(left),expression));
			left = rest(left);
		}
		if (first(right) != null) 
			expression = cons("and", expression);
		while(right!=null)
		{
			expression = (cons(first(right),expression));
			right = rest(right);
		}
	}
	
	else if (tree != null)
		return cons(tree,expression);	
	
	return reverse(expression);
}

public static String tojava (Object tree) {
   return (tojavab(tree, 0) + ";"); }

public static String tojavab (Object tree, int prec) 
{
	String expression = "";
	int precedence = 0;
	if (consp(tree))
	{
		String operator = (String) op((Cons)tree);
		Cons assocVar = assoc(operator,opprec);
		Object left = lhs((Cons)tree);
		Object right = rhs((Cons)tree);	
		if (operator.equals("-") && right == null ||
				(first(rest(assocVar)) == null))
			precedence = 6;
		else
		precedence = (int) first(rest(assocVar));
		String leftString = tojavab(left,precedence);
		if (right==null)
		{
			if (operator.equals("-"))
				expression = ("(" + operator + leftString + ")");
			else
				expression = ("Math." + operator + "(" + leftString + ")");
		}
		else
		{
			String rightString = tojavab(right,precedence);
			expression = leftString + operator + rightString;
			if (precedence <= prec)
				expression = "(" + expression + ")";
		}
	}
	else if (tree != null)
		expression = tree.toString();
		
	return expression;
}

    // ****** your code ends here ******

    public static void main( String[] args ) {
        Cons bt1 = (Cons) reader("(((23 77) -3 88) ((((99)) (7))) 15 -1)");
        System.out.println("bt1 = " + bt1.toString());
        System.out.println("maxbt(bt1) = " + maxbt(bt1));

        Cons expr1 = list("=", "f", list("*", "m", "a"));
        System.out.println("expr1 = " + expr1.toString());
        System.out.println("vars(expr1) = " + vars(expr1).toString());

        Cons expr2 = list("=", "f", list("/", list("*", "m",
                                                   list("expt", "v",
                                                        new Integer(2))),
                                         "r"));
        System.out.println("expr2 = " + expr2.toString());
        System.out.println("vars(expr2) = " + vars(expr2).toString());
        System.out.println("occurs(m, expr2) = " + occurs("m", expr2));
        System.out.println("occurs(7, expr2) = " + occurs(new Integer(7), expr2));
        Cons expr9 = list( "=", "v",
                                list("*", "v0",
                                     list("exp", list("/", list("-", "t"),
                                                      list("*", "r", "c")))));
        System.out.println("expr9 = " + expr9.toString());
        System.out.println("vars(expr9) = " + vars(expr9).toString());
        System.out.println("occurs(c, expr9) = " + occurs("c", expr9));
        System.out.println("occurs(m, expr9) = " + occurs("m", expr9));

        Cons expr3 = list("+", new Integer(3), list("*", new Integer(5),
                                                         new Integer(7)));
        System.out.println("expr3 = " + expr3.toString());
        System.out.println("eval(expr3) = " + eval(expr3));

        Cons expr4 = list("+", list("-", new Integer(3)),
                               list("expt", list("-", new Integer(7),
                                                      list("/", new Integer(4),
                                                                new Integer(2))),
                                            new Integer(3)));
        System.out.println("expr4 = " + expr4.toString());
        System.out.println("eval(expr4) = " + eval(expr4));

        System.out.println("eval(b) = " + eval("b", list(list("b", 7))));

        Cons expr5 = list("+", new Integer(3), list("*", new Integer(5), "b"));
        System.out.println("expr5 = " + expr5.toString());
        System.out.println("eval(expr5) = " + eval(expr5, list(list("b", 7))));

        Cons expr6 = list("+", list("-", "c"),
                          list("expt", list("-", "b", list("/", "z", "w")),
                                            new Integer(3)));
        Cons alist = list(list("c", 3), list("b", 7), list("z", 4),
                          list("w", 2), list("fred", 5));
        System.out.println("expr6 = " + expr6.toString());
        System.out.println("alist = " + alist.toString());
        System.out.println("eval(expr6) = " + eval(expr6, alist));
        System.out.println("english(expr5) = " + english(expr5).toString());
        System.out.println("english(expr6) = " + english(expr6).toString());
        System.out.println("tojava(expr1) = " + tojava(expr1).toString());
        Cons expr7 = list("=", "x", list("*", list("+", "a", "b"), "c"));
        System.out.println("expr7 = " + expr7.toString());
        System.out.println("tojava(expr7) = " + tojava(expr7).toString());
        Cons expr8 = list("=", "x", list("*", "r", list("sin", "theta")));
        System.out.println("expr8 = " + expr8.toString());
        System.out.println("tojava(expr8) = " + tojava(expr8).toString());
        System.out.println("expr9 = " + expr9.toString());
        System.out.println("tojava(expr9) = " + tojava(expr9).toString());


       Cons set3 = list("d", "b", "c", "a");

      }

}
