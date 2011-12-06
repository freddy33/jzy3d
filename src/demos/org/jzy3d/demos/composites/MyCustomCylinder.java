package org.jzy3d.demos.composites;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractComposite;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Quad;
import org.jzy3d.plot3d.text.drawable.DrawableTextBitmap;


public class MyCustomCylinder extends AbstractComposite{
	public void setData(Coord3d position, float height, float radius, int slices, int rings, Color color){
		// Create sides
		top = new Polygon();
		
		for(int i=0; i<slices; i++){
			float angleBorder1 = (float)i*2*(float)Math.PI/(float)slices;
			float angleBorder2 = (float)(i+1)*2*(float)Math.PI/(float)slices;
			
			Coord2d border1 = new Coord2d(angleBorder1, radius).cartesian();
			Coord2d border2 = new Coord2d(angleBorder2, radius).cartesian();
			
			Quad face = new Quad();
			face.add(new Point(new Coord3d(position.x+border1.x, position.y+border1.y, position.z)));
			face.add(new Point(new Coord3d(position.x+border1.x, position.y+border1.y, position.z+height)));
			face.add(new Point(new Coord3d(position.x+border2.x, position.y+border2.y, position.z+height)));
			face.add(new Point(new Coord3d(position.x+border2.x, position.y+border2.y, position.z)));
			face.setColor(color);
			face.setWireframeDisplayed(false);
			
			// add the polygon to the cylinder
			add(face);
			
			// add face label
			//TODO: seems that the text bitmap drawable has a bad getBounds function, since result from transparency computation
			// is wrong, and since the box imply strange square-ification of the scene
			Coord3d labelP = new Coord3d(position.x+(border1.x+border2.x)/2, position.y+(border1.y+border2.y)/2, position.z+height/2);
			DrawableTextBitmap txt3d = new DrawableTextBitmap("Face "+(i+1), labelP, Color.BLACK);
			add(txt3d);
			
			// compute faces
			top.add(new Point(new Coord3d(position.x+border1.x, position.y+border1.y, position.z+height)));
		}
		top.setColor(Color.GREEN);
		add(top);
	}
	
	private Polygon top;
	

}
