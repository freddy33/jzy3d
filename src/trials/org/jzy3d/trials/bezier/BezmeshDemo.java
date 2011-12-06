package org.jzy3d.trials.bezier;

import java.awt.Rectangle;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.ui.ChartLauncher;


public class BezmeshDemo {
	public static void main(String[] args) throws IOException {
		Bezmesh bmesh = new Bezmesh();
		bmesh.setData(getControlPoints());
		bmesh.toConsole();
		
		Chart chart = new Chart();
		chart.getScene().add(bmesh);		
		ChartLauncher.openChart(chart, new Rectangle(0, 200, 600, 400), "Bezier Mesh");
	}
	
	public static Coord3d[][] getControlPoints(){
		Coord3d[][] controls = new Coord3d[4][4];
		
		controls[0] = new Coord3d[4];
		controls[0][0] = new Coord3d(-1.5f, -1.5f,  4.0f);
		controls[0][1] = new Coord3d(-0.5f, -1.5f,  2.0f);
		controls[0][2] = new Coord3d( 0.5f, -1.5f, -1.0f);	
		controls[0][3] = new Coord3d( 1.5f, -1.5f,  2.0f);	
		
		controls[1] = new Coord3d[4];
		controls[1][0] = new Coord3d(-1.5f, -0.5f, 1.0f);
		controls[1][1] = new Coord3d(-0.5f, -0.5f, 3.0f);
		controls[1][2] = new Coord3d( 0.5f, -0.5f, 0.0f);	
		controls[1][3] = new Coord3d( 1.5f, -0.5f, -1.0f);	
		
		controls[2] = new Coord3d[4];
		controls[2][0] = new Coord3d(-1.5f, 0.5f, 4.0f);
		controls[2][1] = new Coord3d(-0.5f, 0.5f, 0.0f);
		controls[2][2] = new Coord3d( 0.5f, 0.5f, 3.0f);	
		controls[2][3] = new Coord3d( 1.5f, 0.5f, 4.0f);
		
		controls[3] = new Coord3d[4];
		controls[3][0] = new Coord3d(-1.5f, 1.5f, -2.5f);
		controls[3][1] = new Coord3d(-0.5f, 1.5f, -2.0f);
		controls[3][2] = new Coord3d( 0.5f, 1.5f, 0.0f);	
		controls[3][3] = new Coord3d( 1.5f, 1.5f, -1.0f);

		/*controls[4] = new Coord3d[4];
		controls[4][0] = new Coord3d(-1.5f, 2.5f, -2.5f);
		controls[4][1] = new Coord3d(-0.5f, 2.5f, -2.0f);
		controls[4][2] = new Coord3d( 0.5f, 2.5f, 0.0f);	
		controls[4][3] = new Coord3d( 1.5f, 2.5f, -1.0f);*/

		return controls;
	}
	
	// not successfull
	public static Coord3d[][] getControlPoints2(){
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		
		Range range = new Range(0,1);
		int steps   = 50;
		
		Coord3d[][] controls = new Coord3d[steps][steps];
		
		for (int i = 0; i < steps; i++) {
			for (int j = 0; j < steps; j++) {
				double x = range.getMin() + i*range.getRange()/steps;
				double y = range.getMin() + j*range.getRange()/steps;
				controls[i][j] = new Coord3d(x, y, mapper.f(x, y));
			}
		}

		return controls;
	}
}
