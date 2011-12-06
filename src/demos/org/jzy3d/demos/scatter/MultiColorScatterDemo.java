package org.jzy3d.demos.scatter;

import javax.media.opengl.GLCapabilities;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.MultiColorScatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class MultiColorScatterDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new MultiColorScatterDemo());
	}
	
	public MultiColorScatterDemo(){
		// Create the dot cloud scene and fill with data
		int size = 10000;
		float x;
		float y;
		float z;;
		
		Coord3d[] points = new Coord3d[size];
		
		for(int i=0; i<size; i++){
			x = (float)Math.random() - 0.5f;
			y = (float)Math.random() - 0.5f;
			z = (float)Math.random() - 0.5f;
			points[i] = new Coord3d(x, y, z);
		}		

		MultiColorScatter scatter = new MultiColorScatter( points, new ColorMapper( new ColorMapRainbow(), -0.5f, 0.5f ) );
		scatter.setWidth(5);
		
		Quality quality = new Quality(true, true, true, true, true, true, false);
        GLCapabilities capabilities = org.jzy3d.global.Settings.getInstance().getGLCapabilities();
	    capabilities.setSampleBuffers(true);   // false = ragged edges around large points. true = smooth rounding.
	    capabilities.setNumSamples(2);
		
		chart = new Chart(quality, "awt", capabilities);
		chart.getAxeLayout().setMainColor(Color.WHITE);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getScene().add(scatter);
		scatter.setLegend( new ColorbarLegend(scatter, chart.getView().getAxe().getLayout(), Color.WHITE, null) );
		scatter.setLegendDisplayed(true);
	}
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}
