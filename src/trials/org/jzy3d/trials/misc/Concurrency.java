package org.jzy3d.trials.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Concurrency {
	public static void main(String[] args){ 
		List<String> list = Collections.synchronizedList(new ArrayList<String>());
		list.add("a");
		list.add("b");
		list.add("c");
		
		Iterator<String> i =list.iterator();
		while (i.hasNext()){ 
			Object o = i.next();
			System.out.println(o);
			
			// throws exception
			list.remove(o);     
			// do not throws
			i.remove();
		}     

	}
}
