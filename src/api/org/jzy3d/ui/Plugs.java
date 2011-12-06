package org.jzy3d.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import org.jzy3d.bridge.awt.FrameAWT;
import org.jzy3d.bridge.swing.FrameSwing;
import org.jzy3d.chart.Chart;
import org.jzy3d.plot3d.rendering.canvas.CanvasAWT;
import org.jzy3d.plot3d.rendering.canvas.CanvasSwing;


public class Plugs {

	public static void frame(Chart chart){
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		frame(chart, new Rectangle(0,0,screen.width, screen.height), "Jzy3d");
	}
	
	public static void frame(Chart chart, Rectangle bounds, String title){
		Object canvas = chart.getCanvas();
		
		if(canvas instanceof CanvasAWT)
			new FrameAWT(chart, bounds, title); // FrameSWT works as well
		else if(canvas instanceof CanvasSwing)
			new FrameSwing(chart, bounds, title);
		else
			throw new RuntimeException("No default frame could be found for the given Chart canvas: " + canvas.getClass());
	}
}
