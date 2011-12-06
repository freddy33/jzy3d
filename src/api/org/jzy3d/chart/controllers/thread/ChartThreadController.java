package org.jzy3d.chart.controllers.thread;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ChartCameraController;
import org.jzy3d.maths.Coord2d;


/** The ViewAnimatedController provides a Thread for controlling the Camera and make
 * it turn around the view point along its the azimuth dimension.
 * @author Martin Pernollet
 *
 */
public class ChartThreadController extends ChartCameraController implements Runnable {
	
	public ChartThreadController(){
	}
	
	public ChartThreadController(Chart chart){
		addTarget(chart);
	}
	
	public void dispose(){
		stop();
		super.dispose();
	}
	
	/**************************************************************/
	
	/** Start the animation.*/
	public void start() {
		if (process==null) {
			process=new Thread(this);
			process.setName("Embedded by ChartThreadController");
			process.start();
		}
	}

	/** Stop the animation.*/
	public void stop() {
		if (process!=null) {
			process.interrupt();
			process=null;
		}
	}

	/** Run the animation.*/
	public void run() {	
		move = new Coord2d(step,0);

		while (process!=null) {
			try {
				rotate( move );
				Thread.sleep(speed);
			}
			catch (InterruptedException e){
				process = null;	
				//System.err.println("I have caught an interrupted exception!:" + e.getMessage());
			}
		}
	}
	
	/**************************************************************/
	
	private Coord2d    move;
	
	private Thread process  = null;
	private int    speed    = 10;//1000/25; // nb milisecond wait between two frames
	private float  step     = 0.005f;
}
