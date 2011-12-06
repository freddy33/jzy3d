package org.jzy3d.demos.textures;


import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.textured.MaskPair;
import org.jzy3d.plot3d.primitives.textured.TexturedCube;
import org.jzy3d.plot3d.primitives.textured.TexturedCylinder;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.textures.TextureFactory;


public class TextureDemo extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new TextureDemo());
	}

	public TextureDemo() {
		chart = new Chart(Quality.Advanced);
	    chart.getView().setAxeSquared(false);
        //chart.getView().setBackgroundColor(Color.BLACK);
        chart.getScene().getGraph().add( getDrawableCylinder( new Coord3d(1,1,0.5), new Color(0.1f, 0.1f, 0.9f) ) );
	    chart.getScene().getGraph().add( getDrawableCube( new Coord3d(-1,-1,-0.5), new Color(0.1f, 0.9f, 0.9f) ) );  
	}
	
	public TexturedCylinder getDrawableCylinder(Coord3d c, Color face){
	    MaskPair mask = new MaskPair( TextureFactory.get("data/textures/masks/arrow-bg-mask-100-100.png"),
				   					  TextureFactory.get("data/textures/masks/arrow-symbol-mask-100-100.png") );
		TexturedCylinder p = new TexturedCylinder(c, face, face.negative(), mask);
		p.setAlphaFactor(0.8f);
		return p;
	}
	
	public TexturedCube getDrawableCube(Coord3d c, Color face){	
	    MaskPair mask = new MaskPair( TextureFactory.get("data/textures/masks/sharp-bg-mask-100-100.png"),
	               TextureFactory.get("data/textures/masks/sharp-symbol-mask-100-100.png") );
	    TexturedCube p = new TexturedCube(c, face, face.negative(), mask);
	    p.setAlphaFactor(0.8f);
		return p;
	}
	
	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
}
