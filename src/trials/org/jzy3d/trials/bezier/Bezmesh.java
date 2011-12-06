package org.jzy3d.trials.bezier;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.jzy3d.colors.Color;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.rendering.view.Camera;

import com.jogamp.common.nio.Buffers;

/**
 * An adaptation of the redbook Bezier example for Jzy3d.
 * 
 * @see http://www.glprogramming.com/red/chapter12.html
 * @see http://www.glprogramming.com/blue/ch05.html#id5505626
 * @see /net.masagroup.jzy3d/src/glredbook/net/letskit/redbook/first/bezmesh.java
 */
public class Bezmesh extends AbstractDrawable {
	public Bezmesh() {
		bbox = new BoundingBox3d();
	}
	
	public void setData(Coord3d[][] points) {
		setData(points, DEF_STEPS, DEF_STEPS);
	}
	
	public void setData(Coord3d[][] points, int xsteps, int ysteps) {
		setData(points, xsteps, ysteps, points.length-MIN_SIZE, points[0].length-MIN_SIZE);
	}
	
	public void setData(Coord3d[][] points, int xsteps, int ysteps, int xorder, int yorder) {
		setData(points, DEF_START, DEF_STOP, xsteps, xorder, DEF_START, DEF_STOP, ysteps, yorder);
	}
	
	public void setData(Coord3d[][] points, float xmin, float xmax, int xsteps, float ymin, float ymax, int ysteps) {
		setData(points, xmin, xmax, xsteps, points.length-MIN_SIZE, ymin, ymax, ysteps, points.length-MIN_SIZE);
	}
	
	/** Input must be a i*j grid.*/
	public void setData(Coord3d[][] points, float xmin, float xmax, int xsteps, int xorder, float ymin, float ymax, int ysteps, int yorder) {
		if( points.length <= 0 )
			throw new IllegalArgumentException("input array has an unsupported format: input.length=" + points.length);
		if( points[0].length <= 0 )
			throw new IllegalArgumentException("input array has an unsupported format: input[0].length=" + points[0].length);
		
		this.xmin = xmin; 
		this.ymin = ymin; 
		this.xmax = xmax; 
		this.ymax = ymax; 
		this.xstep = (xmax - xmin) / xsteps;
		this.ystep = (ymax - ymin) / ysteps;
		this.xsteps = xsteps; 
		this.ysteps = ysteps;
		this.xorder = xorder;
		this.yorder = yorder;
		this.ni = points.length;
		this.nj = points[0].length;
		
		this.bbox.reset();
		
		// keep points in case we want to display them (optional)
		controlPoints = new float[ni][nj][DIM];
		for (int i = 0; i < ni; i++)
			for (int j = 0; j < nj; j++){
				controlPoints[i][j][0] = points[i][j].x;
				controlPoints[i][j][1] = points[i][j].y;
				controlPoints[i][j][2] = points[i][j].z;

				bbox.add( points[i][j] );
			}

		// prepare a buffer
		controlPointBuffer = Buffers.newDirectFloatBuffer(ni * nj * DIM);
		for (int i = 0; i < ni; i++) {
			for (int j = 0; j < nj; j++) {
				for (int k = 0; k < DIM; k++) {
					controlPointBuffer.put(controlPoints[i][j][k]);
					//System.out.print(controlPoints[i][j][k] + " ");
				}
				//System.out.println();
			}
		}
		controlPointBuffer.rewind();
	}
	
	/************** DRAW *****************/

	public void draw(GL2 gl, GLU glu, Camera cam) {
		if (transform != null)
			transform.execute(gl);
		
		setupEvaluator(gl, xmin, xmax, xsteps, xorder, ymin, ymax, ysteps, yorder);
		evaluate(gl, Color.RED, xmin, xmax, xsteps, ymin, ymax, ysteps);
		
		if(drawControl)
			drawControlPoints(gl, Color.BLUE, 3);
	}
	
	protected void setupEvaluator(GL2 gl, float xmin, float xmax, int xsteps, int xorder, float ymin, float ymax, int ysteps, int yorder){
		gl.glMap2f(GL2.GL_MAP2_VERTEX_3, xmin, xmax, DIM, xorder, ymin, ymax, nj * DIM, yorder, controlPointBuffer);
		gl.glEnable(GL2.GL_MAP2_VERTEX_3);
		//gl.glMapGrid2f(nXSteps, startEvalX, endEvalX, nYSteps, startEvalY, endEvalY);
	}
	
	protected void evaluate(GL2 gl, Color color, float xmin, float xmax, int xsteps, float ymin, float ymax, int ysteps){
		gl.glColor4f(color.r, color.g, color.b, color.a);
		
		for (int j = 0; j <= ysteps; j++) {
			gl.glBegin(GL.GL_LINE_STRIP);
			for (int i = 0; i <= xsteps; i++){
				gl.glEvalCoord2f(xmin + i*xstep, ymin + j*ystep);
			}
			gl.glEnd();
			
			gl.glBegin(GL.GL_LINE_STRIP);
			for (int i = 0; i <= xsteps; i++){
				gl.glEvalCoord2f(ymin + j*ystep, xmin + i*xstep);
			}
			gl.glEnd();
		}
	}
	
	protected void drawControlPoints(GL2 gl, Color color, int width){
		gl.glColor4f(color.r, color.g, color.b, color.a);
		gl.glPointSize(width);
		gl.glBegin(GL.GL_POINTS);
		for(int i=0; i<controlPoints.length; i++)
			for(int j=0; j<controlPoints[i].length; j++)
				gl.glVertex3f(controlPoints[i][j][0], controlPoints[i][j][1], controlPoints[i][j][2]);
		gl.glEnd();
	}
	
	public void toConsole(){
		System.out.println("bezier parameters:");
		System.out.println(" x range: " + xmin + ", " + xmax);
		System.out.println(" y range: " + ymin + ", " + ymax);
		System.out.println(" x steps: " + xsteps);
		System.out.println(" y steps: " + ysteps);
		System.out.println(" x order: " + xorder);
		System.out.println(" y order: " + yorder);
	}
	
	/*******************************/

	protected boolean drawControl = true;
	
	protected float xmin;
	protected float ymin;
	protected float xmax;
	protected float ymax;
	protected float xstep;
	protected float ystep;
	protected int xsteps;
	protected int ysteps;
	protected int xorder;
	protected int yorder;
		
	protected int ni;
	protected int nj;
	protected float controlPoints[][][];
	protected FloatBuffer controlPointBuffer;
	
	protected static final int DIM = 3;
	protected static final float DEF_START = 0f;
	protected static final float DEF_STOP = 1f;
	protected static final int DEF_STEPS = 20;
	protected static final int MIN_SIZE = 0;
}
