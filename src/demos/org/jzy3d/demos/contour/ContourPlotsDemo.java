package org.jzy3d.demos.contour;


import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.DefaultContourColoringPolicy;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.factories.JzyFactories;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.primitives.axes.ContourAxeBox;
import org.jzy3d.plot3d.primitives.axes.IAxe;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class ContourPlotsDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new ContourPlotsDemo());
	}

	public ContourPlotsDemo(){
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};

		// Define range and precision for the function to plot
		Range xrange = new Range(50,100);
		Range yrange = new Range(50,100);
		int steps   = 50;
		final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(xrange, steps, yrange, steps), mapper);
		ColorMapper myColorMapper=new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)); 
		surface.setColorMapper(myColorMapper);
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Create a chart with contour axe box, and attach the contour picture
		JzyFactories.axe = new AxeFactory(){
			@Override
			public IAxe getInstance() {
				return new ContourAxeBox(box);
			}
		};
		chart = new Chart(); //TODO: Quality.Advanced contour buggy with axe box 
		ContourAxeBox cab = (ContourAxeBox)chart.getView().getAxe();
		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);
		cab.setContourImg( contour.getContourImage(new DefaultContourColoringPolicy(myColorMapper), 400, 400, 10), xrange, yrange);
		
		// Add the surface and its colorbar
		chart.addDrawable(surface);
		surface.setLegend(new ColorbarLegend(surface, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()));
		surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display
	}
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}
