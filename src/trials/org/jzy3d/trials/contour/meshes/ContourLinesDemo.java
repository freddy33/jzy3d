package org.jzy3d.trials.contour.meshes;


import java.awt.Rectangle;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourMeshGenerator;
import org.jzy3d.factories.JzyFactories;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.primitives.axes.ContourAxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.primitives.contour.ContourMesh;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.ui.ChartLauncher;


public class ContourLinesDemo {
	public static void main(String[] args) throws IOException{
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
//				return 10*Math.sin(x/10)*Math.cos(y/20)*x+ x*y;
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};

		// Define range and precision for the function to plot
		Range xrange = new Range(50,100);   //To get some more detail.
		Range yrange = new Range(50,100);   //To get some more detail.
		int steps   = 50;

		Chart chart = getChart(mapper, xrange, yrange, steps, Quality.Intermediate, "awt");
		ChartLauncher.openChart(chart, new Rectangle(0,0,800,800), "Surface");
		
		// Code to have in an panel:
		//BufferedImage image = contour.getContourImage(new DefaultContourColoringPolicy(myColorMapper), 200, 200, 10);
		//ContourAxeBox.setContourImg(image, xrange, yrange);
		//ChartLauncher.openImagePanel( ((ContourAxeBox)chart.getView().getAxe()).getContourImage(), new Rectangle(800,0,400,400) );
	}

	public static Chart getChart(Mapper mapper, Range xrange, Range yrange, int steps, Quality quality, String type){
		// Create the object to represent the function over the given range.
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
		ColorMapper myColorMapper=new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)); 
		surface.setColorMapper(myColorMapper);
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Compute an image of the contour
		MapperContourMeshGenerator contour = new MapperContourMeshGenerator(mapper, xrange, yrange);
		
		// Create a chart with contour axe box, and attach the contour picture
		JzyFactories.axe = new AxeFactory(){
			@Override
			public IAxe getInstance() {
				return new ContourAxeBox(box);
			}
		};

		Chart chart = new Chart(quality, type);
		ContourAxeBox cab = (ContourAxeBox)chart.getView().getAxe();
		ContourMesh mesh = contour.getContourMesh(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 10, 0, false);
		cab.setContourMesh(mesh);
		
		// Add the surface and its colorbar
		chart.addDrawable(surface);
		surface.setLegend(new ColorbarLegend(surface, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
		
		return chart;
	}
}
