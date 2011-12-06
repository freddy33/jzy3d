package org.jzy3d.demos;

import java.awt.Component;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jzy3d.bridge.swt.Bridge;
import org.jzy3d.chart.Chart;
import org.jzy3d.demos.animation.AnimatedSurfaceDemo;
import org.jzy3d.demos.axelayout.AxeRendererDemo;
import org.jzy3d.demos.background.BackgroundDemo;
import org.jzy3d.demos.composites.CompositeDemo;
import org.jzy3d.demos.contour.Contour3DDemo;
import org.jzy3d.demos.contour.ContourPlotsDemo;
import org.jzy3d.demos.contour.FilledContoursDemo;
import org.jzy3d.demos.contour.HeightMapDemo;
import org.jzy3d.demos.contour.UserChosenContoursDemo;
import org.jzy3d.demos.dynamic.AddRemoveElementsDemo;
import org.jzy3d.demos.histogram.HistogramDemo;
import org.jzy3d.demos.radar.RadarDemo;
import org.jzy3d.demos.scatter.MultiColorScatterDemo;
import org.jzy3d.demos.scatter.Scatter4D;
import org.jzy3d.demos.scatter.ScatterDemo;
import org.jzy3d.demos.scatter.ScatterDemoFromFile;
import org.jzy3d.demos.scatter.ScatterSphereDemo;
import org.jzy3d.demos.surface.BuildSurfaceDemo;
import org.jzy3d.demos.surface.ColorWaveDemo;
import org.jzy3d.demos.surface.MexicanDemo;
import org.jzy3d.demos.surface.WireSurfaceDemo;
import org.jzy3d.demos.surface.delaunay.BadDelaunayResult;
import org.jzy3d.demos.surface.delaunay.GeneratedDelaunaySurfaceDemo;
import org.jzy3d.demos.surface.delaunay.SphericDelaunaySurfaceDemo;
import org.jzy3d.demos.textures.TextureDemo;
import org.jzy3d.global.Settings;
import org.jzy3d.ui.ChartLauncher;


public class Launcher {	
	public static List<IDemo> getDemos() throws IOException{
		List<IDemo> demos = new ArrayList<IDemo>();
		
		// surface
		demos.add(new WireSurfaceDemo());
		demos.add(new MexicanDemo());
		demos.add(new ColorWaveDemo());
		demos.add(new BuildSurfaceDemo());
		demos.add(new BackgroundDemo());
		
		// delaunay surface
		demos.add(new SphericDelaunaySurfaceDemo());
		demos.add(new GeneratedDelaunaySurfaceDemo());
		demos.add(new BadDelaunayResult());
		
		// scatter
		demos.add(new ScatterDemo());
		demos.add(new ScatterDemoFromFile());
		demos.add(new MultiColorScatterDemo());
		demos.add(new ScatterSphereDemo());
		demos.add(new Scatter4D()); // REVIEW
		
		// bar chart
		demos.add(new HistogramDemo());
		
		// custom 3d
		demos.add(new CompositeDemo());
		demos.add(new RadarDemo());

		// enhanced primitives
		demos.add(new TextureDemo());
		//interactive
		//light
		
		
		// contour
		demos.add(new Contour3DDemo());
		demos.add(new ContourPlotsDemo());
		demos.add(new Contour3DDemo());
		demos.add(new FilledContoursDemo());
		demos.add(new HeightMapDemo());
		demos.add(new UserChosenContoursDemo());

		// layout
		demos.add(new AxeRendererDemo());
		// multiview
		// overlay
		// tooltips
		
		// animation
		demos.add(new AnimatedSurfaceDemo());
		demos.add(new AddRemoveElementsDemo());
		return demos;
	}
	
	public static void openDemo(IDemo demo) throws Exception{
		openDemo(demo, DEFAULT_WINDOW);
	}
	
	public static void openDemo(IDemo demo, Rectangle rectangle) throws Exception{
		Settings.getInstance().setHardwareAccelerated(true);
		Chart chart = demo.getChart();
		
		ChartLauncher.instructions();
		ChartLauncher.openChart(chart, rectangle, demo.getName());
		//ChartLauncher.screenshot(demo.getChart(), "./data/screenshots/"+demo.getName()+".png");
	}
	
	public static void openStaticDemo(IDemo demo) throws Exception{
		openStaticDemo(demo, DEFAULT_WINDOW);
	}
	
	public static void openStaticDemo(IDemo demo, Rectangle rectangle) throws Exception{
		Settings.getInstance().setHardwareAccelerated(true);
		Chart chart = demo.getChart();
		
		ChartLauncher.openStaticChart(chart, rectangle, demo.getName());
		ChartLauncher.screenshot(demo.getChart(), "./data/screenshots/"+demo.getName()+".png");
	}
	
	public static void openStaticSWTDemo(IDemo demo) throws Exception{
		Settings.getInstance().setHardwareAccelerated(true);
		Chart chart = demo.getChart();
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		Bridge.adapt(shell, (Component) chart.getCanvas());
		
		shell.setText(demo.getName());
		shell.setSize(800, 600);
		shell.open();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
	protected static String DEFAULT_CANVAS_TYPE = "awt";
	protected static Rectangle DEFAULT_WINDOW = new Rectangle(0,0,600,600);
}

