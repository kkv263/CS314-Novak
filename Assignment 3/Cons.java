/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 16 Feb 09
 *          01 Feb 12; 08 Feb 12; 22 Sep 13; 26 Dec 13
 */

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

public class Cons
{
    // instance variables
    private Object car;   // traditional name for first
    private Cons cdr;     // "could-er", traditional name for rest
    private Cons(Object first, Cons rest)
       { car = first;
         cdr = rest; }

    // Cons is the data type.
    // cons() is the method that makes a new Cons and puts two pointers in it.
    // cons("a", null) = (a)
    // cons puts a new thing on the front of an existing list.
    // cons("a", list("b","c")) = (a b c)
    public static Cons cons(Object first, Cons rest)
      { return new Cons(first, rest); }

    // consp is true if x is a Cons, false if null or non-Cons Object
    public static boolean consp (Object x)
       { return ( (x != null) && (x instanceof Cons) ); }

    // first returns the first thing in a list.
    // first(list("a", "b", "c")) = "a"
    // safe, first(null) = null
    public static Object first(Cons lst) {
        return ( (lst == null) ? null : lst.car  ); }

    // rest of a list after the first thing.
    // rest(list("a", "b", "c")) = (b c)
    // safe, rest(null) = null
    public static Cons rest(Cons lst) {
      return ( (lst == null) ? null : lst.cdr  ); }

    // second thing in a list
    // second(list("+", "b", "c")) = "b"
    public static Object second (Cons x) { return first(rest(x)); }

    // third thing in a list
    // third(list("+", "b", "c")) = "c"
    public static Object third (Cons x) { return first(rest(rest(x))); }

    // destructively change the first() of a cons to be the specified object
    // setfirst(list("a", "b", "c"), 3) = (3 b c)
    public static void setfirst (Cons x, Object i) { x.car = i; }

    // destructively change the rest() of a cons to be the specified Cons
    // setrest(list("a", "b", "c"), null) = (a)     
    // setrest(list("a", "b", "c"), list("d","e")) = (a d e)
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }

    // make a list of the specified items
    // list("a", "b", "c") = (a b c)
    // list() = null
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }
   
   public static Cons nreverse(Cons lst)
   {
   	Cons last = null; Cons next;
   	while (lst!=null)
   	{
   		next = rest(lst);
   		setrest(lst,last);
   		last = lst;
   		lst = next;
   	}
   	return last;
   }

    // convert a list to a string in parenthesized form for printing
    public String toString() {
       return ( "(" + toStringb(this) ); }
    public static String toString(Cons lst) {
       return ( "(" + toStringb(lst) ); }
    private static String toStringb(Cons lst) {
       return ( (lst == null) ?  ")"
                : ( first(lst) == null ? "()" : first(lst).toString() )
                  + ((rest(lst) == null) ? ")" 
                     : " " + toStringb(rest(lst)) ) ); }

    public static int square(int x) { return x*x; }

    // ****** your code starts here ******


    // add up elements of a list of numbers
public static int sum (Cons lst) 
{
	if (lst == null)
		return 0;
	return (Integer) first(lst) + sum(rest(lst));
}

    // mean = (sum x[i]) / n  
public static double mean (Cons lst) 
{
	double total = 0;
	int n = 0;
	while (lst != null)
	{
		total += (Integer) first(lst);
		lst = rest(lst);
		n++;
	}

	return  total/ n;
}

    // square of the mean = mean(lst)^2  

    // mean square = (sum x[i]^2) / n  
public static double meansq (Cons lst) 
{
	double total = 0;
	int n = 0;
	while (lst != null)
	{
		total += square((Integer) first(lst));
		lst = rest(lst);
		n++;
	}

	return  total/ n;
}

public static double variance (Cons lst) 
{
	
	return meansq(lst) - (mean(lst) * mean(lst));
}

public static double stddev (Cons lst) 
{
	return Math.sqrt(variance(lst));
}

public static double sine (double x)
{
	return sineHelper(x,1,x,x);
}

public static double sineHelper(double x,int n,double total,double prevTerm)
{
	if(2*n-1 > 21)
		return total;
	else
	{
	prevTerm = prevTerm * (-(x*x) /((2*n)*(2*n+1)));
	return sineHelper(x,n+1,total + prevTerm, prevTerm);
	}
}

public static Cons nthcdr (int n, Cons lst) 
{	
	if (n < 1)
		return lst;
	else
		return nthcdr(n-1,rest(lst));
}



public static Object elt (Cons lst, int n) 
{
	Cons lstb = nthcdr(n, lst);

	return (Integer) first(lstb);
}

public static double interpolate (Cons lst, double x) 
{
	
	int i = (int) Math.floor(x);
	int j =(int) Math.ceil(x);
	
	//iV and jV are the values of x and x+1
	Integer iV = (Integer) elt(lst,i);
	Integer jV = (Integer) elt(lst,j);
	
	return iV + ((x - i) * (jV - iV));
}

public static Cons binomial(int n) 
{
	Cons list = list(Integer.valueOf(1));
	return binomialHelper(list,n);
}

public static Cons binomialHelper(Cons lst, int n)
{
	Cons lstb = list(Integer.valueOf(1));
	if (n == 0)
		return lst;
	else
	return binomialHelper(cons(Integer.valueOf(1),binomialHelperRows(lst,lstb)),n-1);
}

public static Cons binomialHelperRows(Cons lst, Cons lstb)
{
	if (rest(lst) == null)
		return lstb;
	else
	return binomialHelperRows(rest(lst),cons((Integer)first(lst)+ (Integer)second(lst), lstb));
}

public static int sumtr (Cons lst) 
{
	return sumtrHelper(lst,0);
}

public static int sumtrHelper(Cons lst, int total)
{
	if (lst == null)
		return total;
	else
		if(!consp(first(lst)))
	        return sumtrHelper(rest(lst),total + (Integer)first(lst));
		if(consp(first(lst)))
	        return sumtrHelper(rest(lst),sumtrHelper((Cons)first(lst),total));
	return sumtrHelper(rest(lst),total);
}

    // use auxiliary functions as you wish.
public static Cons subseq (Cons lst, int start, int end) 
{
	lst = nthcdr(start,lst);
	Cons lstr = list();
	while (start < end)
	{
		lstr = cons(first(lst),lstr);
		start++;
		lst = rest(lst);
	}
	return nreverse(lstr);
}

public static Cons posfilter (Cons lst) 
{
	return nreverse(posfilterHelper(lst,list()));
}

public static Cons posfilterHelper (Cons lst, Cons result)
{
	if (lst == null)
		return result;
	else
		if ((Integer)first(lst) >= 0)
			result = cons(first(lst),result);
	return posfilterHelper(rest(lst),result);
}

public static Cons subset (Predicate p, Cons lst) 
{
	return nreverse(subsetHelper(p,lst,list()));
} 

public static Cons subsetHelper(Predicate p, Cons lst, Cons result)
{
	if (lst == null)
		return result;
	else
		if (p.pred((Integer)first(lst)))
			result = cons(first(lst),result);
	return subsetHelper(p,rest(lst),result);
}

public static Cons mapcar (Functor f, Cons lst) 
{
	return nreverse(mapcarHelper(f,lst,list()));
}

public static Cons mapcarHelper (Functor f, Cons lst, Cons result)
{
	if (lst == null)
		return result;
	else
		result = cons(f.fn((Integer)first(lst)),result);
	return mapcarHelper(f,rest(lst),result);
}

public static Object some (Predicate p, Cons lst) 
{	
	if (p.pred((Integer)first(lst)))
		return first(lst);
	else
		return some(p,rest(lst));
}

public static boolean every (Predicate p, Cons lst) 
{
	if (lst == null)
		return true;
	else
		return (!p.pred((Integer)first(lst))) ? false : every(p, rest(lst));		
}

    // ****** your code ends here ******

    public static void main( String[] args )
      { 
        Cons mylist =
            list(95, 72, 86, 70, 97, 72, 52, 88, 77, 94, 91, 79,
                 61, 77, 99, 70, 91 );
        System.out.println("mylist = " + mylist.toString());
        System.out.println("sum = " + sum(mylist));
        System.out.println("mean = " + mean(mylist));
        System.out.println("meansq = " + meansq(mylist));
        System.out.println("variance = " + variance(mylist));
        System.out.println("stddev = " + stddev(mylist));
        System.out.println("sine(0.5) = " + sine(0.5));  // 0.47942553860420301
        System.out.print("nthcdr 5 = ");
        System.out.println(nthcdr(5, mylist));
        System.out.print("nthcdr 18 = ");
        System.out.println(nthcdr(18, mylist));
        System.out.println("elt 5 = " + elt(mylist,5));

        Cons mylistb = list(0, 30, 56, 78, 96);
        System.out.println("mylistb = " + mylistb.toString());
        System.out.println("interpolate(3.4) = " + interpolate(mylistb, 3.4));
        Cons binom = binomial(12);
        System.out.println("binom = " + binom.toString());
        System.out.println("interpolate(3.4) = " + interpolate(binom, 3.4));

        Cons mylistc = list(1, list(2, 3), list(list(list(list(4)),
                                                     list(5)),
                                                6));
        System.out.println("mylistc = " + mylistc.toString());
        System.out.println("sumtr = " + sumtr(mylistc));
        Cons mylistcc = list(1, list(7, list(list(2), 3)),
                             list(list(list(list(list(list(list(4)))), 9))),
                             list(list(list(list(5), 4), 3)),
                             list(6));
        System.out.println("mylistcc = " + mylistcc.toString());
        System.out.println("sumtr = " + sumtr(mylistcc));

        Cons mylistd = list(0, 1, 2, 3, 4, 5, 6 );
        System.out.println("mylistd = " + mylistd.toString());
        System.out.println("subseq(2 5) = " + subseq(mylistd, 2, 5));

        Cons myliste = list(3, 17, -2, 0, -3, 4, -5, 12 );
        System.out.println("myliste = " + myliste.toString());
        System.out.println("posfilter = " + posfilter(myliste));

        final Predicate myp = new Predicate()
            { public boolean pred (Object x)
                { return ( (Integer) x > 3); }};

        System.out.println("subset = " + subset(myp, myliste).toString());

        final Functor myf = new Functor()
            { public Integer fn (Object x)
                { return  (Integer) x + 2; }};

        System.out.println("mapcar = " + mapcar(myf, mylistd).toString());

        System.out.println("some = " + some(myp, myliste).toString());

        System.out.println("every = " + every(myp, myliste));

      }

}
