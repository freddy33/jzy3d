package org.jzy3d.demos.scatter;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.IColorMappable;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.colors.colormaps.IColorMap;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.MultiColorScatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.legends.colorbars.ColorbarLegend;


public class Scatter4D extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new Scatter4D());
	}
	
	public Scatter4D() {
		// Create the dot cloud scene and fill with data
		final int nPointsInEachAxis = 100;
		int size = nPointsInEachAxis * nPointsInEachAxis * nPointsInEachAxis;
		Coord3d[] points = new Coord3d[size];		
		Color[]   colors = new Color[size];
		int nPoints= 0;
		MyColorFunction myIColorMap= new MyColorFunction();
		ColorMapper myColorMap= new ColorMapper(myIColorMap, -1.0f, 1.0f); //define my own Color Map with my own Color Function

		//Create the points matrix giving some randomness to position to avoid the aliasing effects of having all the points perfectly aligned.
		for (int incX = 0; incX < nPointsInEachAxis; incX++) {
			for (int incY = 0; incY < nPointsInEachAxis; incY++) {
				for (int incZ = 0; incZ < nPointsInEachAxis; incZ++) {
					Coord3d coord= new Coord3d(incX+(float)Math.random(), incY+(float)Math.random(), incZ+(float)Math.random());
					points[incZ + incY * nPointsInEachAxis + incX * nPointsInEachAxis * nPointsInEachAxis] = coord; 
					colors[incZ + incY * nPointsInEachAxis + incX * nPointsInEachAxis * nPointsInEachAxis] = myColorMap.getColor(coord);
					nPoints++;
				}
			}
		}

		//Fill up the matrix if necessary
		for(int incX=nPoints; incX<size; incX++){ 
			points[incX] = new Coord3d(0, 0, 0);
			colors[incX] = new Color(0,0,0, 1); //Last parameter "a" is the transparency of the color
		}		
		
		MultiColorScatter scatter = new MultiColorScatter(points, colors, myColorMap);
		chart = new Chart(Quality.Advanced); //Needed to have the Legend numbers displayed correctly
		chart.getScene().add(scatter);
		
		scatter.setLegend( new ColorbarLegend(scatter, 
				chart.getView().getAxe().getLayout().getZTickProvider(), 
				chart.getView().getAxe().getLayout().getZTickRenderer()) );
		scatter.setLegendDisplayed(true);
	}

	public static class MyColorFunction implements IColorMap {  //Define the function to assign colors to each point
		boolean colorMapDirection= true;
		ColorMapper rainbowMap;
		Color myColor;
		float energy ;
		public float zMin;
		public float zMax;
		double scale;

		public MyColorFunction(){
			this.zMin = -1.0f;
			this.zMax = 1.0f;
			this.rainbowMap = new ColorMapper(new ColorMapRainbow(), this.zMin, this.zMax);
			this.scale= 2.0*Math.PI/100.0;
		}

		public void setDirection(boolean isStandard){
			colorMapDirection= isStandard;
		};  	    
		public boolean getDirection(){
			return colorMapDirection;    	    	
		}
		public Color getColor(IColorMappable colorable, float x, float y, float z) {   //Returns the color of point(x,y,z)
			energy= (float)(Math.sin(((double)x)*scale)
					* Math.sin(((double)y)*scale)
					* Math.sin(((double)z)*scale) ); 

			myColor= rainbowMap.getColor(energy); // We get the  color using the very useful RainbowMap 
			myColor.alphaSelf((float) Math.abs(energy/5.0));			// Giving some transparency to view the inside points			
			
			return myColor;
		}

		public Color getColor(IColorMappable colorable, float v) {
			myColor= rainbowMap.getColor(v); // We get the  color using the very useful RainbowMap 			
			return myColor;
		};
	}

	public Chart getChart() {
		return chart;
	}
	protected Chart chart;
}