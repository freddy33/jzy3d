package org.jzy3d.demos.text;



import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.graphs.GraphChart;
import org.jzy3d.chart.graphs.GraphChartMouseController;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;
import org.jzy3d.plot3d.text.align.Halign;
import org.jzy3d.plot3d.text.drawable.cells.DrawableTextCell;
import org.jzy3d.plot3d.text.drawable.cells.TextCellRenderer;

/**
 * Experimental work.
 * 
 * This demo shows how bad a J2D text rendered in a texture is finally rendered in opengl
 * with the basic jzy3d texture support.
 * 
 * However one can enhance the quality of small text (i.e. far zoomed) by tuning
 * - the font to a lower size
 * - the image ratio to a value lower than 1 (which scale the buffered image before making it a texture resource)
 */
public class TextCellsDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception{
		IDemo demo = new TextCellsDemo();
		Launcher.openStaticDemo(demo);
		//demo.getChart().getView().updateBounds();
	}
	
	public TextCellsDemo(){
		Quality quality = Quality.Advanced;
		quality.setDepthActivated(false);
		
		chart = new GraphChart(quality, "awt");
		chart.getView().setAxeBoxDisplayed(false);
		chart.getView().setViewPositionMode(ViewPositionMode.TOP);
		chart.getView().setAxeSquared(false);
		//chart.getView().setMaximized(true);
		//chart.getView().getCamera().setScreenGridDisplayed(true);
		
		// Build a drawable graph
		List<String> strs = new ArrayList<String>();
		
		for (int i = 0; i < 100; i++) {
			strs.add("item #" + i);
		}
		
		chart.getScene().getGraph().add(column(strs));
		new GraphChartMouseController<String,String>(chart);
	}
	
	public static List<DrawableTextCell> column(List<String> strs){
		List<DrawableTextCell> cells = new ArrayList<DrawableTextCell>();
		int y = 0; 
		
		Font font = new Font("Arial", Font.PLAIN, 40);
		
		for(String s: strs){
			TextCellRenderer renderer = new TextCellRenderer(4, s, font);
			renderer.setHorizontalAlignement(Halign.LEFT);
			renderer.setBorderDisplayed(true);
			renderer.setBorderColor(Color.red);
			
			float imgRatio = 1f;		
			DrawableTextCell cell = new DrawableTextCell(renderer.getImage(imgRatio), new Coord2d(0,y), new Coord2d(7,1));
			cells.add(cell);
			y-=1;
		}
		return cells;
	}
	
		
	public Chart getChart(){
		return chart;
	}	
	protected Chart chart;
}
