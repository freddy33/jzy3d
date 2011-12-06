package org.jzy3d.demos.background;


import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.io.FileImage;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class BackgroundDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new BackgroundDemo(), new Rectangle(0,0,400,400));
	}
	
	public BackgroundDemo() throws IOException{
		// Get a background
		BufferedImage i = FileImage.load("data/bg-demo.jpg");
		
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		Range range = new Range(-150,150);
		int steps   = 50;
		
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		
		// Create a chart
		chart = new Chart(Quality.Advanced);
		chart.getView().setBackgroundImage(i);
		
		// Setup a colorbar for the surface object and add it to the scene
		chart.getScene().getGraph().add(surface);
		ColorbarLegend cbar = new ColorbarLegend(surface, chart.getView().getAxe().getLayout());
		surface.setLegend(cbar);
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
	}
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}
