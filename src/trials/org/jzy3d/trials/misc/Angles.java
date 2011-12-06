package org.jzy3d.trials.misc;

import org.jzy3d.maths.Angle2d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Vector2d;

public class Angles {
	public static void test(){
		Coord2d c0 = new Coord2d(0f,0f);
		
		Coord2d c1 = new Coord2d(1f,0f);
		Coord2d c2 = new Coord2d(1f,1f);
		Coord2d c3 = new Coord2d(0f,1f);
		Coord2d c4 = new Coord2d(-1f,1f);
		Coord2d c5 = new Coord2d(-1f,0f);
		Coord2d c6 = new Coord2d(-1f,-1f);
		Coord2d c7 = new Coord2d(0f,-1f);
		Coord2d c8 = new Coord2d(1f,-1f);
		
		Vector2d v1 = new Vector2d(c0,c1);
		
		System.out.println("Dot prod with c2: " + v1.dot(new Vector2d(c0,c2)) );
		System.out.println("Dot prod with c3: " + v1.dot(new Vector2d(c0,c3)) );
		System.out.println("Dot prod with c4: " + v1.dot(new Vector2d(c0,c4)) );
		System.out.println("Dot prod with c5: " + v1.dot(new Vector2d(c0,c5)) );
		System.out.println("Dot prod with c6: " + v1.dot(new Vector2d(c0,c6)) );
		System.out.println("Dot prod with c7: " + v1.dot(new Vector2d(c0,c7)) );
		System.out.println("Dot prod with c8: " + v1.dot(new Vector2d(c0,c8)) );
		
		
		Angle2d a = new Angle2d(c1, c0, c2);
		System.out.println(a.angle());
		
	}
}
