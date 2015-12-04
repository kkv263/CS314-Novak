/**
 * class to simulate MapReduce
 * 
 * @author  Gordon S. Novak Jr.
 * @version 18 Nov 08; 24 Nov 08
 */

import java.util.*;
import java.util.TreeMap;
import java.util.Scanner;
import java.util.Set;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MapReduce
{
    private TreeMap themap;
    private Mapper  mapper;
    private Reducer reducer;
    private Cons    result;
    private String  parameter;

    public MapReduce (Mapper m, Reducer r) {
        mapper = m;
        reducer = r;
        themap = new TreeMap(); }

    public String parameter() { return this.parameter; }

    public Cons mapreduce(String filename, String param)
        throws java.io.FileNotFoundException    {
        this.parameter = param;
        Scanner inputStream = new Scanner(new FileInputStream(filename));
        int lineno = 0;
        Cons intermed = null;
        Cons val = null;
        while ( inputStream.hasNextLine() ) {
            lineno++;
            String line = inputStream.nextLine();
            mapper.map(Integer.toString(lineno), line, this); }
        Set<Map.Entry> intvals = themap.entrySet();
        for (Map.Entry item : intvals )
            reducer.reduce((String) item.getKey(),
                           (Cons) Cons.first((Cons) item.getValue()), this);
        return Cons.nreverse(result); }

    public void collect_map(String key, Cons value) {
        // System.out.println("   coll key = " + key + "  val = " + value.toString());
        Cons ptrs = (Cons) themap.get(key);
        //  if ( ptrs != null ) System.out.println("       ptrs = " + ptrs.toString());
        Cons newptr = Cons.list(value);
        if ( ptrs == null )
            // 2-pointer queue:      ( front   back ) pointers to cons list
            themap.put(key, Cons.list(newptr, newptr) );
        else { 
            Cons.setrest((Cons) Cons.second(ptrs), newptr); // link to back
            Cons.setfirst((Cons) Cons.rest(ptrs), newptr);  // update back
            // System.out.println("  exit ptrs = " + ptrs.toString());
 } }

    public void collect_reduce(String key, Object value) {
        result = Cons.cons(Cons.list(key, value), result); }

}
