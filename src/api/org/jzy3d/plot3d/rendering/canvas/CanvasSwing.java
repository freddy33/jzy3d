package org.jzy3d.plot3d.rendering.canvas;

import java.awt.image.BufferedImage;

import javax.media.opengl.GLCapabilitiesImmutable;
import javax.media.opengl.awt.GLJPanel;

import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.Renderer3d;
import org.jzy3d.plot3d.rendering.view.View;

import com.jogamp.opengl.util.Animator;



/**
 * A {@link CanvasSwing} owns an OpenGL {@link Renderer3d} 
 * for handling GL events, and link it to the scene's {@link View}.
 * <p>
 * The {@link CanvasSwing} provides an animator that is stopped 
 * when it disposes.
 * 
 * @author Martin Pernollet
 */
public class CanvasSwing extends GLJPanel implements IScreenCanvas{
    public CanvasSwing(Scene scene, Quality quality){
        this(scene, quality, org.jzy3d.global.Settings.getInstance().getGLCapabilities());
    }
    
	/** Initialize a Canvas3d attached to a {@link Scene}, with a given rendering {@link Quality}.*/
	public CanvasSwing(Scene scene, Quality quality, GLCapabilitiesImmutable glci){
		super(glci);
		
		view     = scene.newView(this, quality);
		renderer = new Renderer3d(view);
		addGLEventListener(renderer);
		setFocusable(true);
		requestFocusInWindow();
	}
	
	public void dispose(){
		renderer.dispose(this);
		renderer = null;
		view = null; 
	}
	
	/*********************************************************/

	/** Force repaint and ensure that GL2 rendering will occur in the GUI thread, wherever the caller stands.*/
	public void forceRepaint(){
		if(true){
			// -- Method1 --
			// Display() is required to use the GLCanvas procedure and to ensure that GL2 rendering occurs in the 
			// GUI thread.
			// Actually it seems to be a bad idea, because this call implies a rendering out of the excepted GL2 thread,
			// which:
			//  - is slower than rendering in GL2 Thread
			//  - throws java.lang.InterruptedException when rendering occurs while closing the window
			display(); 
		}
		else{
			// -- Method2 --
			// Composite.repaint() is required with post/pre rendering, for triggering PostRenderer rendering
			// at each frame (instead of ). The counterpart is that OpenGL2 rendering will occurs in the caller thread
			// and thus in the thread where the shoot() method was invoked (such as AWT if shoot() is triggered
			// by a mouse event.
			repaint(); 
		}
	}
	
	public BufferedImage screenshot(){
		renderer.nextDisplayUpdateScreenshot();
		display();
		return renderer.getLastScreenshot();
	}
	
	/**Calls super.paint(Graphics), i.e. triggers GLEventListener.display(), and then call
	 * all registered {@link Renderer2d}s' paint(Graphics) method
	 */
	/*public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		view.preRender(g2d);
		// triggers renderer.display() -> renderer.render() -> view.render(gl, gl) 
		// it's better using this hierarchy because it allows using the renderer ability
		// such as auto initialization, update of canvas size, etc.
		super.paint(g2d); 
		view.postRender(g2d);
	}*/
		
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
	
	/*********************************************************/
	
	protected View       view;
	protected Renderer3d renderer;
	protected Animator   animator;

	private static final long serialVersionUID = 980088854683562436L;
}
