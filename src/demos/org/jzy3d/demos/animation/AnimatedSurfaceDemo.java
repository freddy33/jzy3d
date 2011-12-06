package org.jzy3d.demos.animation;


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.IRunnableDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.maths.TicToc;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;
import org.jzy3d.plot3d.rendering.view.Renderer2d;


public class AnimatedSurfaceDemo extends AbstractDemo implements IRunnableDemo{

	public static void main(String[] args) throws Exception{
		IRunnableDemo demo = new AnimatedSurfaceDemo();
		Launcher.openDemo(demo);
		demo.start();
	}
	
	public AnimatedSurfaceDemo(){
		mapper = new ParametrizedMapper(0.9){
			public double f(double x, double y) {
				return 10*Math.sin(x*p)*Math.cos(y*p)*x;
			}
		};
		Range range = new Range(-150,150);
		int steps   = 50;
		
		// Create the object to represent the function over the given range.
		surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
		surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
		surface.setFaceDisplayed(true);
		surface.setWireframeDisplayed(true);
		surface.setWireframeColor(Color.BLACK);
		
		// Create a chart 
		chart = new Chart("awt");
		surface.setLegend(new ColorbarLegend(surface, 
							chart.getView().getAxe().getLayout().getZTickProvider(), 
							chart.getView().getAxe().getLayout().getZTickRenderer()));
		chart.getScene().getGraph().add(surface);

		// display FPS
		fpsText = "";
		chart.addRenderer(new Renderer2d(){
			public void paint(Graphics g) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(java.awt.Color.BLACK);
				g2d.drawString(fpsText, 50, 50);
			}
		});
	}
	
	public Chart getChart(){
		return chart;
	}
	
	public void start(){
		fpsText = "";
		t = new Thread(){
			TicToc tt = new TicToc();
			@Override
			public void run() {
				while(true){
					try {
						sleep(25);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tt.tic();
					mapper.setParam( mapper.getParam() + 0.0001 );
					remap(surface, mapper);
					//chart.render();
					tt.toc();
					fpsText = Utils.num2str(1/tt.elapsedSecond(), 4) + " FPS";
				}
			}
		};
		t.start();
	}
	
	public void stop(){
		if(t!=null)
			t.interrupt();
	}
	
	protected void remap(Shape shape, Mapper mapper){
		List<AbstractDrawable> polygons = shape.getDrawables();		
		for(AbstractDrawable d: polygons){
			if(d instanceof Polygon){
				Polygon p = (Polygon) d;				
				for(int i=0; i<p.size(); i++){
					Point pt = p.get(i);
					Coord3d c = pt.xyz;
					c.z = (float) mapper.f(c.x, c.y);
				}
			}
		}
	}
	
	protected Chart chart;
	protected Shape surface;
	protected ParametrizedMapper mapper;	
	protected String fpsText;
	
	protected Thread t;
}
