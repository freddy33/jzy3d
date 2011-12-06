package org.jzy3d.trials.offscreen;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;

import javax.imageio.ImageIO;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.ui.ChartLauncher;


public class OffscreenTrial extends AbstractDemo {
	public static void main(String[] args) throws Exception {
		IDemo demo = new OffscreenTrial();
		//Launcher.openDemo(new CopyOfWireSurfaceDemo());
		Chart chart = demo.getChart();
		chart.render();
		chart.screenshot();
		
		String filename = "./data/screenshots/offscreen.png";
		File output = new File(filename);
		if(!output.getParentFile().exists())
			output.mkdirs();
		ImageIO.write(chart.screenshot(), "png", output);
		System.out.println("Dumped screenshot in: " + filename);
		//ChartLauncher.openStaticChart(demo.getChart(), new Rectangle(0,0,800,800), demo.getName());
	}

	public OffscreenTrial() {
		// Define a function to plot
		Mapper mapper = new Mapper() {
			public double f(double x, double y) {
				return 10 * Math.sin(x / 10) * Math.cos(y / 20) * x;
			}
		};

		// Define range and precision for the function to plot
		Range range = new Range(-150, 150);
		int steps = 50;

		// Create the object to represent the function over the given range.
		final Shape surface = (Shape) Builder.buildOrthonormal(
				new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface
				.getBounds().getZmin(), surface.getBounds().getZmax(),
				new Color(1, 1, 1, .5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);

		// Create a chart and add surface
		chart = new Chart("offscreen");
		chart.getScene().getGraph().add(surface);

		// Setup a colorbar 
		ColorbarLegend cbar = new ColorbarLegend(surface, chart.getView().getAxe().getLayout());
		cbar.setMinimumSize(new Dimension(100, 600));
		//surface.setLegend(cbar);
	}

	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
}
