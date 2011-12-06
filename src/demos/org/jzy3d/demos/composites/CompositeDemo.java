package org.jzy3d.demos.composites;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;


public class CompositeDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new CompositeDemo());
	}

	public CompositeDemo() {
		chart = new Chart(Quality.Advanced);
		Scene scene = chart.getScene();
		for (int i = 0; i < 150; i += 50)
			for (int j = 0; j < 150; j += 50)
				scene.add(addCylinder(i, j, Math.random() * 2));
	}

	public AbstractDrawable addCylinder(double x, double y, double height) {
		Color color = Color.RED;
		color.a = 0.55f;

		MyCustomCylinder bar = new MyCustomCylinder();
		bar.setData(new Coord3d(x, y, 0), (float) height, 7f, 15, 00, color);

		bar.setWireframeDisplayed(true);
		bar.setWireframeColor(Color.BLACK);
		return bar;
	}
	
	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
}
