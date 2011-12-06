package org.jzy3d.trials.g2d;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.List;

import org.jzy3d.bridge.awt.DoubleBufferedPanelAWT;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.ChartMouseController;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.Scale;
import org.jzy3d.maths.TicToc;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.primitives.interactive.tools.PolygonProjection;
import org.jzy3d.plot3d.primitives.interactive.tools.ProjectionUtils;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.ui.Plugs;


public class G2dSurfaceTrial {
	public static void main(String[] args) throws IOException, InterruptedException{
		chart = getSurfaceChart(Quality.Intermediate, "awt");//HistogramDemo.getChart();
		panel = new DoubleBufferedPanelAWT(){
			public void draw(Graphics g) {
				// draw bg
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(java.awt.Color.WHITE);
				g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
				
				// project to 2d
				List<PolygonProjection> proj = ProjectionUtils.project(chart);
				
				// draw polygons
				TicToc t = new TicToc();
				t.tic();
				for(PolygonProjection p: proj){
					drawProjection(g2d, p);
				}
				t.toc(); System.out.println( "(ms) Draw :" + t.elapsedMilisecond() + " for " + proj.size() + " monotypes" );
			}
			private static final long serialVersionUID = -1;
		};
		openChart(chart, new Rectangle(0,0,600,600), "Jzy3d OpenGL"); // has a mouse controller that refresh the panel
		openPanel(panel, new Rectangle(600,0,600,600), "Jzy3d Java2d & OpenGL");
	}
	
	public static void drawProjection(Graphics2D graphic, PolygonProjection p){
		Color c = p.getMeanColor();
		
		java.awt.Polygon poly = new java.awt.Polygon();
		for(Coord3d coord: p.coords){
			poly.addPoint((int)coord.x, panel.getHeight()-(int)coord.y);
		}
		
		graphic.setPaint( new java.awt.Color(c.r, c.g, c.b, c.a) );
		//graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, c.a));
		graphic.fillPolygon(poly);
		graphic.setColor( java.awt.Color.BLACK );
		graphic.drawPolygon(poly);
	}

	
	/********************************************/
	
	public static Chart getSurfaceChart(Quality quality,String type){
		Mapper mapper = new Mapper(){
			public double f(double x, double y) {
				return 10*Math.sin(x/10)*Math.cos(y/20)*x;
			}
		};
		Range range = new Range(-150,150);
		int steps   = 50;
		
		// Create the object to represent the function over the given range.
		surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,1f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		
		// Create a chart that updates the surface colormapper when scaling changes
		Chart chart = new Chart(quality, type){
			public void setScale(Scale scale){
				super.setScale(scale);
				ColorMapper cm = surface.getColorMapper();
				cm.setScale(scale);
				surface.setColorMapper(cm);
			}
		};
		chart.getScene().getGraph().add(surface);
		return chart;
	}
	
	static Shape surface;
	static Chart chart;
	static DoubleBufferedPanelAWT panel;
	
	/***********************************************************/
	
	public static void openChart(Chart chart, Rectangle bounds, String title){
		ChartMouseController mouse   = new ChartMouseController(){
			public void rotate(Coord2d move){
				super.rotate(move);
				panel.repaint();
			}
		};
		/*mouse.addControllerEventListener(new ControllerEventListener(){
			@Override
			public void controllerEventFired(ControllerEvent e) {
				panel.repaint();
			}
			
		});*/
		chart.addController(mouse);
		
		// Open it in a window
		Plugs.frame(chart, bounds, title);
	}	
	
	public static void openPanel(DoubleBufferedPanelAWT p, Rectangle bounds, String title){
		new FrameG2D(p, bounds, title);
	}
	
}
