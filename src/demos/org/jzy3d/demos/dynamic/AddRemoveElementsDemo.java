package org.jzy3d.demos.dynamic;

import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IRunnableDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;


/** Show ability to add and remove elements from the scene graph during runtime.
 * Slowlyness of display is due to a rendering update after each drawn line.
 */
public class AddRemoveElementsDemo extends AbstractDemo implements IRunnableDemo{
	
	public static void main(String[] args) throws Exception{
		IRunnableDemo demo = new AddRemoveElementsDemo();
		Launcher.openDemo(demo);
		demo.start();
	}
	
	public static int MAX_DRAWABLE = 1000;
	
	public AddRemoveElementsDemo(){
		chart = new Chart();
	}

	@Override
	public String getPitch() {
		return "Adding an removing elements dynamically. " +
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

		// Adding random lines
		int k = 0;
		while(k<MAX_DRAWABLE){
			LineStrip line = new LineStrip();
			line.add(new Point(new Coord3d(rng.nextFloat(),rng.nextFloat(),rng.nextFloat()), new Color(rng.nextFloat(),rng.nextFloat(),rng.nextFloat())));
			line.add(new Point(new Coord3d(rng.nextFloat(),rng.nextFloat(),rng.nextFloat()), new Color(rng.nextFloat(),rng.nextFloat(),rng.nextFloat())));
			chart.getScene().getGraph().add(line);	
			memory.add(line);
			k++;
		}
		
		// Removing lines
		while(k>1){
			chart.getScene().getGraph().remove(memory.get(--k));
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	
	protected Chart chart;
	final List<AbstractDrawable> memory = new Vector<AbstractDrawable>();

}
