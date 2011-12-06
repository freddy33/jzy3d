package org.jzy3d.demos.surface.delaunay;

import java.io.IOException;
import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.io.FileDataset;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.primitives.Shape;


/**
 * Shows how bad Delaunay works for some datasets. Actual input points can be visualized
 * separately using a scatter plot.
 * @author Martin Pernollet
 */
public class BadDelaunayResult extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new BadDelaunayResult());
	}
	
    public BadDelaunayResult() throws IOException{
    	List<Coord3d> coords = FileDataset.loadList("./data/plotdata.txt");
    	
        // Create the object to represent the function over the given range.
        final Shape surface = (Shape) Builder.buildDelaunay(coords);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);
        surface.setLegendDisplayed(true); // opens a colorbar on the right part of the display

        // Create a chart that updates the surface colormapper when scaling changes
        chart = new Chart();
        
        // Setup a colorbar for the surface object and add it to the scene
		chart.getScene().getGraph().add(surface);
	}

	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
}
