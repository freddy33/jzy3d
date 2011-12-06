package org.jzy3d.chart.controllers.mouse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ChartCameraController;
import org.jzy3d.chart.controllers.thread.ChartThreadController;
import org.jzy3d.maths.Coord2d;



public class ChartMouseController extends ChartCameraController implements MouseListener, MouseMotionListener, MouseWheelListener{
	
	public ChartMouseController(){
	}
	
	public ChartMouseController(Chart chart){
		addTarget(chart);
	}
	
	public void addTarget(Chart chart){
		super.addTarget(chart);
		this.prevMouse = Coord2d.ORIGIN; 
		chart.getCanvas().addMouseListener(this);
		chart.getCanvas().addMouseMotionListener(this);
		chart.getCanvas().addMouseWheelListener(this);
	}
	
	public void dispose(){
		for(Chart c: targets){
			c.getCanvas().removeMouseListener(this);
			c.getCanvas().removeMouseMotionListener(this);
			c.getCanvas().removeMouseWheelListener(this);
		}
		
		if(threadController!=null)
			threadController.stop();
		
		super.dispose(); // i.e. target=null
	}
	
	/*********************************************************/
	
	public void addSlaveThreadController(ChartThreadController controller){
		removeSlaveThreadController();
		this.threadController = controller;
	}
	
	public void removeSlaveThreadController(){
		if(threadController!=null){
			threadController.stop();
			threadController = null;
		}
	}
		
	/*********************************************************/
	
	/** Handles toggle between mouse rotation/auto rotation: double-click starts the animated
	 * rotation, while simple click stops it.*/
	public void mousePressed(MouseEvent e) {
		// 
		if(MouseUtilities.isDoubleClick(e)){
			if(threadController!=null){
				threadController.start();
				return;
			}
		}
		if(threadController!=null)
			threadController.stop();
			
		prevMouse.x  = e.getX();
		prevMouse.y  = e.getY();
	}
	
	/** Compute shift or rotate*/
	public void mouseDragged(MouseEvent e) {
		Coord2d mouse = new Coord2d(e.getX(),e.getY());
		
		// Rotate
		if(MouseUtilities.isLeftDown(e)){
			Coord2d move  = mouse.sub(prevMouse).div(100);
			rotate( move );
		}
		// Shift
		else if(MouseUtilities.isRightDown(e)){
			Coord2d move  = mouse.sub(prevMouse);
			if(move.y!=0)
				shift(move.y/500);
		}
		prevMouse = mouse;
	}
	
	/** Compute zoom */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(threadController!=null)
			threadController.stop();
		
		float factor = 1 + (e.getWheelRotation()/10.0f);
		zoom(factor);
	}
	
	public void mouseClicked(MouseEvent e) {
		/*if(MouseUtilities.isRightClick(e)){
			rightClicked(e);
		}*/
		/*else if(MouseUtilities.isLeftDown(e)){
			rightClicked(e);
		}*/
	}  
	
	/*public void rightClicked(MouseEvent e){
		
	}*/

	
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {} 
	public void mouseMoved(MouseEvent e) {}
	
	/*********************************************************/
	
	protected Coord2d    prevMouse;
	protected ChartThreadController threadController;
	
	//protected Chart chart;
}
