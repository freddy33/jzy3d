package org.jzy3d.demos.histogram;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.HistogramBar;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;


public class HistogramDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new HistogramDemo());
	}
	
	public HistogramDemo(){
		chart = new Chart(Quality.Advanced);
		Scene scene = chart.getScene();
		for(int i=0; i<200; i+=50)
			for(int j=0; j<200; j+=50)
				scene.add( addBar(i,j,Math.random()*2) );
	}
		
	public AbstractDrawable addBar(double x, double y, double height){
		Color color = Color.random();
		color.a = 0.55f;

		HistogramBar bar = new HistogramBar();
		bar.setData(new Coord3d(x, y, 0), (float)height, 10, color);
		bar.setWireframeDisplayed(false);
		bar.setWireframeColor(Color.BLACK);
		return bar;
	}

	
	@Override
	public String getPitch() {
		return "Draws a bar chart";
	}
	
	public Chart getChart(){
		return chart;
	}
	protected Chart chart;
}
