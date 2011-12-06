package org.jzy3d.demos.multiview2;


import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.CanvasSwing;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;


public class MultipleCanvasSplitPaneDemo {
	public static void main(String[] args) throws IOException{
		// Create a scene
		Scene scene = new Scene(true);


		// First window, will hold the mouse source
		CanvasSwing cawt1 = new CanvasSwing(scene, Quality.Advanced);
//		openFrame( cawt1,  new Rectangle(0,0,400,400) );
		cawt1.getView().setBackgroundColor(Color.WHITE);
		
		// Second window, with other view settings
		CanvasSwing cawt2 = new CanvasSwing(scene, Quality.Intermediate); // don't want alpha
		scene.newView(cawt2, Quality.Advanced);
//		openFrame( cawt2,  new Rectangle(400,0,400,400) );
		cawt2.getView().setBackgroundColor(Color.WHITE);
		//cawt2.getView().setViewMode(CameraMode.PROFILE);
		cawt2.getView().setViewPositionMode(ViewPositionMode.TOP);
		//cawt2.getView().setMaximized(true);
		
//		View.STRETCH_RATIO = 2;

        SplitViewPanel sP = new SplitViewPanel(cawt1, cawt2);
        openFrame(sP, new Rectangle(0,0,400,400));
		
		// The gl viewport debug grid
		//cawt2.getView().getCamera().setScreenGridDisplayed(true);
		
		
		// This mouse controller supports several targets, but only one mouse source		
		ViewMouseControllerSwing mouse = new ViewMouseControllerSwing();
		mouse.addTarget(cawt1.getView());
		mouse.addTarget(cawt2.getView());
		mouse.addMouseSource(cawt1);		

		// Surface must be added once all canvases have been initialized
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		Range range = new Range(-150,150);
		int steps   = 50;
		scene.add( getSurface(mapper, range, steps) ); 
		
	}
	
	public static Shape getSurface(Mapper mapper, Range range, int steps){
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);		
		
		return surface;
	}
	
	public static void openFrame(CanvasSwing canvas, Rectangle rect){
		final Frame frame1 = new Frame();
		frame1.add(canvas);
		frame1.pack();
		frame1.setBounds(rect);
		frame1.setVisible(true);
		
		frame1.addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				//frame1.dispose();
			}
		});
	}

    public static void openFrame(SplitViewPanel sPanel, Rectangle rect){
		final Frame frame1 = new Frame();
		frame1.add(sPanel);
		frame1.pack();
		frame1.setBounds(rect);
		frame1.setVisible(true);

		frame1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
				//frame1.dispose();
			}
		});
	}
}
