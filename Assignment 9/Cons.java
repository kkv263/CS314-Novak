/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 30 Oct 08; 03 Nov 08
 *          11 Nov 09; 18 Nov 08; 08 Apr 09; 07 Nov 09; 08 Apr 11; 11 Apr 11
 *          19 Apr 11; 23 May 14; 25 Feb 15
 */

import java.util.StringTokenizer;
import java.util.PriorityQueue;
import java.util.Random;

import org.jfugue.*;  // ***** Uncomment to use jFugue

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }
@SuppressWarnings("unchecked")

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
    public static Object fourth (Cons x) { return first(rest(rest(rest(x)))); }
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
        return transformrb(allpats, transformlst(allpats, listt)); }
    Object res = transformrb(allpats, input);
    //    System.out.println("   result =  " + res.toString());
    return res; }

    // Transform a single item.  If no change, returns original.
public static Object transformrb(Cons pats, Object input) {
    if ( pats == null ) return input;
    if ( input == null ) return null;
    Cons bindings = match(first((Cons)first(pats)), input);
    if ( bindings == null ) return transformrb(rest(pats), input);
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
            else if ( ((String)first(lst)).equals("zreturn" ) ) {
                System.out.println();
                for (int i = 0; i < tabs; i++) System.out.print("\t"); }
            else javaprint(first(lst), tabs);
        else javaprint(first(lst), tabs);
        javaprintlist(rest(lst), tabs); } }
          
public static Cons formulas = readlist( list( 
   "(= s (* 0.5 (* a (expt t 2))))",
   "(= s (+ s0 (* v t)))",
   "(= a (/ f m))",
   "(= v (* a t))",
   "(= f (/ (* m v) t))",
   "(= f (/ (* m (expt v 2)) r))",
   "(= h (- h0 (* 4.94 (expt t 2))))",
   "(= c (sqrt (+ (expt a 2) (expt b 2))))",
   "(= v (* v0 (- 1 (exp (/ (- t) (* r c))))))" ));

public static int fcount = 0;
public static Random random = new Random(7);

public static String[] instruments = {"[Piano]",
                                      "[Piano]",
                                      "[Piano]",
                                      "[Piano]",
                                      "[bass_drum]",
                                      "[acoustic_snare]",
                                      "[pedal_hi_hat]",
                                      "[crash_cymbal_1]",
                                      "[cowbell]",
                                      "[ride_bell]",
                                      "[hand_clap]",
                                      "[tambourine]" };
public static int[] insttime = new int[12];
public static String[] inststring = new String[12];
    public static String[] durations = {"", "s", "i", "i.", "q",
                                        "q", "q.", "q.", "h",
                                        "h", "h", "h", "h.",
                                        "h.", "h.", "h.", "w",
                                        "w.", "w.", "w.", "w."};
    public static int[] wholedur = {0, 1, 2, 2,
                                    4, 4, 4, 4,
                                    8, 8, 8, 8,
                                    8, 8, 8, 8,
                                    16, 16, 16, 16,
                                    16, 16, 16, 16,
                                    16, 16, 16, 16,
                                    16, 16, 16, 16};

public static void restto(int inst, int time) {
    int diff = time - insttime[inst];
    if ( diff > 0 ) {
        if ( (diff % 2) != 0 ) {
            inststring[inst] += " Rs";
            diff--; }
        if ( (diff % 4) != 0 ) {
            inststring[inst] += " Ri";
            diff -= 2; }
        if ( (diff % 8) != 0 ) {
            inststring[inst] += " Rq";
            diff -= 4; }
        if ( (diff % 16) != 0 ) {
            inststring[inst] += " Rh";
            diff -= 8; }
        while ( diff > 0 ) {
            inststring[inst] += " Rw";
            diff -= 16; }
    }
}

public static void emit(int inst, int time, int d) {
    System.out.println("emit: " + time + " " + instruments[inst] + " " + d);
    restto(inst, time);
    inststring[inst] += (" " + instruments[inst] + durations[d]);
    insttime[inst] = time + d;
}
  
public static void emitp(int voice, String note, int time, int d) {
    System.out.println("emitp: " + time + " " + instruments[voice] + " "
                       + voice + " " + note + " " + d);
    restto(voice, time);
    if ( d == wholedur[d] )
       inststring[voice] += ( ((time < insttime[voice]) ? "+" : " ")
                            + note + durations[d]);
       else inststring[voice] += (" " + note + durations[d - wholedur[d]]
                                  + "- " + note + "-" + durations[wholedur[d]]);
    insttime[voice] = time + d;
}

// empty buffers into a music string
    public static String mstring(int tempo) {
        String all = "";
        for ( int i = 0; i < 4; i++ )
            if ( insttime[i] > 0 ) {
                all += (" V" + i);
                if (tempo > 0)
                    all += (" T" + tempo);
                all += inststring[i]; };
        boolean other = false;
        for ( int i = 4; i < 12; i++ )
            if ( insttime[i] > 0 ) other = true;
        if (other)
           { all += " V9 ";
             for ( int i = 4; i < 12; i++ )
                 if ( insttime[i] > 0 ) {
                    all += (" L" + (i - 3) + " ");
                    all += inststring[i]; }; };
        return all;
    }
    // ****** your code starts here ******


    // include your function vars from previous assignment
    public static Cons vars (Object expr) 
    {
    	Cons variables = null;
    	if (consp(expr))
    		variables = union(vars(lhs((Cons)expr)),vars(rhs((Cons)expr)));
    	else if (stringp(expr))
    		variables = cons(expr,variables);
    	return variables;
    }

    public static Cons algpats = readlist( list(
       "( (= ?x (+ ?y ?z))    (= (- ?x ?y) ?z) )",
       "( (= ?x (+ ?y ?z))    (= (- ?x ?z) ?y) )",
       // add more
       "( (= ?x (- ?y ?z)	) (= (+ ?x ?y) ?z) )",
       "( (= ?x (- ?y ?z)	) (= (+ ?x ?z) ?y) )",
       "( (= ?x (* ?y ?z)	) (= (/ ?x ?y) ?z) )",
       "( (= ?x (* ?y ?z)   ) (= (/ ?x ?z) ?y) )",
       "( (= ?x (/ ?y ?z)   ) (= (* ?x ?y) ?z) )",
       "( (= ?x (/ ?y ?z)   ) (= (* ?x ?z) ?y) )",
       "( (= ?x (exp ?y)    ) (= (log ?x) ?y   )",
       "( (= ?x (log ?y)    ) (= (exp ?x) ?y   )",
       "( (= ?x (expt ?y 2) ) (= (sqrt ?x) ?y  )",
       "( (= ?x (sqrt ?y)   ) (= (expt ?x 2) ?y)",
       "((= ?x (- ?y)		) (= (- ?x) ?y)	   )"
       ));

    public static Cons solve (Cons e, String v){
    	Cons list =  solveHelper(e,v);
    	if (list == null)
    		return solveHelper(list("=", rhs(e), lhs(e)), v);
    	return list;
    }
    
    public static Cons solveHelper (Cons e, String v)
    {
    	if (v.equals(lhs(e)))
    		return e;
    	if (v.equals(rhs(e)))
    		return list("=", v, lhs(e));
    	if (!consp(e))
    		return null;
    	else
    	{
    		Cons algPatterns = algpats;
    		while (algPatterns != null)
    		{
    			Cons transformEq = (Cons)transform((Cons)first(algPatterns),e);
    			Cons solveEq= null;
    			if (algPatterns != null)
    				solveEq = solveHelper(transformEq,v);
    			if (solveEq!= null)
    				return solveEq;
    			algPatterns = rest(algPatterns);
    			
    		}
    		return null;
    	}
    }

    public int hashCode() 
    {
    	return conshash(this); 
    }

    public static int conshash(Object e) 
    {
    	if (e == null)
    		return 0;
    	int hashFirst = 0;
    	int hashRest = 0;

    	if (first((Cons) e) == null);
    	else
    		hashFirst = first((Cons) e).hashCode();
    	if (rest((Cons)e) == null);
    	else
    		hashRest = rest((Cons)e).hashCode();
    	
    	return (hashFirst * 17) ^ (hashRest * 127);
    }

    // total time to execute an action
    public static int totaltime(Cons action) 
    {
    	String first = (String)first((Cons)action);
    	Cons rest = rest(action);
    	switch (first)
    	{
    		case ("repeat"):
    			return (Integer)first(((Cons)rest)) * totaltime((Cons)first(rest(((Cons)rest))));
	    	case ("seq"):
	    		int sum = 0;
	    		Cons seqtemp = rest(action);
	    		while(seqtemp != null)
	    		{
	    			sum += totaltime((Cons)first((Cons)seqtemp));
	    			seqtemp = rest(seqtemp);
	    		}
	    		return sum;
	    	case ("sync"):
	    		int max = Integer.MIN_VALUE;
	    		Cons synctemp = rest(action);
	    		while (synctemp != null)
	    		{
	    			if ((Integer) totaltime((Cons)first((Cons)rest)) > max) 
	    				max = (Integer) totaltime((Cons)first((Cons)rest));
	    			synctemp = rest(synctemp);
	    		}
	    		return max;
	    	case ("piano"):
	    		return (Integer) third((Cons)rest);
	    	default:
	    		return 0;
    	}
    	
    }

    // you should not need to change this function
    // start by putting the total program on the queue at time 0
    public static PriorityQueue initpq(Cons action) {
        PriorityQueue pq = new PriorityQueue();
        pq.add(new Event(action, 0));
        System.out.println("new program: " + action.toString());
        for ( int i = 0; i < 12; i++ ) {
            inststring[i] = "";
            insttime[i] = 0; };
        return pq; }

    // you should not need to change this function
    // add an event to the priority queue
    public static PriorityQueue
           addevent(PriorityQueue pq, Cons action, int time) {
        if ( action != null ) pq.add(new Event(action, time));
        return pq; }

    // you should not need to change this function
    // if the queue is not empty, remove the next event and execute it
        public static void simulator(PriorityQueue pq) {
        while ( pq.size() > 0 ) {
            Event e = (Event) pq.poll();
            int tm = e.time();
            Cons act = e.action();
             //     System.out.println(":" + tm + " " + act.toString());
            execute(pq, act, tm);
         }
        return;
     }

    // execute an event
        public static void execute(PriorityQueue pq, Cons act, int time) {
            String command = (String) first(act);
            // For simple commands, just emit the I/O command.
            // better to look up simple commands in a table, but this works.
            switch(command)
            {
	            case "rest": 
	                addevent(pq,null,time);
	                return; 
	            case "boom": 
	                emit(4, time, (int)(Integer)second(act));
	                return; 
	            case "snare": 
	                emit(5, time, (int)(Integer)second(act) );
	                return; 
	            case "hat": 
	                emit(6, time,(int)(Integer)second(act) );
	                return;
	            case "cymbal": 
	                emit(7, time, (int)(Integer)second(act));
	                return;
	            case "cowbell": 
	                emit(8, time,(int)(Integer)second(act));
	                return;
	            case "bell": 
	                emit(9, time, (int)(Integer)second(act));
	                return;
	            case "clap": 
	                emit(10, time, (int)(Integer)second(act));
	                return; 
	            case "kaboom":
	            	addevent(pq,list("seq",list("rest",time/4),list("boom",time/4),list("boom",time/2)),time);
	            	return;
	            case "tambourine":
	            	double rNumber = (int) random.nextDouble() * (int)(Integer)second(act);
	            	if(rNumber >= 2)
	            	addevent(pq,list("tambourine",rNumber),(int) ((int)(Integer)second(act) + time -rNumber));
	            	emit(11, time, 2);
	            	return;
	            case "seq":
	            	Cons seqtemp = rest(act);
	            	while(seqtemp != null)
	            	{
	            		addevent(pq,(Cons) first(seqtemp),time);
	            		time += totaltime((Cons)first(seqtemp));
	            		seqtemp = rest(seqtemp);
	            	}
	            	return;
	            case "sync":
	            	Cons synctemp = rest(act);
	            	while(synctemp != null)
	            	{
	            		addevent(pq,(Cons) first(synctemp),time);
	            		synctemp = rest(synctemp);
	            	}
	            	return;
	            case "repeat":
	            	Cons repaction = (Cons) third(act);
	            	for (int i = 0; i < (Integer)second(act); i++)
	            	{
	            		addevent(pq,repaction,time += totaltime(repaction));
	            	}
	            	return;
	            case "piano":
	            	emitp((int)(Integer)second(act),(String)third(act),time,(int)(Integer)fourth(act));
	            	return;
            }
            
           
        }

    public static Cons round( Cons melody ) 
    {
    	int dVoice1 = totaltime(melody);
    	int dVoice2 = totaltime(melody) / 4;
    	int dVoice3 = totaltime(melody) / 2;
    	
    	Cons voice1 = (Cons) subst(Integer.valueOf(0),"?i",melody);
    	Cons voice2 = (Cons) subst(Integer.valueOf(1),"?i",melody);
    	Cons voice3 = (Cons) subst(Integer.valueOf(2),"?i",melody);

    	return list("sync",
    			list("seq" , list("rest",0),voice1),
    			list("seq" , list("rest",dVoice2),voice2),
    			list("seq" , list("rest",dVoice3),voice3));
    }

    // ****** your code ends here ******

    public static void main( String[] args ) {

        System.out.println("formulas = ");
        Cons frm = formulas;
        Cons vset = null;
        while ( frm != null ) {
            System.out.println("   "  + ((Cons)first(frm)).toString());
            vset = vars((Cons)first(frm));
            while ( vset != null ) {
                System.out.println("       "  +
                    solve((Cons)first(frm), (String)first(vset)) );
                vset = rest(vset); }
            frm = rest(frm); }

        Cons hashtest =  (Cons) readlist(list("(a)", "(a b)", "(a b c ab +)"));
        for (Cons hptr = hashtest; hptr != null; hptr = rest(hptr) ) {
            Cons ls = (Cons) first(hptr);
            for (Cons ptr = ls; ptr != null; ptr = rest(ptr) ) {
                String s = (String) first(ptr);
                int shash = s.hashCode();
                System.out.println("Hash of " + s + " = " + shash); }
            System.out.println("Hash of " + ls.toString() + " = "
                               + ls.hashCode());  }
        fcount = 0;

        final Functor myf = new Functor()
            { public Object fn (Object x)
                { fcount++;
                  return Math.sqrt((Double) x); }};

        Memoizer mymem = new Memoizer(myf);
        Double[] vals = { 2.0, 3.0, 4.0, 2.0, 2.5, 3.0, 3.5};
        for (int i=0; i < vals.length; i++ )
            System.out.println("Fn of " + vals[i] + " = " +
                               mymem.call(vals[i]));
        System.out.println("Number of function calls = " + fcount);

        Player player = new Player();
        		
        PriorityQueue pqa = initpq((Cons) reader("(seq (boom 4) (bell 4))"));
        simulator(pqa);
        String stra = mstring(0);
        System.out.println(stra);
        player.play(new Pattern(stra));

        PriorityQueue pqb = initpq((Cons) reader("(repeat 2 (seq (boom 4) (bell 4)))"));
        simulator(pqb);
        String strb = mstring(0);
        System.out.println(strb);
        player.play(new Pattern(strb));

        PriorityQueue pqc =
            initpq((Cons)
                   reader("(seq (repeat 2 (kaboom 4)) (repeat 3 (cymbal 2)))"));
        simulator(pqc);
        String strc = mstring(0);
        System.out.println(strc);
        player.play(new Pattern(strc));

        PriorityQueue pqd =
            initpq((Cons)
                   reader("(tambourine 32)"));
        simulator(pqd);
        String strd = mstring(0);
        System.out.println(strd);
        player.play(new Pattern(strd));

	PriorityQueue pqe = initpq((Cons) reader("(seq (piano 0 E5 1) (piano 0 A5 1) (piano 0 C6 1) (piano 0 B5 1) (piano 0 E5 1) (piano 0 B5 1) (piano 0 D6 1) (piano 0 C6 2) (piano 0 E6 2) (piano 0 G#5 2) (piano 0 E6 2) (piano 0 A5 1) (piano 0 E5 1) (piano 0 A5 1) (piano 0 C6 1) (piano 0 B5 1) (piano 0 E5 1) (piano 0 B5 1) (piano 0 D6 1) (piano 0 C6 2) (piano 0 A5 2) (rest 2))"));
        simulator(pqe);
        String stre = mstring(0);
        System.out.println(stre);
        player.play(new Pattern(stre));

        PriorityQueue pqf =
            initpq((Cons)
                   reader("(sync (seq (boom 2) (rest 4) (kaboom 4) (rest 6) (boom 2) (rest 4) (kaboom 4) (boom 2) (rest 4)) (repeat 16 (seq (hat 1) (rest 1))))"
			  ));
        simulator(pqf);
        String strf = mstring(0);
        System.out.println(strf);
        player.play(new Pattern(strf));

	Cons rhyth = readlist( list (
	     "(seq (boom 2) (rest 4) (kaboom 4) (rest 6) (boom 2) (rest 4) (kaboom 4) (boom 2) (rest 4))",
	     "(seq (rest 4) (snare 2) (repeat 3 (seq (rest 6) (snare 2))) (rest 2))",
	     "(repeat 16 (seq (hat 1) (rest 1)))",
	     "(seq (rest 30) (cymbal 1) (rest 1))" ));
        PriorityQueue pqh =
            initpq( list( "repeat", 4, cons("sync", rhyth)));
        simulator(pqh);
        String strh = mstring(0);
        System.out.println(strh);
        player.play(new Pattern(strh));

        Cons melody = (Cons) reader("(seq (repeat 2 (seq (piano ?i C5 4) (piano ?i D5 4) (piano ?i E5 4) (piano ?i C5 4)))     (repeat 2 (seq (piano ?i E5 4) (piano ?i F5 4) (piano ?i G5 8)))     (repeat 2 (seq (piano ?i G5 2) (piano ?i A5 2) (piano ?i G5 2) (piano ?i F5 2) (piano ?i E5 4) (piano ?i C5 4)))     (repeat 2 (seq (piano ?i C5 4) (piano ?i G4 4) (piano ?i C5 8))))");
        PriorityQueue pqi = initpq((Cons) subst( Integer.valueOf(0),
                                                 "?i", melody));
        simulator(pqi);
        String stri = mstring(0);
        System.out.println(stri);
        player.play(new Pattern(stri));

        System.out.println("Total time of melody = "
                           + (Integer)totaltime(melody));

        PriorityQueue pqj = initpq( round(melody) );
        simulator(pqj);
        String strj = mstring(0);
        System.out.println(strj);
	    player.play(new Pattern(strj));

        // "The Entertainer" by Scott Joplin
        // Available by public domain
        // Arranged by Gilbert DeBenedetti
        // For sheet music, see
        // http://gmajormusictheory.org/Freebies/Level3/3Enterta.pdf
        Cons ent0r = cons("seq", readlist( list (
"(seq (rest 8) (piano 0 D5 4) (piano 0 D#5 4))",
"(seq (piano 0 E5 4) (piano 0 C6 8) (piano 0 E5 4) (piano 0 C6 8))",
"(seq (piano 0 E5 4) (piano 0 C6 20) )")));

	Cons ent0a = cons("seq", readlist( list (
"(seq (piano 0 D7 4) (piano 0 E7 4) (piano 0 C7 4) (piano 0 A6 8))",
"(seq (piano 0 B6 4) (piano 0 G6 4) (rest 4) (piano 0 D6 4))",
"(seq (piano 0 E6 4) (piano 0 C6 4) (piano 0 A5 8) (piano 0 B5 4))",
"(seq (piano 0 G5 4) (rest 4) (piano 0 D5 4) (piano 0 E5 4))",
"(seq (piano 0 C5 4) (piano 0 A4 8) (piano 0 B4 4) (piano 0 A4 4))",
"(seq (piano 0 Ab4 4) (piano 0 G4 4) (rest 12))",
"(seq (sync (piano 0 B5 4) (piano 0 F6 4) (piano 0 G6 4)) (rest 4))",
"(seq (piano 0 D5 4) (piano 0 D#5 4) (piano 0 E5 4) (piano 0 C6 8) (piano 0 E5 4))",
"(seq (piano 0 C6 8) (piano 0 E5 4) (piano 0 C6 20) (rest 4))",
"(seq (piano 0 C6 4))",
"(seq (piano 0 D6 4) (piano 0 D#6 4) (piano 0 E6 4) (piano 0 C6 4))",
"(seq (piano 0 D6 4) (piano 0 E6 8) (piano 0 B5 4) (piano 0 D6 8))",
"(seq (piano 0 C6 16))")));

	Cons ent0b = cons("seq", readlist( list (
"(seq (rest 64) (rest 8) (piano 0 A5 4))",     // 64
"(seq (piano 0 G5 4) (piano 0 F#5 4) (piano 0 A5 4) (piano 0 C6 4))",
"(seq (piano 0 E6 8) (piano 0 D6 4) (piano 0 C6 4) (piano 0 A5 4))",
"(seq (piano 0 D6 16) (rest 64))")));

	Cons ent0c = cons("seq", readlist( list (
"(seq (rest 4) (piano 0 C6 4))",
"(seq (piano 0 D6 4) (piano 0 D#6 4) (piano 0 E6 4) (piano 0 C6 4))",
"(seq (piano 0 D6 4) (piano 0 E6 8) (piano 0 B5 4) (piano 0 D6 8))",
"(seq (piano 0 C6 16) (rest 8) (piano 0 C6 4) (piano 0 D6 4))",
"(seq (piano 0 E6 4) (piano 0 C6 4) (piano 0 D6 4) (piano 0 E6 8))",
"(seq (piano 0 C6 4) (piano 0 D6 4) (piano 0 C6 4) (piano 0 E6 4))",
"(seq (piano 0 C6 4) (piano 0 D6 4) (piano 0 E6 8) (piano 0 C6 4))",
"(seq (piano 0 D6 4) (piano 0 C6 4) (piano 0 E6 4) (piano 0 C6 4))",
"(seq (piano 0 D6 4) (piano 0 E6 8) (piano 0 B5 4) (piano 0 D6 8))",
"(seq (piano 0 C6 4) (rest 48) )" ) ));

        Cons ent0 = list("seq", ent0a,
                         list("sync",
                              list("seq", ent0r, list("rest", 64), ent0r),
                              ent0b),
                         ent0c);

	Cons ent1 = cons("seq", readlist( list (
"(seq (rest 112) (piano 1 B3 4) (rest 12) (piano 1 C4 4) (rest 4))",
"(seq (sync (piano 1 E4 4) (piano 1 G4 4)) (rest 4) (piano 1 C4 4))",
"(seq (rest 4) (sync (piano 1 E4 4) (piano 1 Bb4 4)) (rest 4))",
"(seq (piano 1 C4 4) (rest 4) (sync (piano 1 F4 4) (piano 1 A4 4)))",
"(seq (rest 4) (sync (piano 1 C4 4) (piano 1 E4 4) (piano 1 G4 4)))",
"(seq (rest 12) (piano 1 C4 4) (rest 4))",
"(seq (sync (piano 1 E4 4) (piano 1 G4 4)) (rest 4) (piano 1 G3 16))",
"(seq (piano 1 C4 4) (rest 4) (piano 1 G3 4) (rest 4) (piano 1 C4 4))",
"(seq (rest 4) (rest 8) (piano 1 C4 4) (rest 4))",
"(seq (sync (piano 1 E4 4) (piano 1 G4 4)) (rest 4) (piano 1 C4 4))",
"(seq (rest 4) (sync (piano 1 E4 4) (piano 1 Bb4 4)) (rest 4))",
"(seq (piano 1 C4 4) (rest 4) (sync (piano 1 F4 4) (piano 1 A4 4)))",
"(seq (rest 4) (sync (piano 1 C4 4) (piano 1 E4 4) (piano 1 G4 4)))",
"(seq (rest 4) (rest 8) (piano 1 D4 4) (rest 4))",
"(seq (sync (piano 1 F#4 4) (piano 1 A4 4)) (rest 4) (piano 1 D4 4))",
"(seq (rest 4) (sync (piano 1 F#4 4) (piano 1 A4 4)) (rest 4))",
"(seq (piano 1 G4 4) (rest 4) (piano 1 G3 4) (rest 4) (piano 1 A3 4))",
"(seq (rest 4) (piano 1 B3 4) (rest 4) (piano 1 C4 4) (rest 4))",
"(seq (sync (piano 1 E4 4) (piano 1 G4 4)) (rest 4) (piano 1 C4 4))",
"(seq (rest 4) (sync (piano 1 E4 4) (piano 1 Bb4 4)) (rest 4))",
"(seq (piano 1 C4 4) (rest 4) (sync (piano 1 F4 4) (piano 1 A4 4)))",
"(seq (rest 4) (sync (piano 1 C4 4) (piano 1 E4 4) (piano 1 G4 4)))",
"(seq (rest 12) (piano 1 C4 4) (rest 4))",
"(seq (sync (piano 1 E4 4) (piano 1 G4 4)) (rest 4) (piano 1 G3 16))",
"(seq (piano 1 C4 4) (rest 4) (piano 1 G3 4) (rest 4) (piano 1 C4 4))",
"(seq (rest 4) (rest 8))",
"(seq (sync (piano 1 C4 16) (piano 1 E4 16) (piano 1 G4 16)))",
"(seq (sync (piano 1 C4 16) (piano 1 Bb4 16)))",
"(seq (sync (piano 1 C4 16) (piano 1 A4 16)))",
"(seq (sync (piano 1 C4 16) (piano 1 Ab4 16)))",
"(seq (sync (piano 1 C4 8) (piano 1 G4 8)) (rest 8) (piano 1 G3 16))",
"(seq (piano 1 C4 4) (rest 4) (piano 1 G3 4) (rest 4) (piano 1 C3 4))",
"(seq (rest 28))" ) ) );

        System.out.println();
        System.out.println(ent0);
        System.out.println();
        System.out.println(ent1);
        System.out.println();
	Cons ent = list( "sync", ent0, ent1);
        System.out.println(ent);
        System.out.println();
        PriorityQueue pqk =  initpq(ent);
        simulator(pqk);
        String strk = mstring(240);
        System.out.println(strk);
        player.play(new Pattern(strk));
        
        
        
	System.exit(0); // If using Java 1.4 or lower
	
      }

}
