package org.jzy3d.plot3d.rendering.canvas;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;

import org.jzy3d.plot3d.pipelines.NotImplementedException;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.Renderer3d;
import org.jzy3d.plot3d.rendering.view.View;

// Still not working
// try createGLPbufferDrawable


// http://today.java.net/pub/a/today/2008/10/30/integrating-glpbuffer-and-graphics2d.html
public class OffscreenCanvas implements ICanvas {
	/** Initialize a Canvas3d attached to a {@link Scene}, with a given rendering {@link Quality}.*/
	public OffscreenCanvas(Scene scene, Quality quality, GLProfile profile){
		// TODO: find alternative
		//if (!GLDrawableFactory.getFactory(profile).canCreateGLPbuffer(WHAT?))
		//	throw new RuntimeException("Could not create GLPBuffer");
		
		
		
		view     = scene.newView(this, quality);
		renderer = new Renderer3d(view);

		GLCapabilities caps = org.jzy3d.global.Settings.getInstance().getGLCapabilities();
		caps.setDoubleBuffered(false);
		glpBuffer = GLDrawableFactory.getFactory(profile).createGLPbuffer(null, caps, null, 600,600, null);
		
		
		
		glpBuffer.addGLEventListener(renderer);
		glContext =  glpBuffer.createContext(null); 
		//glpBuffer.setSize(800, 600);
		glContext.makeCurrent();
		glpBuffer.createContext(glContext);
	}
		
	public GLPbuffer getGlpBuffer() {
		return glpBuffer;
	}

	public GLContext getGlContext() {
		return glContext;
	}

	@Override
	public void dispose() {
		glContext.destroy();
		glpBuffer.destroy();
	}
	
	@Override
	public void forceRepaint() {
		glpBuffer.display();
	}
	
	@Override
	public BufferedImage screenshot() {
		renderer.nextDisplayUpdateScreenshot();
		glpBuffer.display();
		return renderer.getLastScreenshot();
	}
	
	/*********************************************************/
	
	/** Provide a reference to the View that renders into this canvas.*/
	public View getView(){
		return view;
	}
	
	/** Provide the actual renderer width for the open gl camera settings, 
	 * which is obtained after a resize event.*/
	public int getRendererWidth(){
		return (renderer!=null?renderer.getWidth():0);
	}
	
	/** Provide the actual renderer height for the open gl camera settings, 
	 * which is obtained after a resize event.*/
	public int getRendererHeight(){
		return (renderer!=null?renderer.getHeight():0);
	}
	
	public void removeKeyListener(KeyListener listener) {throw new NotImplementedException();}
	public void removeMouseListener(MouseListener listener) {throw new NotImplementedException();}
	public void removeMouseMotionListener(MouseMotionListener listener) {throw new NotImplementedException();}
	public void removeMouseWheelListener(MouseWheelListener listener) {throw new NotImplementedException();}
	public void addKeyListener(KeyListener listener) {throw new NotImplementedException();}
	public void addMouseListener(MouseListener listener) {throw new NotImplementedException();}
	public void addMouseMotionListener(MouseMotionListener listener) {throw new NotImplementedException();}
	public void addMouseWheelListener(MouseWheelListener listener) {throw new NotImplementedException();}
	
	/*********************************************************/
	
	private View       view;
	private Renderer3d renderer;
	private GLPbuffer  glpBuffer;
	private GLContext  glContext;
}
