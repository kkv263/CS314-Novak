/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          06 Oct 08; 07 Oct 08; 09 Oct 08; 23 Oct 08; 30 Oct 08; 03 Nov 08
 *          11 Nov 09; 18 Nov 08; 20 Nov 08
 */

import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Random;

interface Functor { Object fn(Object x); }
interface Predicate { boolean pred(Object x); }

interface Mapper { void map(String key, String value, MapReduce mr); }
interface Reducer { void reduce(String key, Cons value, MapReduce mr); }

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

public static Cons nreverse (Cons lst) {
  Cons last = null; Cons next;
  while (lst != null)
    { next =  rest(lst);
      setrest(lst, last);
      last = lst;
      lst = next; };
  return last; }

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

    // tree equality
public static boolean equal(Object tree, Object other) {
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


    public static void main( String[] args )
throws java.io.FileNotFoundException
 {

     // 1. line count

        final Mapper map1 = new Mapper() 
        {
            public void map(String key, String value, MapReduce mr) 
            {
                mr.collect_map("line", list("1")); 
            } 
        };

        final Reducer red1 = new Reducer() 
        {
            public void reduce(String key, Cons value, MapReduce mr) 
            {
                // System.out.println("  red1 key = " + key + "  val = " + value.toString());
                int sum = 0;
                for ( Cons lst = value; lst != null; lst = rest(lst) )
                    sum += Integer.decode((String) first((Cons)
                                                         first(lst)));
                mr.collect_reduce(key, new Integer(sum)); 
            } 
        };

        MapReduce mr1 = new MapReduce(map1, red1);

        Cons res1 = mr1.mapreduce("decind.txt", "");
        
        System.out.println("Result = " + res1.toString());
   

    // ****** your code starts here ******
        // you should fill in the map and reduce functions for each task.

     // 2. letter count, case-insensitive

        final Mapper map2 = new Mapper() 
        {
            public void map(String key, String value, MapReduce mr) 
            {
//              System.out.println("   map2 key = " + key + "  val = " + value);
            	
            	value = value.toLowerCase();
            	for (int i = 0; i < value.length(); i++)
            	{
            		char ch = value.charAt(i);
            		int letter = (int) ch;
            		
            		if (letter >= 97 && letter <= 122)
            		mr.collect_map("" + ch, list(1) );	
            	}
            } 
        };

        final Reducer red2 = new Reducer() 
        {
            public void reduce(String key, Cons value, MapReduce mr) 
            {
                // System.out.println("   red2 key = " + key + "  val = " + value.toString());
            	int sum = 0;
            	while (value!=null)
            	{
            		Cons temp = (Cons) first(value);
            		if (temp!=null)
            			sum++;
            		value = rest(value);
            	}
            	
            	mr.collect_reduce(key,sum);
            } 
        };

        MapReduce mr2 = new MapReduce(map2, red2);

        Cons res2 = mr2.mapreduce("decind.txt", "");
        
        System.out.println("Result = " + res2.toString());

     // 3. word count, case-insensitive

        final Mapper map3 = new Mapper() 
        {
            public void map(String key, String value, MapReduce mr) 
            {
                // System.out.println("   map3 key = " + key + "  val = " + value);
            	value = value.toLowerCase();
            	value = value.trim();
            	String[] array = value.split("[^A-Za-z']+");
            	if (value.length() != 0)
            	{
	            	for (int i = 0; i< array.length; i++)
	            	{
	            		mr.collect_map(array[i], list("1"));
	            	}
            	}
            } 
        };

        final Reducer red3 = new Reducer() 
        {
            public void reduce(String key, Cons value, MapReduce mr) 
            {
                // System.out.println("   red3 key = " + key + "  val = " + value.toString());
            	int sum = 0;
            	while (value!=null)
            	{
            		Cons temp = (Cons) first(value);
            		if (temp!=null)
            			sum++;
            		value = rest(value);
            	}
            	mr.collect_reduce(key,sum);
            } 
        };

        MapReduce mr3 = new MapReduce(map3, red3);

        Cons res3 = mr3.mapreduce("file1.txt", "");
        
        System.out.println("Result = " + res3.toString());

        MapReduce mr3b = new MapReduce(map3, red3);

        Cons res3b = mr3b.mapreduce("decind.txt", "");

        for ( Cons lst = res3b; lst != null; lst = rest(lst) )
        System.out.println("   " + ((Cons)first(lst)).toString());
   
        // 4. grep

        final Mapper map4 = new Mapper() 
        {
            public void map(String key, String value, MapReduce mr) 
            {
                //  System.out.println("   map4 key = " + key + "  val = " + value);
                String grepkey = mr.parameter();
                if (value.indexOf(grepkey) != -1)
                	mr.collect_map(grepkey, list(key));
                // System.out.println("   grepkey = " + grepkey);
            } 
        };
                        
        final Reducer red4 = new Reducer() 
        {
            public void reduce(String key, Cons value, MapReduce mr) 
            {
                // System.out.println("   red4 key = " + key + "  val = " + value.toString());
            	Cons result = null;
            	while (value!=null)
            	{
            		Cons temp = (Cons) first(value);
            		String line = (String) first(temp);
            		result = cons(line,result);
            		value = rest(value);
            	}
            	
            	mr.collect_reduce(key,nreverse(result));
            } 
        };

        MapReduce mr4 = new MapReduce(map4, red4);

        Cons res4 = mr4.mapreduce("decind.txt", "ther");
        
        System.out.println("Result = " + res4.toString());

        // 5. index, case-insensitive

        final Mapper map5 = new Mapper() 
        {
            public void map(String key, String value, MapReduce mr) 
            {
                // System.out.println("   map5 key = " + key + "  val = " + value);
            	value = value.toLowerCase();
            	value = value.trim();
            	String[] array = value.split("[^A-Za-z']+");
            	if (value.length()!=0)
            	{
	            	for (int i = 0; i<array.length; i++)
	            	{
	            		mr.collect_map(array[i],list(key));
	            	}
            	}
            } 
        };

        final Reducer red5 = new Reducer() 
        {
            public void reduce(String key, Cons value, MapReduce mr) 
            {
                // System.out.println("   red5 key = " + key + "  val = " + value.toString());
            	Cons result = null;
            	while (value!=null)
            	{
            		Cons temp = (Cons) first(value);
            		String line = (String) first(temp);
            		result = cons(line,result);
            		value = rest(value);
            	}
            	
            	mr.collect_reduce(key,nreverse(result));
            } 
        };

        MapReduce mr5 = new MapReduce(map5, red5);

        Cons res5 = mr5.mapreduce("decind.txt", "");

        for ( Cons lst = res5; lst != null; lst = rest(lst) )
        System.out.println("   " + ((Cons)first(lst)).toString());

     // 6. movie reviews

        final Mapper map6 = new Mapper() 
        {
            public void map(String key, String value, MapReduce mr) 
            {
//                 System.out.println("   map6 key = " + key + "  val = " + value);
                 String[] array = value.split(",");

                 mr.collect_map(array[0], list(array[2]));
            } 
        };

        final Reducer red6 = new Reducer() 
        {
            public void reduce(String key, Cons value, MapReduce mr) 
            {
                // System.out.println("   red6 key = " + key + "  val = " + value.toString());
            	Integer count = 0;
            	Double sum = 0.0;
            	Double average = 0.0;
            	Cons result = null;
            	
            	while (value!=null)
            	{
            		Cons temp = (Cons) first(value);
            		Integer rating = Integer.parseInt((String)first(temp));
            		if (temp!=null)
            			count++;
            		sum += rating;
            		value = rest(value);
            	}
            	average = sum / count;
            	result = cons(list(average, count),result);
            	mr.collect_reduce(key,result);
            } 
        };

        MapReduce mr6 = new MapReduce(map6, red6);

        Cons res6 = mr6.mapreduce("movies.txt", "");
        
        for ( Cons lst = res6; lst != null; lst = rest(lst) )
        System.out.println("   " + ((Cons)first(lst)).toString());

    // ****** your code ends here ******


    }

}
