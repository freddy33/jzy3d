package org.jzy3d.demos.light;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Cylinder;
import org.jzy3d.plot3d.primitives.enlightables.EnlightableBar;
import org.jzy3d.plot3d.primitives.enlightables.EnlightableSphere;
import org.jzy3d.plot3d.rendering.lights.Light;


public class AbstractLightDemo {
	protected static EnlightableSphere addSphere(Chart chart, Coord3d c, float radius, int slicing){
		EnlightableSphere s = new EnlightableSphere(c,radius,slicing,Color.WHITE);
		//s.setWireframeColor(Color.WHITE);
		s.setWireframeDisplayed(false);
		s.setFaceDisplayed(true);
		//s.setMaterialAmbiantReflection(materialAmbiantReflection)
		chart.getScene().add( s );
		return s;
	}
	
	protected static EnlightableBar addTube(Chart chart, Coord3d c, float radius, float height, int slicing){
		EnlightableBar s = new EnlightableBar(c,height, radius, Color.WHITE);
		//s.setWireframeColor(Color.RED);
		s.setWireframeDisplayed(false);
		s.setFaceDisplayed(true);
		chart.getScene().add( s );
		return s;
	}
	
	protected static Cylinder addCylinder(Chart chart, Coord3d c, float radius, float height, int slicing){
		Cylinder s = new Cylinder();
		s.setData(c,height, radius, slicing, slicing, Color.BLACK);
		s.setWireframeColor(Color.GRAY);
		s.setWireframeDisplayed(true);
		s.setFaceDisplayed(true);
		chart.getScene().add( s );
		return s;
	}
	
	protected static Light addLight(Chart chart, Coord3d position){
		Light light = new Light(lightId++);
		light.setPosition(position);
		light.setAmbiantColor(Color.BLACK);
		light.setDiffuseColor(Color.CYAN);
		light.setSpecularColor(Color.CYAN);
		chart.getScene().add( light );
		return light;
	}
	protected static int lightId=0;
}
