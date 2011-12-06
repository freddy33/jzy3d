package org.jzy3d.demos.surface.delaunay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IRunnableDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.delaunay.Triangulation;
import org.jzy3d.plot3d.builder.delaunay.jdt.Delaunay_Triangulation;
import org.jzy3d.plot3d.builder.delaunay.jdt.Point_dt;
import org.jzy3d.plot3d.builder.delaunay.jdt.Triangle_dt;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


/** 
 * Show ability to incrementally build a delaunay surface during runtime.
 * Slowlyness of display is due to a rendering update after each drawn line.
 * 
 * @author Carlo Caputo
 */
public class IncrementalDelaunayDemo extends AbstractDemo implements IRunnableDemo{
	public static void main(String[] args) throws Exception{
		IRunnableDemo demo = new IncrementalDelaunayDemo();
		Launcher.openDemo(demo);
		demo.start();
	}

	public static int MAX_DRAWABLE = 2500;
	
	public IncrementalDelaunayDemo(){
		chart = new Chart(Quality.Advanced);
		surface = new Shape();
		chart.getScene().getGraph().add(surface);
		surface.setLegend(new ColorbarLegend(surface, chart.getView().getAxe().getLayout()));

		triangulation = new Delaunay_Triangulation();
	}

	@Override
	public String getPitch() {
		return "Incrementally build a delaunay surface. " +
				"You must stop the rotation thread before closing the panel " +
				"to avoid the ConcurrentModificationException in your application.";
	}

	@Override
	public Chart getChart() {
		return chart;
	}

	@Override
	public void start() {
		final Random rng = new Random();

		int k = 0;
		while(k<MAX_DRAWABLE){
			// Adding random points
			double a = rng.nextDouble()*2*Math.PI;
			double r = Math.sqrt(rng.nextDouble())*8; // sqrt for uniform distribution
			double x = Math.cos(a)*r;
			double y = Math.sin(a)*r;

			// 1/(1+|u|) + 1/(1+|r|) + (cos(x)+sin(y)), u=sin(atan2(x,y)+r), r=sqrt(x^2+y^2)
			double r0 = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
			double r1 = Math.sqrt(Math.pow(x-2,2)+Math.pow(y+2,2));
			double r2 = Math.sqrt(Math.pow(x-4,2)+Math.pow(y-5,2));
			double r3 = Math.sqrt(Math.pow(x+4,2)+Math.pow(y+2,2));
			double u = Math.sin(Math.atan2(x,y)+r0);
			double z =
				6/(1+Math.abs(u)) // double spiral
				-2/(1+Math.abs(r0)) // hole
				-10/(1+Math.abs(r1))
				+10/(1+Math.abs(r2)) // peak
				+8/(1+Math.abs(r3))
				+.5*(Math.cos(x)+Math.sin(y)) // waves
			;

			triangulation.insertPoint(new Point_dt(x,y,z));
			k++;
			
			List<Polygon> polygons = new ArrayList<Polygon>(triangulation.trianglesSize());

			Iterator<Triangle_dt> trianglesIter;
			trianglesIter = triangulation.trianglesIterator();

			while (trianglesIter.hasNext()) {
				Triangle_dt triangle = trianglesIter.next();

				if (triangle.isHalfplane()) { /* isHalfplane means a degenerated triangle */
					continue;
				}

				Coord3d c1 = triangle.p1().getAsCoord3d();
				Coord3d c2 = triangle.p2().getAsCoord3d();
				Coord3d c3 = triangle.p3().getAsCoord3d();

				Polygon polygon = new Polygon();

				polygon.add(new Point(c1));
				polygon.add(new Point(c2));
				polygon.add(new Point(c3));

				polygons.add(polygon);
			}

			synchronized(surface) { // FIXME http://code.google.com/p/jzy3d/issues/detail?id=20
				surface.clear();
				surface.add(polygons);
			}
			surface.setFaceDisplayed(true);
			surface.setWireframeDisplayed(true);
			surface.setWireframeColor(Color.BLACK);
			surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, .75f)));
			chart.getView().updateBounds();

			try {
				Thread.sleep(10);
			}
			catch (InterruptedException e){
				System.err.println("I have caught an interrupted exception!:" + e.getMessage());
			}
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	protected Chart chart;
	protected Shape surface;
	protected Triangulation triangulation;

}
