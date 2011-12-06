package org.jzy3d.picking;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.rendering.view.modes.CameraMode;
import org.jzy3d.plot3d.transform.Scale;
import org.jzy3d.plot3d.transform.Transform;

import com.jogamp.common.nio.Buffers;

/**
 * @see: http://www.opengl.org/resources/faq/technical/selection.htm
 * 
 * @author Martin Pernollet
 *
 */
public class PickingSupport {
	public PickingSupport(){
		this(10);
	}
	
	public PickingSupport(int brushSize){
		this(brushSize, 2048);
	}
	
	public PickingSupport(int brushSize, int bufferSize){
		this.brushSize = brushSize;
		this.bufferSize = bufferSize;
	}

	/*************************/

	public boolean addObjectPickedListener(IObjectPickedListener listener){
		return verticesListener.add(listener);
	}
	
	public boolean removeObjectPickedListener(IObjectPickedListener listener){
		return verticesListener.remove(listener);
	}

	protected void fireObjectPicked(List<? extends Object> v){
		for(IObjectPickedListener listener: verticesListener){
			listener.objectPicked(v);
		}
	}
	
	/*************************/
	
	public void registerPickableObject(IPickable pickable, Object model){
		pickable.setPickingId(pickId++);
		pickables.put(pickable.getPickingId(), pickable);
		pickableTargets.put(pickable, model);
		//System.out.println("register " + pickable);
	}
	
	public void getPickableObject(int id){
		pickables.get(id);
	}
	
	/*************************/
	
	public void pickObjects(GL2 gl, GLU glu, View view, Graph graph, IntegerCoord2d pickPoint) {
        int viewport[] = new int[4];
        int selectBuf[] = new int[bufferSize]; // TODO: move @ construction
        IntBuffer selectBuffer = Buffers.newDirectIntBuffer(bufferSize);
        
        // Prepare selection data
        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);        
        gl.glSelectBuffer(bufferSize, selectBuffer);        
        gl.glRenderMode(GL2.GL_SELECT);         
        gl.glInitNames();
        gl.glPushName(0); 

        // Retrieve view settings
        Camera camera = view.getCamera();
        CameraMode cMode = view.getCameraMode();
        Coord3d viewScaling = view.getLastViewScaling();
        Transform viewTransform = new Transform(new Scale(viewScaling));
        double xpick = (double) pickPoint.x;
        double ypick = (double) pickPoint.y;
        
        // Setup projection matrix
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        {
	        gl.glLoadIdentity();
	        // Setup picking matrix, and update view frustrum
	        glu.gluPickMatrix(xpick, ypick, brushSize, brushSize, viewport, 0);
	        camera.doShoot(gl, glu, cMode);
	        
	        // Draw each pickable element in select buffer
	        gl.glMatrixMode(GL2.GL_MODELVIEW);
	        for(IPickable pickable: pickables.values()){
	        	setCurrentName(gl, pickable);
	        	pickable.setTransform(viewTransform);
	        	pickable.draw(gl, glu, camera);
	        	releaseCurrentName(gl);
	        }
	        // Back to projection matrix
	        gl.glMatrixMode(GL2.GL_PROJECTION);
        }
        gl.glPopMatrix();
        gl.glFlush();
        
        // Process hits
        int hits = gl.glRenderMode(GL2.GL_RENDER);
        selectBuffer.get(selectBuf);
        List<IPickable> picked = processHits(hits, selectBuf);
        
        // Trigger an event
        List<Object> clickedObjects = new ArrayList<Object>(hits);
        for(IPickable pickable: picked){
        	Object vertex = pickableTargets.get(pickable);
        	clickedObjects.add(vertex);
        }
        fireObjectPicked(clickedObjects);
    }
    
    protected void setCurrentName(GL2 gl, IPickable pickable){
    	if(method==0)
    		gl.glLoadName(pickable.getPickingId());
    	else
    		gl.glPushName(pickable.getPickingId());
    } 
    
    protected void releaseCurrentName(GL2 gl){
    	if(method==0)
    		;
    	else
    		gl.glPopName();
    }
    
    protected static int method = 0;
    
    /*********************/
    
    /** Provides the number of picked object by a click. */
    @SuppressWarnings("unused")
	protected List<IPickable> processHits(int hits, int buffer[]) {
        int names, ptr = 0;
        int z1, z2=0;
        
        List<IPickable> picked = new ArrayList<IPickable>();
        
        for (int i = 0; i < hits; i++) { 
            names = buffer[ptr]; ptr++;
            z1 = buffer[ptr]; ptr++;
            z2 = buffer[ptr]; ptr++;

            for (int j = 0; j < names; j++) {
            	int idj = buffer[ptr]; ptr++;
            	if( ! pickables.containsKey(idj) )
            		throw new RuntimeException("internal error: pickable id not found in registry!");
                picked.add( pickables.get(idj) );
            }
        }
        return picked;
    }
    
    /*********************/

    protected static int pickId = 0;
	protected Map<Integer, IPickable> pickables = new HashMap<Integer, IPickable>();
	protected List<IObjectPickedListener> verticesListener = new ArrayList<IObjectPickedListener>(1);
	protected Map<IPickable, Object> pickableTargets = new HashMap<IPickable, Object>();
	protected int brushSize;
	protected int bufferSize;
}
