package org.jzy3d.plot3d.rendering.scene;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.jzy3d.maths.Array;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.plot3d.primitives.AbstractComposite;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.interactive.IInteractive;
import org.jzy3d.plot3d.primitives.interactive.ISelectable;
import org.jzy3d.plot3d.rendering.legends.Legend;
import org.jzy3d.plot3d.rendering.ordering.AbstractOrderingStrategy;
import org.jzy3d.plot3d.rendering.ordering.DefaultOrderingStrategy;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.transform.Transform;

import com.jogamp.common.nio.Buffers;

/**
 * The scene's {@link Graph} basically allows decomposing the list of object
 * into a list of {@link AbstractDrawable}s primitive (i.e. objects that are not 
 * {@link AbstractComposite}.
 * 
 * The list of primitives is ordered using either the provided {@link DefaultOrderingStrategy}
 * or an other specified {@link AbstractOrderingStrategy}. Sorting is usefull for handling transparency
 * properly.
 * 
 * The {@link Graph} maintains a reference to its mother {@link Scene} in order to
 * inform the {@link View}s when its content has change and that repainting is required.
 * 
 * The add() method allows adding a {@link AbstractDrawable} to the scene Graph and updates
 * all views' viewpoint in order to target the center of the scene.
 * 
 * @author Martin Pernollet
 */
public class Graph{
	public Graph(Scene scene){
		this(scene, new DefaultOrderingStrategy(), true);
	}
	
	public Graph(Scene scene, boolean sort){
		this(scene, new DefaultOrderingStrategy(), sort);
	}
	
	public Graph(Scene scene, AbstractOrderingStrategy strategy){
		this(scene, strategy, true);
	}
	
	public Graph(Scene scene, AbstractOrderingStrategy strategy, boolean sort){
		this.scene = scene;
		this.strategy = strategy;
		this.sort = sort;
		components = new ArrayList<AbstractDrawable>();
	}
	
	public void dispose(){
		for(AbstractDrawable c: components)
			if(c!=null)
				c.dispose();
		components.clear();
		scene = null;
	}
	
	/*******************************************************************/

	/** Add a Drawable to the SceneGraph and call all views' so that
	 * they update their bounds according to their mode (automatic or
	 * manual).
	 * @param drawable: The drawable that must be added to the scene graph.
	 * @param update: should be true if you wish to have all the views updated with old bounds including drawable bounds
	 */
	public void add(AbstractDrawable drawable, boolean updateViews){
		components.add(drawable);
		
		if(updateViews)
			for(View view: scene.views)
				view.updateBounds();
	}
	
	public void add(AbstractDrawable drawable){
		add(drawable, true);
	}
	
	public void add(List<? extends AbstractDrawable> drawables, boolean updateViews){
		for(AbstractDrawable d: drawables)
			add(d, false);
		if(updateViews)
			for(View view: scene.views)
				view.updateBounds();
	}
	
	public void add(List<? extends AbstractDrawable> drawables){
		add(drawables, true);
	}
	
	/** Delete a Drawable from the SceneGraph and update all views' viewpoint 
	 * in order to target the center of the scene.
	 * @param drawable The drawable that must be deleted from the scene graph.
	 */
	public boolean remove(AbstractDrawable drawable, boolean updateViews){
		boolean output = components.remove(drawable);		
		BoundingBox3d bbox = getBounds();
		
		for(View view: scene.views){
			view.lookToBox(bbox);
			if(updateViews)
				view.shoot();
		}
		
		return output;
	}
	
	public boolean remove(AbstractDrawable drawable){
		return remove(drawable, true);
	}
	
	public List<AbstractDrawable> getAll(){
		return components;
	}
	
	/****************************************************************/
	
	/** Decompose all {@link AbstractComposite} objects, and sort the extracted monotype 
	 * (i.e. non-{@link AbstractComposite} {@link AbstractDrawable}s) in order to render them according
	 * to the default -or defined- {@link AbstractOrderingStrategy}.
	 */
	public void draw(GL2 gl, GLU glu, Camera camera){
		draw(gl, glu, camera, components, sort);
	}
	
	protected void draw(GL2 gl, GLU glu, Camera camera, List<AbstractDrawable> components, boolean sort){
		if(sort){
			// Expand all composites into a list of monotypes
			ArrayList<AbstractDrawable> monotypes = Decomposition.getDecomposition(components);
			
			// Compute order of monotypes for rendering
			strategy.sort(monotypes, camera);
	
			// Render sorted monotypes
			gl.glMatrixMode(GL2.GL_MODELVIEW);			
			//int k = 0;
			for(AbstractDrawable d: monotypes){
				//System.out.println((k++) + "] shortest=" + d.getShortestDistance(camera) + " longest=" + d.getLongestDistance(camera));
				if(d.isDisplayed())
					d.draw(gl, glu, camera);
			}
		}
		else{
			for(AbstractDrawable d: components)
				if(d.isDisplayed())
					d.draw(gl, glu, camera);
		}
	}
	
	public void select(GL2 gl, GLU glu, int x, int y){
		int capacity = components.size()*4;      //Each object take in maximium : 4 * name stack depth
	    IntBuffer selectBuffer = Buffers.newDirectIntBuffer(capacity);
		gl.glSelectBuffer(selectBuffer.capacity(), selectBuffer);
		
		//Get viewport & projectionmatrix
	    int[] viewport = new int[4]; float[] projection = new float[16];
	    gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);	    
	    gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projection, 0);

	    //Switch to projection transformation
	    gl.glMatrixMode(GL2.GL_PROJECTION);
	    gl.glLoadIdentity();
	    
	    //Restrict region to pick object only in this region
	    glu.gluPickMatrix(x, y, 5, 5, viewport, 0);    //x, y, 5, 5 is the picking area

	    //Load the projection matrix
	    gl.glMultMatrixf(projection, 0);
		
	    gl.glRenderMode(GL2.GL_SELECT);
	    gl.glMatrixMode(GL2.GL_MODELVIEW);
	    int k=0;
		for(AbstractDrawable d: components){
			if( d instanceof ISelectable ){
				gl.glLoadName(k++);
				((ISelectable)d).select(gl, glu);
			}
		}
	
	
		//Switch back to render mode & get the number of hits/records in the select buffer
	    int nbRecords = gl.glRenderMode(GL2.GL_RENDER);
	    if(nbRecords <= 0) {
	    	System.out.println("no selection");
	    	return;
	    }
	    /*
	     * Select buffer
	     * -------------
	     * The select buffer is a list of nbRecords records.
	     * Each records is composed of :
	     * 1st int : depth of the name stack
	     * 2nd int : minimum depth value
	     * 3rd int : maximum depth value
	     * x int(s) : list of name (number is defined in the 1st integer))
	     */
	    int[] object = null;
	
	    int index = 0, depth = 0;
	    for(int i = 0; i < nbRecords; i++) {
	        if(index == 0 || selectBuffer.get(index+2) < depth) {
	            //Current record is closer
	            int stack = selectBuffer.get(index++); //Depth of the name stack
	            depth = selectBuffer.get(index++);     //Min depth
	            index++;                               //Skip max depth
	
	            System.out.println("stack=" + stack + " depth="+depth);
	            object = new int[stack];
	            for(int j = 0; j < stack; j++)
	                object[j] = selectBuffer.get(index++);
	        }
	        else
	        {
	        	System.out.println("skip");
	            //Skip: Record is farther
	            int names = selectBuffer.get(index++);
	            index += 2 + names;
	        }
	    }
	
	    //'object' store the names of the closer object picked
	    //Process what should be done with the selection
	    //processPicked(object);
	    System.out.println("graph select");
	    Array.print(object);
	}
	
	/** Update all interactive {@link AbstractDrawable} projections*/
	public void project(GL2 gl, GLU glu, Camera camera){
		for(AbstractDrawable d: components){
			if( d instanceof IInteractive )
				( (IInteractive)d ).project(gl, glu, camera);
		}
	}
	
	/*****************************************************************/
		
	/** Get the {@link @Drawable} ordering strategy.*/
	public AbstractOrderingStrategy getStrategy() {
		return strategy;
	}

	/** Set the {@link @Drawable} ordering strategy.*/
	public void setStrategy(AbstractOrderingStrategy strategy) {
		this.strategy = strategy;
	}
	
	/** Delegate transforming iteratively to all Drawable of this graph 
	 * and stores the given transform for keeping the ability of retrieving it.*/
	public void setTransform(Transform transform){
		this.transform = transform;
		for(AbstractDrawable c: components){
			if(c!=null)
				c.setTransform(transform);
		}
	}

	/** Return the transform that was affected to this composite.*/
	public Transform getTransform(){
		return transform;
	}	
	
	/** Creates and return a BoundingBox3d that embed all Drawable bounds, among those that
	 * have a defined bounding box.*/
	public BoundingBox3d getBounds(){
		if(components.size()==0)
			return new BoundingBox3d(0,0,0,0,0,0);
		else{
			BoundingBox3d box = new BoundingBox3d();
			
			for(AbstractDrawable c: components){
				if(c!=null && c.getBounds()!=null)
					box.add(c.getBounds());
			}
			return box;
		}
	}
	
	/****************************************************************/
	
	/** Return the list of available {@link AbstractDrawable}'s {@link Legend}.*/
	public List<Legend> getMetaData(){
		List<Legend> list = new ArrayList<Legend>();
		
		for(AbstractDrawable c: components)
			if(c!=null)
				if(c.hasFace() && c.isFace2dDisplayed())
					list.add(c.getFace());
		
		return list;
	}
	
	/** Return true if the {@link Graph} contains at least one {@link AbstractDrawable} that
	 * has {@link Legend} that must be displayed.*/
	public int hasMetaData(){
		int k = 0;
		for(AbstractDrawable c: components)
			if(c!=null)
				if(c.hasFace() && c.isFace2dDisplayed())
					k++;
		return k;
	}
	
	/****************************************************************/
	
	/** Print out information concerning all Drawable of this composite.*/
	public String toString(){
		String output = "(Graph) #elements:"+components.size()+":\n";
		
		int k=0;
		for(AbstractDrawable c: components){
			if(c!=null)
				output += " Graph element ["+ (k++) +"]:" + c.toString(1) + "\n";
			else
				output += " Graph element ["+ (k++) +"] (null)\n";
		}
		return output;
	}
	
	/****************************************************************/
	
	protected List<AbstractDrawable> components;
	protected Scene scene;
	protected Transform transform;
	//protected OrderingStrategy strategy;
	
	protected boolean VERBOSE = false;
	protected AbstractOrderingStrategy strategy;
	protected boolean sort = true;
}
