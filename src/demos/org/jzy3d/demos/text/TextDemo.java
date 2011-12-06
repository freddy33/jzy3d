package org.jzy3d.demos.text;



import java.awt.Font;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.ControllerType;
import org.jzy3d.chart.graphs.GraphChart;
import org.jzy3d.chart.graphs.GraphChartMouseController;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.events.ControllerEvent;
import org.jzy3d.events.ControllerEventListener;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.plot3d.text.DrawableTextWrapper;
import org.jzy3d.plot3d.text.ITextRenderer;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.drawable.DrawableTextBillboard;
import org.jzy3d.plot3d.text.drawable.DrawableTextBitmap;
import org.jzy3d.plot3d.text.drawable.DrawableTextTexture;
import org.jzy3d.plot3d.text.drawable.cells.DrawableTextCell;
import org.jzy3d.plot3d.text.drawable.cells.TextCellRenderer;
import org.jzy3d.plot3d.text.renderers.jogl.JOGLTextRenderer;
import org.jzy3d.plot3d.text.renderers.jogl.ShadowedTextStyle;


public class TextDemo extends AbstractDemo{
	public static int NODES = 50;
	public static int EDGES = 10;
	public static int BOXES = 5;
	public static float GL_LAYOUT_SIZE = 10;
	
	public static void main(String[] args) throws Exception{
		IDemo demo = new TextDemo();
		Launcher.openStaticDemo(demo);
		demo.getChart().getView().updateBounds(); // some text bounds are only known after drawing
	}
	
	public TextDemo(){
		// Make a chart
		Quality quality = Quality.Advanced;
		quality.setDepthActivated(false);
		chart = new GraphChart(quality, "awt");
		chart.getView().setAxeBoxDisplayed(true);
		chart.getView().setViewPositionMode(ViewPositionMode.TOP);
		chart.getView().setAxeSquared(false);
		chart.getView().setMaximized(true);
		
		
		// Text texture examples
		final DrawableTextTexture t = new DrawableTextTexture("dffdnfdddsfhsfgbAAA", new Coord2d(0,0), new Coord2d(8,1));
		chart.getScene().getGraph().add( t );
		
		final DrawableTextTexture t2 = new DrawableTextTexture("blablabla", new Coord2d(0,2), new Coord2d(8,1));
		chart.getScene().getGraph().add( t2 );

		final DrawableTextTexture t3 = new DrawableTextTexture("blablabla", new Coord2d(0,4), new Coord2d(4,0.5)); // smaller than t2
		chart.getScene().getGraph().add( t3 );

		
		// Two text object that support
		final DrawableTextBitmap t4 = new DrawableTextBitmap("DrawableTextBitmap", new Coord3d(0,6,0), Color.BLACK);
		t4.setHalign(Halign.RIGHT); // TODO: invert left/right
		chart.getScene().getGraph().add( t4 );
		
		final DrawableTextBillboard t5 = new DrawableTextBillboard("DrawableTextBillboard", new Coord3d(0,8,0), Color.BLACK);
		t5.setHalign(Halign.RIGHT); // TODO: invert left/right
		chart.getScene().getGraph().add( t5 );

		
		// Using JOGL text renderer with or without a text style
		ITextRenderer renderer2 = new JOGLTextRenderer(new ShadowedTextStyle(72f, 10, java.awt.Color.RED, java.awt.Color.CYAN));
		final DrawableTextWrapper t6 = new DrawableTextWrapper("DrawableTextWrapper(JOGLTextRenderer(ShadowedTextStyle))", new Coord3d(0,10,0), Color.BLACK, renderer2);
		chart.getScene().getGraph().add( t6 );
		
		ITextRenderer renderer3 = new JOGLTextRenderer();
		final DrawableTextWrapper t7 = new DrawableTextWrapper("DrawableTextWrapper(JOGLTextRenderer)", new Coord3d(0,12,0), Color.BLACK, renderer3);
		chart.getScene().getGraph().add( t7 );


		// Using text cell renderer
		TextCellRenderer cellRenderer = new TextCellRenderer(4, "DrawableTextCell(TextCellRenderer)", new Font("Serif", Font.PLAIN, 16));
		cellRenderer.setHorizontalAlignement(Halign.LEFT);
		cellRenderer.setBorderDisplayed(true);
		cellRenderer.setBorderColor(java.awt.Color.red);		
		final DrawableTextCell t8 = new DrawableTextCell(cellRenderer, new Coord2d(0,14), new Coord2d(7,1));
		chart.getScene().getGraph().add( t8 );

		// Add mouse controller, and listen to resize to show how bounds change, don't change, or are missing.
		GraphChartMouseController<String,String> mouse = new GraphChartMouseController<String,String>(chart);
		mouse.addControllerEventListener(new ControllerEventListener() {
			@Override
			public void controllerEventFired(ControllerEvent e) {
				if( e.getType()== ControllerType.ZOOM ){
					System.out.println("---------------------------------------------");
					System.out.println("DrawableTextBitmap has bounds: " + t4.getBounds());
					System.out.println("DrawableTextBillboard has bounds: " + t5.getBounds());
					System.out.println("JOGLTextRenderer has bounds: " + t6.getBounds());
					System.out.println("TextCellRenderer has bounds: " + t8.getBounds());					
				}
			}
		});
		
	}
		
	public Chart getChart(){
		return chart;
	}	
	protected Chart chart;
}
