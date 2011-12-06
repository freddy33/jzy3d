package org.jzy3d.chart.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.jzy3d.chart.Chart;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.maths.Coord2d;


public abstract class ChartCameraController {
	
	public void dispose(){
		targets.clear();
	}

	/***************************************************************************/
	
	protected void rotate(final Coord2d move){
		for(Chart c: targets)
			c.getView().rotate(move);
		fireControllerEvent(ControllerType.ROTATE, move);
	}
	
	protected void shift(final float factor){
		for(Chart c: targets)
			c.getView().shift(factor);
		fireControllerEvent(ControllerType.SHIFT, factor);
	}
	
	protected void zoom(final float factor){
		for(Chart c: targets)
			c.getView().zoom(factor);
		fireControllerEvent(ControllerType.ZOOM, factor);
	}	
	
	/*************************************************************/

	public void addTarget(Chart chart){
		if(targets==null)
			targets = new ArrayList<Chart>(1);
		targets.add(chart);
	}
	
	public void removeTarget(Chart chart){
		if(targets!=null){
			targets.remove(chart);
		}	
	}
	
	/*************************************************************/

	public void addControllerEventListener(ControllerEventListener listener){
		controllerListeners.add(listener);
	}
	
	public void removeControllerEventListener(ControllerEventListener listener){
		controllerListeners.remove(listener);
	}
	
	protected void fireControllerEvent(ControllerType type, Object value){
		ControllerEvent e = new ControllerEvent(this, type, value);
		for(ControllerEventListener listener: controllerListeners)
			listener.controllerEventFired(e);
	}
	
	/*************************************************************/
	
	protected List<Chart> targets;
	protected Vector<ControllerEventListener> controllerListeners = new Vector<ControllerEventListener>(1);
}
