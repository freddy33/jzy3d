package org.jzy3d.demos.contour;


import org.jzy3d.chart.Chart;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.contour.MapperContourPictureGenerator;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.primitives.MultiColorScatter;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class Contour3DDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new Contour3DDemo());
	}

	public Contour3DDemo(){
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};

		// Define range and precision for the function to plot
		Range xrange = new Range(-100,100);   //To get some more detail.
		Range yrange = new Range(-100,100);   //To get some more detail.
		
		// Compute an image of the contour
		MapperContourPictureGenerator contour = new MapperContourPictureGenerator(mapper, xrange, yrange);
		int nPoints= 1000;
		double[][] contours= contour.getContourMatrix(nPoints, nPoints, 40);
		
		// Create the dot cloud scene and fill with data
		int size = nPoints*nPoints;
		Coord3d[] points = new Coord3d[size];
		
		for (int x = 0; x < nPoints; x++)
			for (int y = 0; y < nPoints; y++){
				if (contours[x][y]>-Double.MAX_VALUE){ // Non contours points are -Double.MAX_VALUE and are not painted
					points[x*nPoints+y] = new Coord3d((float)x,(float)y,(float)contours[x][y]);									
				}
				else
					points[x*nPoints+y] = new Coord3d((float)x,(float)y,(float)0.0);  									
//				points[x*400+y] = new Coord3d((float)x,(float)y,(float)mapper.f(x, y));				
			}

		chart = new Chart();
		MultiColorScatter scatter = new MultiColorScatter( points, new ColorMapper( new ColorMapRainbow(), -600.0f, 600.0f ) );
		chart.getScene().add(scatter);
		scatter.setLegend( new ColorbarLegend(scatter, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()) );
		scatter.setLegendDisplayed(true);
	}
	
	public Chart getChart(){
		return chart;		
	}
	protected Chart chart;
}
