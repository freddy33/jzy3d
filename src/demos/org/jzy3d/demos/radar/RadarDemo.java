package org.jzy3d.demos.radar;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Disk;


public class RadarDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new RadarDemo());
	}
	
	public RadarDemo(){
		chart = new Chart();
		Disk disk1, disk2;
		disk1 = new Disk();
		disk2 = new Disk(new Coord3d(10,20,30), 0, 50, 10, 10, Color.GREEN);
		disk2.setWireframeColor(Color.RED);
		disk2.setWireframeDisplayed(true);
		chart.getScene().add(disk1);
		chart.getScene().add(disk2);	
	}
	
	public Chart getChart(){
		return chart;
	}
	
	protected Chart chart;
}
