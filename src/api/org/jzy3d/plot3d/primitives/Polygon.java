package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.IMultiColorable;
import org.jzy3d.colors.ISingleColorable;
import org.jzy3d.events.DrawableChangedEvent;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.rendering.view.Camera;



/** 
 * A {@link Polygon} holds a List of {@link Point}s that store a coordinate
 * and a color.
 * <br>
 * There is no setData function for a {@link Polygon} since it is built incrementaly
 * by calls to {@link add(Point)}.
 * <br>
 * The effective color of the {@link Polygon} will be an interpolation between 
 * all {@link Polygon}'s point colors. 
 * The final rendering will thus depend on the selected shading model 
 * for {@link Polygon}s (see {@link Scene.init()}).
 * <br>
 * A {@link Polygon} is not a {@link AbstractComposite}, because:
 * <ul>
 * <li>Its add() and get() methods support {@link Point} only, and no generic {@link AbstractDrawable}.
 * <li>There is no sense delegating the draw method to its components (Point),
 * since the implementation of the polygon's draw function() is based on 
 * OpenGL2 polygon primitives.
 * </ul>
 * 
 * @author Martin Pernollet
 */
public class Polygon extends AbstractWireframeable implements ISingleColorable, IMultiColorable{

	/** Initializes an empty {@link Polygon} with face status defaulting to true,
	 * and wireframe status defaulting to false.*/
	public Polygon(){
		super();
		points = new ArrayList<Point>(4); // use Vector for synchro, or ArrayList for unsyncro.
		bbox   = new BoundingBox3d();	
		center = new Coord3d();
	}
	
	/**********************************************************************/
		
	public void draw(GL2 gl, GLU glu, Camera cam){
		// Execute transformation
		if(transform!=null)
			transform.execute(gl);
				
		// Draw content of polygon
		if(facestatus){
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
			if(wfstatus){
				gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
				gl.glPolygonOffset(1.0f, 1.0f);
			}
			gl.glBegin(GL2.GL_POLYGON);
						
			for(Point p: points){
				if(mapper!=null){
					Color c = mapper.getColor(p.xyz); // TODO: should store result in the point color
					gl.glColor4f(c.r, c.g, c.b, c.a);
					//System.out.println(c);
				}
				else{
					gl.glColor4f(p.rgb.r, p.rgb.g, p.rgb.b, p.rgb.a);
					//System.out.println(p.rgb);
				}
				
				gl.glVertex3f(p.xyz.x, p.xyz.y, p.xyz.z);
			}
			gl.glEnd();
			if(wfstatus)
				gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
		}
		
		// Draw edge of polygon
		if(wfstatus){
			gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
			
			gl.glEnable(GL2.GL_POLYGON_OFFSET_FILL);
			gl.glPolygonOffset(1.0f, 1.0f);
			
			gl.glColor4f(wfcolor.r, wfcolor.g, wfcolor.b, 1);//wfcolor.a);
			gl.glLineWidth(wfwidth);

			gl.glBegin(GL2.GL_POLYGON);
			for(Point p: points){
				gl.glVertex3f(p.xyz.x, p.xyz.y, p.xyz.z);
			}
			gl.glEnd();
			
			gl.glDisable(GL2.GL_POLYGON_OFFSET_FILL);
		}
		
		/*// Drawbarycenter
			Point b = new Point(getBarycentre(), Color.BLUE);
			b.setWidth(5);
			b.draw(gl,glu,cam);
		*/
			
	}
	
	/**********************************************************************/
	
	/** Add a point to the polygon.*/
	public void add(Point point){
		/*if(point.rgb.a < 1f)
			hasAlpha = true;*/
		points.add(point);
		bbox.add(point);
		
		// recompute center
		center = new Coord3d();
		for(Point p: points)
			center = center.add(p.xyz);
		center = center.div(points.size());
	}
	
	/*//--- experimental code ------
	public boolean hasAlpha(){
		return hasAlpha;
	}
	private boolean hasAlpha = false;
	//----------------------------*/
	
	@Override
	public Coord3d getBarycentre(){
		return center;
	}
	
	/** Retrieve a point from the {@link Polygon}.
	 * @return a Point3d. 
	 */
	public Point get(int p){
		return points.get(p);
	}
	
	public List<Point> getAll(){
		return points;
	}
	
	/** Indicates the number of points in this {@link Polygon}.
	 * @return the number of points
	 */	
	public int size(){
		return points.size();
	}
	
	public double getDistance(Camera camera){
		return getBarycentre().distance(camera.getEye());
	}
	
	public double getShortestDistance(Camera camera){
		double min = Float.MAX_VALUE;
		double dist = 0;
		for(Point point: points){
			dist = point.getDistance(camera);
			if(dist < min)
				min = dist;
		}
		
		dist = getBarycentre().distance(camera.getEye());
		if(dist < min)
			min = dist;
		return min;
	}
	
	public double getLongestDistance(Camera camera){
		double max = 0;
		double dist = 0;
		for(Point point: points){
			dist = point.getDistance(camera);
			if(dist > max)
				max = dist;
		}
		return max;
	}
	
	/**********************************************************************/

	public void setColorMapper(ColorMapper mapper){
		this.mapper = mapper;
		
		fireDrawableChanged(new DrawableChangedEvent(this, DrawableChangedEvent.FIELD_COLOR));
	}
	
	public ColorMapper getColorMapper(){
		return mapper;
	}
	
	/*public void setColors(ColorMapper mapper){
		for(Point p: points)
			p.setColor(mapper.getColor(p.xyz));	
	}*/
	
	public void setColor(Color color){
		this.color = color;
		
		for(Point p: points)
			p.setColor(color);
		
		fireDrawableChanged(new DrawableChangedEvent(this, DrawableChangedEvent.FIELD_COLOR));
	}
	
	public Color getColor(){
		return color;
	}
	
	/**********************************************************************/
	
	public String toString(int depth){
		return (Utils.blanks(depth) + "(Polygon) #points:" + points.size());
	}
	
	/**********************************************************************/
	protected ColorMapper mapper;
	protected List<Point> points;
	protected Color color;
	protected Coord3d center;
}
