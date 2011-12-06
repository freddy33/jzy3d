package org.jzy3d.demos.surface.delaunay;

import static java.lang.Math.E;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class GeneratedDelaunaySurfaceDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new GeneratedDelaunaySurfaceDemo());
	}
	
	public static int NPOINTS = 1000;
	
    public GeneratedDelaunaySurfaceDemo(){        
        List<Coord3d> coords = getSurf();
         
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
    
        
    public List<Coord3d> getSurf(){
    	List<Coord3d> coords = new ArrayList<Coord3d>(NPOINTS);
        Random r = new Random();

        double x;
        double y;
        double z;
        for (int i=0; i<NPOINTS;i++) {
            x = 2.0*r.nextDouble();
            y = 2.0*r.nextDouble();
            double s = r.nextDouble();            

            if (s>0.75) {x*=-1;y*=-1;}
            if (s>0.5 && s<= 0.75) {x *= -1;}
            if (s>0.25 && s<= 0.5) {y *= -1 ;}            

            // exp( -(x**2 + y**2) ) * cos(x/4)*sin(y) * cos(2*(x**2+y**2))
            double f1 = cos(2*(x*x+y*y));
            double f2 = cos(x/4)*sin(y);
            double f3 = x*x + y*y;
            z = pow(E, -f3)*f2*f1;

            coords.add(new Coord3d(x,y,z));
        }
        return coords;
    }

}
