/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 27 Mar 09; 06 Aug 10
 *          30 Dec 13
 */

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

public static Cons formulas = 
    list( list( "=", "s", list("*", new Double(0.5),
                               list("*", "a",
                                list("expt", "t", new Integer(2))))),
          list( "=", "s", list("+", "s0", list( "*", "v", "t"))),
          list( "=", "a", list("/", "f", "m")),
          list( "=", "v", list("*", "a", "t")),
          list( "=", "f", list("/", list("*", "m", "v"), "t")),
          list( "=", "f", list("/", list("*", "m",
                                         list("expt", "v", new Integer(2))),
                               "r")),
          list( "=", "h", list("-", "h0", list("*", new Double(4.94),
                                               list("expt", "t",
                                                    new Integer(2))))),
          list( "=", "c", list("sqrt", list("+",
                                            list("expt", "a",
                                                 new Integer(2)),
                                            list("expt", "b",
                                                 new Integer(2))))),
          list( "=", "v", list("*", "v0",
                               list("-", new Double(1.0),
                                    list("exp", list("/", list("-", "t"),
                                                     list("*", "r", "c"))))))
          );

    // Note: this list will handle most, but not all, cases.
    // The binary operators - and / have special cases.
public static Cons opposites = 
    list( list( "+", "-"), list( "-", "+"), list( "*", "/"),
          list( "/", "*"), list( "sqrt", "expt"), list( "expt", "sqrt"),
          list( "log", "exp"), list( "exp", "log") );

public static void printanswer(String str, Object answer) {
    System.out.println(str +
                       ((answer == null) ? "null" : answer.toString())); }

public static Cons reverse (Cons lst)
{
	Cons answer = null;
	for (; lst!=null; lst = rest(lst))
		answer = cons(first(lst), answer);
	return answer;
}

    // ****** your code starts here ******


public static Cons findpath(Object item, Object cave) 
{
	Cons result = null;

	if (consp(cave))
	{
		if (first((Cons)cave).equals(item))
		{
			result = cons("done",result);
			result = cons("first",result);
		}
		else if (consp(first((Cons)cave)) && rest((Cons)cave) == null)
			result = cons("first",findpath(item,first((Cons)cave)));
		else
			result = cons("rest",findpath(item,rest((Cons)cave)));
	}
	else if (stringp(cave) && cave.equals(item))
		result = cons("done",result);
	
		return result;
}


public static Object follow(Cons path, Object cave) 
{
	if (path == null || cave == null)
		return null;
	
	while (path!=null)
	{
		if(first(path).equals("first"))
			cave = first((Cons)cave);
		if(first(path).equals("rest"))
			cave = rest((Cons)cave);
		path = rest(path);
	}
	return cave;
}

public static Object corresp(Object item, Object tree1, Object tree2) 
{
	return follow(findpath(item,tree1),tree2);
}

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

public static Double solveit (Cons equations, String var, Cons values) 
{
	Cons eq = null;
	Cons allVars = cons(var,list());
	Cons tempValues = values;
	while (tempValues != null)
	{
		if (consp(values))
			allVars = cons(first((Cons)first(tempValues)),allVars);
		tempValues = rest(tempValues);
	}
	
	allVars = reverse(allVars);
	while(equations != null)
	{
		Cons expression = (Cons)first(equations);
		if (setEqual(allVars,vars((expression))))
		{
			eq = expression;
			break;
		}
		else 
			equations = rest(equations);
			
	}
	return eval(rhs((Cons)solve(eq, var)), values);
}

 
    // Include your functions vars and eval from the previous assignment.
    // Modify eval as described in the assignment.
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


    // ****** your code ends here ******

    public static void main( String[] args ) {

        Cons cave = list("rocks", "gold", list("monster"));
        Cons path = findpath("gold", cave);
        printanswer("cave = " , cave);
        printanswer("path = " , path);
        printanswer("follow = " , follow(path, cave));

        Cons caveb = list(list(list("green", "eggs", "and"),
                               list(list("ham"))),
                          "rocks",
                          list("monster",
                               list(list(list("gold", list("monster"))))));
        Cons pathb = findpath("gold", caveb);
        printanswer("caveb = " , caveb);
        printanswer("pathb = " , pathb);
        printanswer("follow = " , follow(pathb, caveb));

        Cons treea = list(list("my", "eyes"),
                          list("have", "seen", list("the", "light")));
        Cons treeb = list(list("my", "ears"),
                          list("have", "heard", list("the", "music")));
        printanswer("treea = " , treea);
        printanswer("treeb = " , treeb);
        printanswer("corresp = " , corresp("light", treea, treeb));
        System.out.println("formulas = ");
        Cons frm = formulas;
        Cons vset = null;
        while ( frm != null ) {
            printanswer("   "  , ((Cons)first(frm)));
            vset = vars((Cons)first(frm));
            while ( vset != null ) {
                printanswer("       "  ,
                    solve((Cons)first(frm), (String)first(vset)) );
                vset = rest(vset); }
            frm = rest(frm); }

        Cons bindings = list( list("a", (Double) 32.0),
                              list("t", (Double) 4.0));
        printanswer("Eval:      " , rhs((Cons)first(formulas)));
        printanswer("  bindings " , bindings);
        printanswer("  result = " , eval(rhs((Cons)first(formulas)), bindings));

        printanswer("Tower: " , solveit(formulas, "h0",
                                            list(list("h", new Double(0.0)),
                                                 list("t", new Double(4.0)))));

        printanswer("Car: " , solveit(formulas, "a",
                                            list(list("v", new Double(88.0)),
                                                 list("t", new Double(8.0)))));
        
        printanswer("Capacitor: " , solveit(formulas, "c",
                                            list(list("v", new Double(3.0)),
                                                 list("v0", new Double(6.0)),
                                                 list("r", new Double(10000.0)),
                                                 list("t", new Double(5.0)))));

        printanswer("Ladder: " , solveit(formulas, "b",
                                            list(list("a", new Double(6.0)),
                                                 list("c", new Double(10.0)))));


      }

}
