package org.jzy3d.demos.scatter;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;


public class ScatterDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new ScatterDemo());
	}
	
	public ScatterDemo(){
		// Create the dot cloud scene and fill with data
		int size = 100000;
		float x;
		float y;
		float z;
		float a;
		
		Coord3d[] points = new Coord3d[size];
		Color[]   colors = new Color[size];
		
		for(int i=0; i<size; i++){
			x = (float)Math.random() - 0.5f;
			y = (float)Math.random() - 0.5f;
			z = (float)Math.random() - 0.5f;
			points[i] = new Coord3d(x, y, z);
			a = 0.25f + (float)(points[i].distance(Coord3d.ORIGIN)/Math.sqrt(1.3)) / 2;
			colors[i] = new Color(x, y, z, a);
		}
		
		Scatter scatter = new Scatter(points, colors);
		chart = new Chart();
		chart.getScene().add(scatter);
		
		//chart.getView().setViewMode( CameraMode.TOP );
	}

	public Chart getChart(){
		return chart;
	}
	
	protected Chart chart;
}
