package org.jzy3d.demos.interactive;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.AbstractChartMouseSelector;
import org.jzy3d.chart.controllers.mouse.ChartMouseController;
import org.jzy3d.chart.controllers.mouse.interactives.ScatterMouseSelector;
import org.jzy3d.chart.controllers.thread.ChartThreadController;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.interactive.InteractiveScatter;
import org.jzy3d.plot3d.rendering.view.Renderer2d;
import org.jzy3d.ui.Plugs;


/**
 * @author Martin Pernollet
 */
public class InteractiveScatterDemo {
	static int POINTS = 25000;
	
	public static void main(String[] args) {
    	Plugs.frame(getChart(), new Rectangle(0,200,400,400), "Jzy3d Demo");
    }

    public static Chart getChart() {
    	InteractiveScatter scatter = generateScatter(POINTS);
        
        chart = new Chart("awt");
        chart.getScene().add( scatter );
        chart.getView().setMaximized(true);
        
        // Create and add controllers
        threadCamera = new ChartThreadController(chart);
        mouseCamera = new ChartMouseController();
        mouseCamera.addSlaveThreadController(threadCamera);
        mouseSelection = new ScatterMouseSelector(scatter);
        chart.getCanvas().addKeyListener(new KeyListener(){
			public void keyPressed(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {
				switch( e.getKeyChar()){
				case 'c': releaseCam(); break;				
				default: break;
		        }
				holding = false;
				message = MESSAGE_SELECTION_MODE;
				chart.render(); // update message display
			}
			public void keyTyped(KeyEvent e) {
				if(!holding){
					switch( e.getKeyChar()){
						case 'c': useCam(); break;				
						default: break;
			        }
					holding = true;
					message = MESSAGE_ROTATION_MODE;
					chart.render();
				}
			}
			protected boolean holding = false;
		});
        releaseCam();
        
        message = MESSAGE_SELECTION_MODE;
        messageRenderer = new Renderer2d(){
        	public void paint(Graphics g){
        		if(displayMessage && message!=null){
        			g.setColor(java.awt.Color.RED);
        			g.drawString(message, 10, 30);
        		}
        			
        	}
        };
        chart.addRenderer(messageRenderer);
        return chart;
    }
    
    protected static void useCam(){
    	mouseSelection.releaseChart();
        chart.addController(mouseCamera);
    }
    
    protected static void releaseCam(){
    	chart.removeController(mouseCamera);
        mouseSelection.attachChart(chart);
    }

    protected static InteractiveScatter generateScatter(int npt){
        Coord3d[] points = new Coord3d[npt];
        Color[] colors = new Color[npt];
    	Random rng = new Random();
        for (int i = 0; i < npt; i++) {
            colors[i] = new Color(0f, 0f, 0f, 0.5f);
            points[i] = new Coord3d(rng.nextFloat(), rng.nextFloat(), rng.nextFloat());
        }
        InteractiveScatter dots = new InteractiveScatter(points, colors);
        dots.setWidth(1);
        return dots;
    }
    
    /***********************************************/
    
	//protected static SelectableScatter scatter;
    protected static Chart chart;
    protected static Renderer2d messageRenderer;

    protected static ChartThreadController threadCamera;
    protected static ChartMouseController mouseCamera;
    protected static AbstractChartMouseSelector mouseSelection;

    protected static boolean displayMessage = true;
    protected static String message;
    private static final long serialVersionUID = 1L;
    
    public static String MESSAGE_SELECTION_MODE = "Selection mode (hold 'c' to control camera)";
    public static String MESSAGE_ROTATION_MODE = "Rotation mode (release 'c' to make a selection)";
}
