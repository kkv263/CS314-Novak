import java.util.Arrays;

/**
 * this class Cons implements a Lisp-like Cons cell
 * 
 * @author  Gordon S. Novak Jr.
 * @version 29 Nov 01; 25 Aug 08; 05 Sep 08; 08 Sep 08; 12 Sep 08; 24 Sep 08
 *          02 Oct 09; 12 Feb 10; 04 Oct 12; 03 Oct 14; 25 Feb 15
 */

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
    public static void setfirst (Cons x, Object i) { x.car = i; }
    public static void setrest  (Cons x, Cons y) { x.cdr = y; }
   public static Cons list(Object ... elements) {
       Cons list = null;
       for (int i = elements.length-1; i >= 0; i--) {
           list = cons(elements[i], list);
       }
       return list;
   }

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

    public static int square(int x) { return x*x; }

    // iterative destructive merge using compareTo
public static Cons dmerj (Cons x, Cons y) {
  if ( x == null ) return y;
   else if ( y == null ) return x;
   else { Cons front = x;
          if ( ((Comparable) first(x)).compareTo(first(y)) < 0)
             x = rest(x);
            else { front = y;
                   y = rest(y); };
          Cons end = front;
          while ( x != null )
            { if ( y == null ||
                   ((Comparable) first(x)).compareTo(first(y)) < 0)
                 { setrest(end, x);
                   x = rest(x); }
               else { setrest(end, y);
                      y = rest(y); };
              end = rest(end); }
          setrest(end, y);
          return front; } }

public static Cons midpoint (Cons lst) {
  Cons current = lst;
  Cons prev = current;
  while ( lst != null && rest(lst) != null) {
    lst = rest(rest(lst));
    prev = current;
    current = rest(current); };
  return prev; }

    // Destructive merge sort of a linked list, Ascending order.
    // Assumes that each list element implements the Comparable interface.
    // This function will rearrange the order (but not location)
    // of list elements.  Therefore, you must save the result of
    // this function as the pointer to the new head of the list, e.g.
    //    mylist = llmergesort(mylist);
public static Cons llmergesort (Cons lst) {
  if ( lst == null || rest(lst) == null)
     return lst;
   else { Cons mid = midpoint(lst);
          Cons half = rest(mid);
          setrest(mid, null);
          return dmerj( llmergesort(lst),
                        llmergesort(half)); } }


    // ****** your code starts here ******
    // add other functions as you wish.

public static Cons reverse (Cons lst)
{
	Cons answer = null;
	for (; lst!=null; lst = rest(lst))
		answer = cons(first(lst), answer);
	return answer;
}

public static Cons member(Object item, Cons lst)
{
	if (lst == null)
		return null;
	else if (item.equals(first(lst)))
		return lst;
	else
		return member(item, rest(lst));
}

public static Cons union (Cons x, Cons y) 
{
	return mergeunion(x,y);
}

    // following is a helper function for union
public static Cons mergeunion (Cons x, Cons y) 
{
	if (x == null || y == null)
		return y;
	if (member(first(x),y) == null)
		return mergeunion(rest(x),cons(first(x),y));
	else
	return mergeunion(rest(x),y);
}

public static Cons setDifference (Cons x, Cons y) 
{
	return mergediff(x,y);
}

    // following is a helper function for setDifference
public static Cons mergediff (Cons x, Cons y) 
{
	if (x == null)
		return null;
	if (member(first(x),y) == null)
		return cons(first(x),mergediff(rest(x),y));
	else
	return mergediff(rest(x),y);
}


public static Cons bank(Cons accounts, Cons updates) 
{
	Account account, update;
	updates = llmergesort(updates);
	Cons result = list();
	while (updates != null)
	{
		int total = 0;
		account = (Account) first(accounts);
		update = (Account) first(updates);
		
		if (!account.name().equals(update.name()))
		{
			result = cons(new Account(account.name(),account.amount()),result);
			accounts = rest(accounts);
			
			if (!(((Account) first(accounts)).name()).equals(update.name()))
			{
				if (update.amount() > 0)
				{
					System.out.println("No account found for " + update.name() + ". Creating account with ($" + update.amount() + ")");
					result = cons(update,result);
				}
				else
					System.out.println("No account found for " + update.name() + ". Denied creation of account with ($" + update.amount() + ")");
				updates = rest(updates);
			}
		}
		
		if (account.name().equals(update.name()))
		{
			total = account.amount() + update.amount();
				
			if(total < 0)
			{
				total -= 30;
				System.out.println("Overdraft for: " + account.name() + " with ($" + total + ") in account.");				
			}
			accounts = cons(new Account(account.name(),total),rest(accounts));
			updates = rest(updates);
		}
		
	}
	
	while (accounts != null)
	{
		result = cons(first(accounts),result);
		accounts = rest(accounts);
	}
	
	return reverse(result);
}

public static String [] mergearr(String [] x, String [] y) 
{
	String[] result = new String[x.length + y.length];
	
	int rSpot = 0;
	int xSpot = 0;
	int ySpot = 0;
	while (xSpot < x.length && ySpot < y.length)
	{
		if(x[xSpot].compareTo(y[ySpot]) <= 0)
		{
			result[rSpot] = x[xSpot];
			xSpot++;
		}
		else if(x[xSpot].compareTo(y[ySpot]) >= 0)
		{
			result[rSpot] = y[ySpot];
			ySpot++;
		}
		rSpot++;
		
	}
	
	while(xSpot < x.length)
	{
		result[rSpot] = x[xSpot];
		xSpot++;
		rSpot++;
	}
	
	while(ySpot < y.length)
	{
		result[rSpot] = y[ySpot];
		ySpot++;
		rSpot++;
	}
	return result;
}

public static boolean markup(Cons text) 
{
	
	Cons stack = list();
	String current = "";
	String currentText = "";
	int position = 0;
	int depth = 0;
	while (text != null)
	{		
		current = (String) first(stack);	
		currentText = (String) first(text);
		
		if(currentText != "" 
				&& (currentText).charAt(0) == '<')
		{
				if(currentText.charAt(1) == '/')
				{
					if(stack != null &&
							currentText.substring(2).equals(current.substring(1)))
					{
						stack = rest(stack);
						depth --;
					}
					else
					{
						System.out.println("Offending Tag: " + currentText + " at position: "  + position + ", it should be " + first(stack));
						return false;
					}
				}
				else
				{
				stack = cons(currentText, stack);
				depth++;
				}
				if (depth == 100)
				{
				System.out.println("Depth over 100");
				return false;
				}
		}
		
		text = rest(text);
		position++;
	}
	
	if (stack != null)
	{
		position --;
		System.out.println("Offending Tag: " + first(stack) + " at position: "  + position + ", Unbalanced tag / missing closing tag.");
	}

	return true;
}

    // ****** your code ends here ******

    public static void main( String[] args )
      { 
        Cons set1 = list("d", "b", "c", "a");
        Cons set2 = list("f", "d", "b", "g", "h");
        System.out.println("set1 = " + Cons.toString(set1));
        System.out.println("set2 = " + Cons.toString(set2));
        System.out.println("union = " + Cons.toString(union(set1, set2)));

        Cons set3 = list("d", "b", "c", "a");
        Cons set4 = list("f", "d", "b", "g", "h");
        System.out.println("set3 = " + Cons.toString(set3));
        System.out.println("set4 = " + Cons.toString(set4));
        System.out.println("difference = " +
                           Cons.toString(setDifference(set3, set4)));

        Cons accounts = list(
               new Account("Arbiter", new Integer(498)),
               new Account("Flintstone", new Integer(102)),
               new Account("Foonly", new Integer(123)),
               new Account("Kenobi", new Integer(373)),
               new Account("Rubble", new Integer(514)),
               new Account("Tirebiter", new Integer(752)),
               new Account("Vader", new Integer(1024)) );

        Cons updates = list(
               new Account("Foonly", new Integer(100)),
               new Account("Flintstone", new Integer(-10)),
               new Account("Arbiter", new Integer(-600)),
               new Account("Garble", new Integer(-100)),
               new Account("Rabble", new Integer(100)),
               new Account("Flintstone", new Integer(-20)),
               new Account("Foonly", new Integer(10)),
               new Account("Tirebiter", new Integer(-200)),
               new Account("Flintstone", new Integer(10)),
               new Account("Flintstone", new Integer(-120))  );
        System.out.println("accounts = " + accounts.toString());
        System.out.println("updates = " + updates.toString());
        Cons newaccounts = bank(accounts, updates);
        System.out.println("result = " + newaccounts.toString());

        String[] arra = {"a", "big", "dog", "hippo"};
        String[] arrb = {"canary", "cat", "fox", "turtle"};
        String[] resarr = mergearr(arra, arrb);
        for ( int i = 0; i < resarr.length; i++ )
            System.out.println(resarr[i]);

        Cons xmla = list( "<TT>", "foo", "</TT>");
        Cons xmlb = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</TR>", "</TABLE>" );
        Cons xmlc = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "</TR>",
                          "<TR>", "<TD>", "fum", "</TD>", "<TD>",
                          "baz", "</TD>", "</WHAT>", "</TABLE>" );
        Cons xmld = list( "<TABLE>", "<TR>", "<TD>", "foo", "</TD>",
                          "<TD>", "bar", "</TD>", "", "</TR>",
                          "</TABLE>", "</NOW>" );
        Cons xmle = list( "<THIS>", "<CANT>", "<BE>", "foo", "<RIGHT>" );
        Cons xmlf = list( "<CATALOG>",
                          "<CD>",
                          "<TITLE>", "Empire", "Burlesque", "</TITLE>",
                          "<ARTIST>", "Bob", "Dylan", "</ARTIST>",
                          "<COUNTRY>", "USA", "</COUNTRY>",
                          "<COMPANY>", "Columbia", "</COMPANY>",
                          "<PRICE>", "10.90", "</PRICE>",
                          "<YEAR>", "1985", "</YEAR>",
                          "</CD>",
                          "<CD>",
                          "<TITLE>", "Hide", "your", "heart", "</TITLE>",
                          "<ARTIST>", "Bonnie", "Tyler", "</ARTIST>",
                          "<COUNTRY>", "UK", "</COUNTRY>",
                          "<COMPANY>", "CBS", "Records", "</COMPANY>",
                          "<PRICE>", "9.90", "</PRICE>",
                          "<YEAR>", "1988", "</YEAR>",
                          "</CD>", "</CATALOG>");
        System.out.println("xmla = " + xmla.toString());
        System.out.println("result = " + markup(xmla));
        System.out.println("xmlb = " + xmlb.toString());
        System.out.println("result = " + markup(xmlb));
        System.out.println("xmlc = " + xmlc.toString());
        System.out.println("result = " + markup(xmlc));
        System.out.println("xmld = " + xmld.toString());
        System.out.println("result = " + markup(xmld));
        System.out.println("xmle = " + xmle.toString());
        System.out.println("result = " + markup(xmle));
        System.out.println("xmlf = " + xmlf.toString());
        System.out.println("result = " + markup(xmlf));

      }

}
