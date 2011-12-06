package org.jzy3d.demos.scatter;

import java.io.FileReader;
import java.io.IOException;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;

import au.com.bytecode.opencsv.CSVReader;

public class ScatterDemoFromFile extends AbstractDemo{
	public static String FILENAME = "../../data/plotdata.txt";
	
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new ScatterDemoFromFile());
	}
	
	public ScatterDemoFromFile() throws IOException{
		int size = readNLines(FILENAME); // TODO REMOVE ME, USE LIST
		float x;
		float y;
		float z;
		float a;
		
		Coord3d[] points = new Coord3d[size];
		Color[]   colors = new Color[size];
		
		// Load file
		CSVReader reader = new CSVReader(new FileReader(FILENAME));
		String [] nextLine;
		int k=0;
		while ((nextLine = reader.readNext()) != null) {
			x = Float.parseFloat(nextLine[0]);
			y = Float.parseFloat(nextLine[1]);
			z = Float.parseFloat(nextLine[2]);
			a = Float.parseFloat(nextLine[3]);
			points[k]   = new Coord3d(x, y, z);
			colors[k++] = new Color(x, y, z, a);
		}
		reader.close();
		
		Scatter scatter = new Scatter(points, colors);
		chart = new Chart();
		chart.getScene().add(scatter);
	}
	
	protected static int readNLines(String filename) throws IOException{
		CSVReader reader = new CSVReader(new FileReader(filename));
		int n = 0;
		while(reader.readNext() != null){
			n++;
		}
		reader.close();
		return n;
	}
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}
