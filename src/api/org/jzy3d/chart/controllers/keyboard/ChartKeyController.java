package org.jzy3d.chart.controllers.keyboard;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ChartCameraController;
import org.jzy3d.maths.Coord2d;


public class ChartKeyController extends ChartCameraController implements KeyListener{
		
	public ChartKeyController(){
	}
	
	public ChartKeyController(Chart chart){
		addTarget(chart);
	}
	
	public void addTarget(Chart chart){
		super.addTarget(chart);
		chart.getCanvas().addKeyListener(this);
	}
	
	public void dispose(){
		for(Chart c: targets){
			c.getCanvas().removeKeyListener(this);
		}
		
		super.dispose(); // i.e. target=null
	}
	
	/*********************************************************/
	
    public void keyPressed(KeyEvent e) {
    	// rotation
    	if(!e.isShiftDown()){
    		Coord2d move = new Coord2d();
    		float offset = 0.1f;
    		switch(e.getKeyCode()){
    		case KeyEvent.VK_DOWN:
    			move.y = move.y - offset; rotate( move ); break;
    		case KeyEvent.VK_UP:
    			move.y = move.y + offset; rotate( move ); break;
    		case KeyEvent.VK_LEFT:
    			move.x = move.x - offset; rotate( move ); break;
    		case KeyEvent.VK_RIGHT:
    			move.x = move.x + offset; rotate( move ); break;
    		default:
    			break;
    		}
    	}
    	// zoom
    	else{    		
    		switch(e.getKeyCode()){
    		// shift
    		case KeyEvent.VK_DOWN:
    			shift( 0.1f ); break;
    		case KeyEvent.VK_UP:
    			shift( -0.1f ); break;
    		// zoom
    		case KeyEvent.VK_LEFT:
    			zoom( 0.9f ); break;
    		case KeyEvent.VK_RIGHT:
    			zoom( 1.1f ); break;
    		default:
    			break;
    		}
    	}
    }
    
    public void keyTyped(KeyEvent e) {
    }
    
    public void keyReleased(KeyEvent e) {
    }
}
