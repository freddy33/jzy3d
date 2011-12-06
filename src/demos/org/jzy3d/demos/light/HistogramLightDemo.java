package org.jzy3d.demos.light;


import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.enlightables.Enlightable;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.ui.ChartLauncher;
import org.jzy3d.ui.editors.LightEditor;
import org.jzy3d.ui.editors.MaterialEditor;


/**
 * @author Martin Pernollet
 * 
 */
public class HistogramLightDemo extends AbstractLightDemo{
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
				
				//KeyEvent.;
		        default: break;
		        }
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		ChartLauncher.openChart(chart, new Rectangle(400,0,900,900), "Light Demo");		
		
		MaterialEditor enlightableEditor = new MaterialEditor(chart);
		if(chart.getScene().getGraph().getAll().get(0) instanceof Enlightable)
			enlightableEditor.setTarget((Enlightable)chart.getScene().getGraph().getAll().get(0));
		LightEditor lightEditor = new LightEditor(chart);
		lightEditor.setTarget(chart.getScene().getLightSet().get(0));
		
		ChartLauncher.openPanel(lightEditor, new Rectangle(0,0,200,900), "Light");
		ChartLauncher.openPanel(enlightableEditor, new Rectangle(200,0,200,675), "Material");		
	}
	
	public static Chart getChart(String type){
		// Create a chart that updates the surface colormapper when scaling changes
		Chart chart = new Chart(Quality.Advanced, type);
		chart.getView().setBackgroundColor(Color.BLACK);
		chart.getView().setAxeSquared(true);
		chart.getAxeLayout().setGridColor(Color.WHITE);
		chart.getAxeLayout().setFaceDisplayed(false);
		addLight( chart, new Coord3d(0,-100,100));
		sceneHisto(chart);		
		return chart;
	}
	
	protected static void sceneHisto(Chart chart){
		float radius = 50;
		int slice = 10;
		Random rng = new Random();
		for(int i=-2; i< 2; i++){
			for(int j=-2; j< 2; j++){
				addTube(chart, new Coord3d(i*radius*2 + (radius/1)*i,j*radius*2 + (radius/1)*j, 0), radius, rng.nextFloat()*100, slice);
			}
		}
	}
}
