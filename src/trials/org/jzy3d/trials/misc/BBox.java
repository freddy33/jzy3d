package org.jzy3d.trials.misc;

import org.jzy3d.maths.BoundingBox2d;
import org.jzy3d.maths.Coord2d;

public class BBox {
	public static void test(){
		BoundingBox2d t1 = new BoundingBox2d();
		BoundingBox2d t2 = new BoundingBox2d();
		BoundingBox2d t3 = new BoundingBox2d();
		BoundingBox2d t4 = new BoundingBox2d();
		
		t1.add(new Coord2d(0,0));
		t1.add(new Coord2d(1,0));
		t1.add(new Coord2d(1,1));
		t1.add(new Coord2d(0,1));
		
		t2.add(new Coord2d(0.2,0.2));
		t2.add(new Coord2d(0.8,0.2));
		t2.add(new Coord2d(0.8,0.8));
		t2.add(new Coord2d(0.2,0.8));
		
		t3.add(new Coord2d(0.2,0.2));
		t3.add(new Coord2d(1.8,1.2));
		t3.add(new Coord2d(1.8,1.8));
		t3.add(new Coord2d(1.2,1.8));
		
		t4.add(new Coord2d(2,2));
		t4.add(new Coord2d(3,2));
		t4.add(new Coord2d(3,3));
		t4.add(new Coord2d(2,3));
		
		System.out.println("t1: "+t1);
		System.out.println("t2: "+t2);
		System.out.println("t3: "+t3);
		System.out.println("t4: "+t4);
		
		System.out.println("t1.contains(t2): "+t1.contains(t2));
		System.out.println("t1.contains(t3): "+t1.contains(t3));
		System.out.println("t1.contains(t4): "+t1.contains(t4));
		System.out.println("t1.intersect(t2): "+t1.intersect(t2));
		System.out.println("t1.intersect(t3): "+t1.intersect(t3));
		System.out.println("t1.intersect(t4): "+t1.intersect(t4));
	}
}
