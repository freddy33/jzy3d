package org.jzy3d.demos.scatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.ChartMouseController;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;


/**
 * @author Martin Pernollet
 */
public class ScatterSphereDemo extends AbstractDemo {
	static int POINTS = 1000;

	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new ScatterSphereDemo());
	}

	public ScatterSphereDemo()  {
		chart = new Chart(Quality.Fastest);
		chart.getScene().add(generateShereList(POINTS));
		chart.getView().setBoundManual(new BoundingBox3d(0, 1, 0, 1, 0, 1));
		chart.addController(new ChartMouseController());
	}

	protected List<AbstractDrawable> generateShereList(int npt) {
		List<AbstractDrawable> list = new ArrayList<AbstractDrawable>(npt);
		Random rng = new Random();
		for (int i = 0; i < npt; i++) {
			Coord3d position = new Coord3d(rng.nextFloat(), rng.nextFloat(),
					rng.nextFloat());
			Color color = new Color(1 * position.x * position.y, 1 * position.y
					* position.z, 1 * position.z * position.x, 1f);
			float radius = 0.025f * rng.nextFloat();
			int slices = 8;

			list.add(addSphere(position, color, radius, slices));
		}
		return list;
	}

	protected Sphere addSphere(Coord3d c, Color color, float radius,
			int slicing) {
		Sphere s = new Sphere(c, radius, slicing, color);
		s.setWireframeColor(Color.BLACK);
		s.setWireframeDisplayed(true);
		s.setWireframeWidth(1);
		s.setFaceDisplayed(true);
		// s.setMaterialAmbiantReflection(materialAmbiantReflection)
		return s;
	}

	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
	protected ChartMouseController mouseCamera;
}
