package org.jzy3d.demos.light;


import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
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
public class SphereLightDemo extends AbstractLightDemo{
	public static void main(String[] args){
		final Chart chart = getChart("awt");
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
				default: break;
		        }
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		
		
		MaterialEditor enlightableEditor = new MaterialEditor(chart);
		enlightableEditor.setTarget((Enlightable)chart.getScene().getGraph().getAll().get(0));
		LightEditor lightEditor = new LightEditor(chart);
		lightEditor.setTarget(chart.getScene().getLightSet().get(0));

		ChartLauncher.openChart(chart, new Rectangle(400,0,700,700), "Light Demo");
		ChartLauncher.openPanel(lightEditor, new Rectangle(0,0,200,900), "Light");
		ChartLauncher.openPanel(enlightableEditor, new Rectangle(200,0,200,675), "Material");		
	}
	
	public static Chart getChart(String type){
		Chart chart = new Chart(Quality.Advanced, type);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getView().getAxe().getLayout().setGridColor(Color.WHITE);
		chart.getView().getAxe().getLayout().setFaceDisplayed(false);
		chart.getView().setAxeSquared(false);
		//chart.getView().setProjection(Camera.PERSPECTIVE);
		
		float radius = 50;
		int slices = 100;

		addLight( chart, new Coord3d());
		sceneSphere(chart, radius, slices);
		return chart;
	}
	
	protected static void sceneSphere(Chart chart, float radius, int slices){
		addSphere( chart, new Coord3d(-100,-100,-50), radius, slices);
		addSphere( chart, new Coord3d(100,-100,-50), radius, slices);
		addSphere( chart, new Coord3d(100,100,-50), radius, slices);
		addSphere( chart, new Coord3d(-100,100,-50), radius, slices);
		
		addSphere( chart, new Coord3d(-100,-100,50), radius, slices);
		addSphere( chart, new Coord3d(100,-100,50), radius, slices);
		addSphere( chart, new Coord3d(100,100,50), radius, slices);
		addSphere( chart, new Coord3d(-100,100,50), radius, slices);
		
		addSphere( chart, new Coord3d(-200,-200,0), radius, slices); // shows shadows are not part of native open gl 
	}
}