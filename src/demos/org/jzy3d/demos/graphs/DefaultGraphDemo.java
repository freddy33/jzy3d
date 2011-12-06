package org.jzy3d.demos.graphs;


import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.chart.graphs.GraphChart;
import org.jzy3d.chart.graphs.GraphChartMouseController;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.graphs.IGraph;
import org.jzy3d.maths.graphs.StringGraphGenerator;
import org.jzy3d.picking.IObjectPickedListener;
import org.jzy3d.plot3d.primitives.graphs.impl.DefaultDrawableGraph2d;
import org.jzy3d.plot3d.primitives.graphs.layout.DefaultGraphFormatter;
import org.jzy3d.plot3d.primitives.graphs.layout.IGraphFormatter;
import org.jzy3d.plot3d.primitives.graphs.layout.IGraphLayout2d;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;


public class DefaultGraphDemo extends AbstractDemo{
	public static int NODES = 100000;
	public static int EDGES = 2000;
	public static float GL_LAYOUT_SIZE = 10;
	
	public static void main(String[] args) throws Exception{
		IDemo demo = new DefaultGraphDemo();
		Launcher.openStaticDemo(demo);
	}
	
	public DefaultGraphDemo(){
		// Init graph
		IGraph<String, String> graph = StringGraphGenerator.getGraph(NODES, EDGES);
		IGraphLayout2d<String> layout = StringGraphGenerator.getRandomLayout(graph, GL_LAYOUT_SIZE);
		IGraphFormatter<String, String> formatter = new DefaultGraphFormatter<String, String>();
		formatter.setVertexLabelsDisplayed(false);
		
		// Create chart
		Quality quality = Quality.Advanced;
		quality.setDepthActivated(false);
		chart = new GraphChart(quality);
		chart.getView().setAxeBoxDisplayed(false);
		chart.getView().setViewPositionMode(ViewPositionMode.TOP);
		chart.getView().setAxeSquared(false);
		
		// Build a drawable graph
		final DefaultDrawableGraph2d<String,String> dGraph = new DefaultDrawableGraph2d<String,String>();
		
		
		GraphChartMouseController<String,String> mouse = new GraphChartMouseController<String,String>(chart);
		mouse.getPickingSupport().addObjectPickedListener(new IObjectPickedListener() {
			@Override
			public void objectPicked(List<? extends Object> vertices) {
				for(Object vertex: vertices){
					System.out.println("picked: " + vertex);
					dGraph.setVertexHighlighted((String)vertex, true);
				}
				chart.render();
			}
		});
		
		dGraph.setGraphModel(graph, mouse.getPickingSupport());
		dGraph.setGraphLayout(layout);
		dGraph.setGraphFormatter(formatter);

		chart.getScene().getGraph().add( dGraph );
	}
		
	public Chart getChart(){
		return chart;
	}	
	protected Chart chart;
}
