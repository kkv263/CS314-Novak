// libtest.java      GSN    03 Oct 08; 21 Feb 12; 26 Dec 13
// 

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.omg.CORBA.Current;

interface Functor { Object fn(Object x); }

interface Predicate { boolean pred(Object x); }

@SuppressWarnings("unchecked")
public class libtest {

    // ****** your code starts here ******


public static Integer sumlist(LinkedList<Integer> lst) 
{
	int sum = 0;
	ListIterator<Integer> iterator = lst.listIterator();
	while (iterator.hasNext())
	{
		sum += iterator.next();
	}
	return sum;
}

public static Integer sumarrlist(ArrayList<Integer> lst) 
{
	int sum = 0;
	int pos = 0;
	while (pos < lst.size())
	{
		sum += lst.get(pos);
		pos++;
	}
	return sum;
}

public static LinkedList<Object> subset (Predicate p,
                                          LinkedList<Object> lst) 
{
	LinkedList<Object> result = new LinkedList<Object>();
	ListIterator<Object> iterator = lst.listIterator();
	Object current = null;
	while (iterator.hasNext())
	{
		current = iterator.next();
		if (p.pred(current))
				result.addLast(current);
	}
	return result;
}

public static LinkedList<Object> dsubset (Predicate p,
                                           LinkedList<Object> lst) 
{
	ListIterator<Object> iterator = lst.listIterator();
	while (iterator.hasNext())
	{
		if (!p.pred(iterator.next()))
		iterator.remove();
	}
	return lst;
}

public static Object some (Predicate p, LinkedList<Object> lst) 
{
	ListIterator<Object> iterator = lst.listIterator();
	Object current = null;
	while (iterator.hasNext())
	{
		current = iterator.next();
		if (p.pred(current))
		{
		break;
		}
	}
	return current;
}

public static LinkedList<Object> mapcar (Functor f, LinkedList<Object> lst) 
{
	LinkedList<Object> result = new LinkedList<Object>();
	ListIterator<Object> iterator = lst.listIterator();
	Object current = null;
	
	while (iterator.hasNext())
	{
		current = iterator.next();
		result.addLast(f.fn(current));
	}
	return result;
}

public static LinkedList<Object> merge (LinkedList<Object> lsta,
                                        LinkedList<Object> lstb) 
{
	LinkedList<Object> result = new LinkedList<Object>();
	
	int aSpot = 0;
	int bSpot = 0;
	
	while (aSpot < lsta.size() && bSpot < lstb.size())
	{
		if((((Integer) lsta.get(aSpot)).compareTo
				((Integer)lstb.get(bSpot)) <= 0))
		{
			result.addLast(lsta.get(aSpot));
			aSpot++;
		}
		else if((((Integer) lsta.get(aSpot)).compareTo
				((Integer)lstb.get(bSpot)) >= 0))
		{
			result.addLast(lstb.get(bSpot));
			bSpot++;
		}
	}
	
		while(aSpot < lsta.size())
		{
			result.addLast(lsta.get(aSpot));
			aSpot++;
		}
		
		while(bSpot < lstb.size())
		{
			result.addLast(lstb.get(bSpot));
			bSpot++;
		}
		return result;

}

public static LinkedList<Object> sort (LinkedList<Object> lst) 
{
	LinkedList<Object> leftHalf = new LinkedList<Object>();
	LinkedList<Object> rightHalf = new LinkedList<Object>();
	int middle = lst.size() / 2;
	int pos = 0;
	
	if (lst.size() < 2)
		return lst;
	
	for (Object obj : lst)
		if (pos < middle)
		{
			leftHalf.addFirst(obj);
			pos ++;
		}
		else
		{
			rightHalf.addFirst(obj);
			pos ++;
		}	
		return merge(sort(leftHalf),sort(rightHalf));
}

public static LinkedList<Object> intersection (LinkedList<Object> lsta,
                                               LinkedList<Object> lstb) 
{
	LinkedList<Object> result = new LinkedList<Object>();
	
	lsta = sort(lsta);
	lstb = sort(lstb);
	
	for (int aPos = 0; aPos < lsta.size(); aPos++)
	{
		for (int bPos = 0; bPos < lstb.size(); bPos++)
		{
			if (lsta.get(aPos).equals((lstb.get(bPos))))
			{
				result.addLast(lsta.get(aPos));
			}
		}
	}
	
	return result;
}

public static LinkedList<Object> reverse (LinkedList<Object> lst) 
{
	LinkedList<Object> result = new LinkedList<Object>();
	ListIterator<Object> iterator = lst.listIterator();

	while (iterator.hasNext())
	{
		result.addFirst(iterator.next());
	}
	return result;
}

    // ****** your code ends here ******

    public static void main(String args[]) {
        LinkedList<Integer> lst = new LinkedList<Integer>();
        lst.add(new Integer(3));
        lst.add(new Integer(17));
        lst.add(new Integer(2));
        lst.add(new Integer(5));
        System.out.println("lst = " + lst);
        System.out.println("sum = " + sumlist(lst));

        ArrayList<Integer> lstb = new ArrayList<Integer>();
        lstb.add(new Integer(3));
        lstb.add(new Integer(17));
        lstb.add(new Integer(2));
        lstb.add(new Integer(5));
        System.out.println("lstb = " + lstb);
        System.out.println("sum = " + sumarrlist(lstb));

        final Predicate myp = new Predicate()
            { public boolean pred (Object x)
                { return ( (Integer) x > 3); }};

        LinkedList<Object> lstc = new LinkedList<Object>();
        lstc.add(new Integer(3));
        lstc.add(new Integer(17));
        lstc.add(new Integer(2));
        lstc.add(new Integer(5));
        System.out.println("lstc = " + lstc);
        System.out.println("subset = " + subset(myp, lstc));

        System.out.println("lstc     = " + lstc);
        System.out.println("dsubset  = " + dsubset(myp, lstc));
        System.out.println("now lstc = " + lstc);

        LinkedList<Object> lstd = new LinkedList<Object>();
        lstd.add(new Integer(3));
        lstd.add(new Integer(17));
        lstd.add(new Integer(2));
        lstd.add(new Integer(5));
        System.out.println("lstd = " + lstd);
        System.out.println("some = " + some(myp, lstd));

        final Functor myf = new Functor()
            { public Integer fn (Object x)
                { return new Integer( (Integer) x + 2); }};

        System.out.println("mapcar = " + mapcar(myf, lstd));

        LinkedList<Object> lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(6));
        lste.add(new Integer(9));
        lste.add(new Integer(11));
        lste.add(new Integer(23));
        System.out.println("lste = " + lste);
        LinkedList<Object> lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        lste = new LinkedList<Object>();
        lste.add(new Integer(1));
        lste.add(new Integer(3));
        lste.add(new Integer(5));
        lste.add(new Integer(7));
        System.out.println("lste = " + lste);
        lstf = new LinkedList<Object>();
        lstf.add(new Integer(2));
        lstf.add(new Integer(3));
        lstf.add(new Integer(6));
        lstf.add(new Integer(6));
        lstf.add(new Integer(7));
        lstf.add(new Integer(10));
        lstf.add(new Integer(12));
        lstf.add(new Integer(17));
        System.out.println("lstf = " + lstf);
        System.out.println("merge = " + merge(lste, lstf));

        LinkedList<Object> lstg = new LinkedList<Object>();
        lstg.add(new Integer(39));
        lstg.add(new Integer(84));
        lstg.add(new Integer(5));
        lstg.add(new Integer(59));
        lstg.add(new Integer(86));
        lstg.add(new Integer(17));
        System.out.println("lstg = " + lstg);

        System.out.println("intersection(lstd, lstg) = "
                           + intersection(lstd, lstg));
        System.out.println("reverse lste = " + reverse(lste));

        System.out.println("sort(lstg) = " + sort(lstg));

   }
}
