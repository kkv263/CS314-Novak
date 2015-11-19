import java.util.HashMap;


public class Memoizer {
	private HashMap<Object, Object> hash;
	private Functor function;

	public Memoizer() 
	{
		
	}

	public Memoizer (Functor function)
	{
		this.hash = new HashMap<Object, Object>();
		this.function = function;
	}
	
	public Object call(Object x)
	{
		if (this.hash.containsKey(x))
			return this.hash.get(x);
		else
		{
			Object value = function.fn(x);
			this.hash.put(x, value);
			return value;
		}
			
	}
}
