package org.jzy3d.demos.surface.delaunay;

import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.concrete.SphereScatterGenerator;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class SphericDelaunaySurfaceDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new SphericDelaunaySurfaceDemo());
	}
	
    public SphericDelaunaySurfaceDemo(){        
        List<Coord3d> coords = SphereScatterGenerator.generate(new Coord3d(10,20,30),10000,20, true);
         
        // Create the object to represent the function over the given range.
        final Shape surface = (Shape) Builder.buildDelaunay(coords);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);

        // Create a chart that updates the surface colormapper when scaling changes
        chart = new Chart(Quality.Advanced);
		chart.getScene().getGraph().add(surface);
		
		// TODO: solve bug on chart.getView().setProjection(Camera.PERSPECTIVE);
		
		// Setup a colorbar for the surface object and add it to the scene
		surface.setLegend(new ColorbarLegend(surface, chart.getView().getAxe().getLayout()));

	}

	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
    
	@Override
	public String getPitch() {
		return "Spheric data tesselation with Delaunay";
	}
}
