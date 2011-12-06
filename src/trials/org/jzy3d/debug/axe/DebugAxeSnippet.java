package org.jzy3d.debug.axe;


import java.awt.Rectangle;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.Scale;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.ui.ChartLauncher;


public class DebugAxeSnippet {
	public static void main(String[] args) throws IOException{
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};

		// Define range and precision for the function to plot
		Range range = new Range(-150,150);
		int steps   = 50;
		
		Chart chart = getChart(mapper, range, steps, Quality.Intermediate, "awt");
		ChartLauncher.openChart(chart, new Rectangle(0,200,800,800), "Surface");
	}
	
	public static Chart getChart(Mapper mapper, Range range, int steps, Quality quality, String type){
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		
		// Create a chart that updates the surface colormapper when scaling changes
		Chart chart = new Chart(quality, type){
			public void setScale(Scale scale){
				super.setScale(scale);
				ColorMapper cm = surface.getColorMapper();
				cm.setScale(scale);
				surface.setColorMapper(cm);
				//surface.setFaceDisplayed(true);
				System.out.println(scale);
			}
		};
		chart.getView().setAxe( new DebugBox(new BoundingBox3d()));
		// Setup a colorbar for the surface object and add it to the scene
		chart.getScene().getGraph().add(surface);
		surface.setLegend(new ColorbarLegend(surface, 
							chart.getView().getAxe().getLayout().getZTickProvider(), 
							chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
		return chart;
	}
}
