package org.jzy3d.demos.light;


import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.enlightables.Enlightable;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.ui.ChartLauncher;
import org.jzy3d.ui.editors.LightEditor;
import org.jzy3d.ui.editors.MaterialEditor;


/**
 * Light should be:
 * carrefully called w.r.t. to cam.shoot
 * imply computing normal of each polygon, quad, linestrip
 * 
 */
public class SurfaceLightTrial extends AbstractLightDemo {
	public static void main(String[] args){
		final Chart chart = getChart("swing");
		chart.getCanvas().addKeyListener(new KeyListener(){
			@Override
			public void keyPressed(KeyEvent e) {
				switch( e.getKeyChar()){
				case KeyEvent.VK_2: chart.getScene().getLightSet().get(0).getPosition().x -= 10; chart.render(); break;
				case KeyEvent.VK_8: chart.getScene().getLightSet().get(0).getPosition().x += 10; chart.render(); break;
				case KeyEvent.VK_4: chart.getScene().getLightSet().get(0).getPosition().y -= 10; chart.render(); break;
				case KeyEvent.VK_6: chart.getScene().getLightSet().get(0).getPosition().y += 10; chart.render(); break;
				case KeyEvent.VK_9: chart.getScene().getLightSet().get(0).getPosition().z += 10; chart.render(); break;
				case KeyEvent.VK_7: chart.getScene().getLightSet().get(0).getPosition().z -= 10; chart.render(); break;
				
				//KeyEvent.;
		        default: break;
		        }
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		

		addLight( chart, new Coord3d());
		ChartLauncher.openChart(chart, new Rectangle(400,0,900,900), "Light Demo");
		
		
		MaterialEditor enlightableEditor = new MaterialEditor(chart);
		if(chart.getScene().getGraph().getAll().get(0) instanceof Enlightable)
			enlightableEditor.setTarget((Enlightable)chart.getScene().getGraph().getAll().get(0));
		LightEditor lightEditor = new LightEditor(chart);

		lightEditor.setTarget(chart.getScene().getLightSet().get(0));

		
		ChartLauncher.openPanel(lightEditor, new Rectangle(0,0,200,900), "Light");
		ChartLauncher.openPanel(enlightableEditor, new Rectangle(200,0,200,900), "Material");
		
	}
	
	public static Chart getChart(String type){
		// Create a chart that updates the surface colormapper when scaling changes
		Chart chart = new Chart(Quality.Nicest, type);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getView().getAxe().getLayout().setGridColor(Color.WHITE);
		chart.getView().getAxe().getLayout().setFaceDisplayed(false);
		chart.getView().setAxeSquared(true);

		//chart.getView().setProjection(Camera.PERSPECTIVE);
		
		// Define a function to plot
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};

		// Define range and precision for the function to plot
		Range range = new Range(-150,150);
		int steps   = 50;		
		Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		chart.getScene().add(surface);
		return chart;
	}
}
