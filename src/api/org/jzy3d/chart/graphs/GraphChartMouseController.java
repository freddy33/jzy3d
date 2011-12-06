package org.jzy3d.chart.graphs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.controllers.mouse.ChartMouseController;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.rendering.scene.Graph;

public class GraphChartMouseController<V,E> extends ChartMouseController{
	public GraphChartMouseController(){
		super();
		picking = new PickingSupport();
	}
	
	public GraphChartMouseController(Chart chart){
		super(chart);
		picking = new PickingSupport();
	}

	public GraphChartMouseController(Chart chart, int brushSize){
		super(chart);
		picking = new PickingSupport(brushSize);
	}

	public GraphChartMouseController(Chart chart, int brushSize, int bufferSize){
		super(chart);
		picking = new PickingSupport(brushSize, bufferSize);
	}
	
	/****************/

	public PickingSupport getPickingSupport() {
		return picking;
	}

	public void setPickingSupport(PickingSupport picking) {
		this.picking = picking;
	}
	
	/****************/

	public void mousePressed(MouseEvent e) {
		int yflip = -e.getY() + targets.get(0).getCanvas().getRendererHeight();
		prevMouse.x  = e.getX();
		prevMouse.y  = yflip;
		GraphView view = (GraphView)targets.get(0).getView();
		prevMouse3d = view.projectMouse(e.getX(),yflip);
		
		GL2 gl = chart().getView().getCurrentGL();
		Graph graph = chart().getScene().getGraph();
		
		// will trigger vertex selection event to those subscribing to PickingSupport.
		picking.pickObjects(gl, glu, view, graph, new IntegerCoord2d(e.getX(),yflip));
	}
	
	public void mouseDragged(MouseEvent e) {
		int yflip = -e.getY() + targets.get(0).getCanvas().getRendererHeight();
		Coord2d mouse = new Coord2d(e.getX(),yflip);
		GraphView view = (GraphView)targets.get(0).getView();
		Coord3d thisMouse3d = view.projectMouse(e.getX(),yflip);
		
		// 1/2 pan for cleaner rendering
		if(!done){
			pan(prevMouse3d, thisMouse3d); done = true;
		}
		else
			done = false;
		prevMouse = mouse;
		prevMouse3d = thisMouse3d;
	}
	protected boolean done;
	
	public void mouseWheelMoved(MouseWheelEvent e) {
		lastInc = (e.getWheelRotation()/10.0f);
		factor = factor + lastInc;

		GraphView view = (GraphView)targets.get(0).getView();
		mouse3d = view.projectMouse( lastMouseX, lastMouseY );

		zoom(1+lastInc);
	}
	
	/*public void mouseMoved(MouseEvent e) {
		lastMouseX = e.getX();
		lastMouseY = -e.getY() + targets.get(0).getCanvas().getRendererHeight();
		
		GraphView view = (GraphView)targets.get(0).getView();
		mouse3d = view.projectMouse( lastMouseX, lastMouseY );

		//System.out.println("moved to " + mouse3d);
	}*/
	
	/**********************/
	
	protected void zoom(final float factor){
		Chart chart = targets.get(0);
		BoundingBox3d viewBounds = chart.getView().getBounds();
		BoundingBox3d newBounds = viewBounds.scale(new Coord3d(factor,factor,1));
		chart.getView().setBoundManual(newBounds);
		chart.getView().shoot();
		
		fireControllerEvent(ControllerType.ZOOM, factor);
	}
	
	protected void pan(Coord3d from, Coord3d to){
		Chart chart = targets.get(0);
		
		BoundingBox3d viewBounds = chart.getView().getBounds();
		Coord3d offset = to.sub(from).div(-PAN_FACTOR);
		BoundingBox3d newBounds = viewBounds.shift(offset);
		chart.getView().setBoundManual(newBounds);
		chart.getView().shoot();
		
		fireControllerEvent(ControllerType.PAN, offset);
	}
	
	protected static float PAN_FACTOR = 0.25f;
	
	/**********************/
	
	protected void zoomOld(final float factor){
		for(Chart chart: targets){
			@SuppressWarnings("unused")
			BoundingBox3d viewBounds = chart.getView().getBounds();
			BoundingBox3d graphBounds = chart.getScene().getGraph().getBounds();
			System.out.println("graph bounds=" + graphBounds);
			//
			float xviewcenter = mouse3d.x;
			float yviewcenter = mouse3d.y;
			System.out.println("xviewcenter=" + xviewcenter +  " yviewcenter="+yviewcenter);
			//BoundingBox3d newBounds = viewBounds.shift(new Coord3d(xviewcenter, yviewcenter,0)).scale(new Coord3d(factor,factor,1));
			BoundingBox3d newBounds = graphBounds.scale(new Coord3d(factor,factor,1)).shift(new Coord3d(xviewcenter, yviewcenter,0));
			//BoundingBox3d newBounds = graphBounds.shift(new Coord3d(xviewcenter, yviewcenter,0)).scale(new Coord3d(factor,factor,1));
			chart.getView().setBoundManual(newBounds);
			chart.getView().shoot();
		}
		
		fireControllerEvent(ControllerType.ZOOM, factor);
	}
	
	protected Chart chart(){
		return targets.get(0);
	}
	
	/**********************/
	
	protected float factor = 1;
	protected float lastInc;
	protected int lastMouseX = 0;
	protected int lastMouseY = 0;
	protected Coord3d mouse3d;	
	protected Coord3d prevMouse3d;
	protected PickingSupport picking;
	protected GLU glu = new GLU();
}
