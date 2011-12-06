package org.jzy3d.debug.surf;

import java.util.ArrayList;
import java.util.List;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class DelaunaySurfaceProblem extends AbstractDemo{
	public static void main(String[] args) throws Exception {
		Launcher.openDemo(new DelaunaySurfaceProblem());
	}
	
    public DelaunaySurfaceProblem(){        
        List<Coord3d> coords = DelaunaySurfaceProblem.generateBug(new Coord3d(10,20,30),10000,20, true);
         
        // Create the object to represent the function over the given range.
        final Shape surface = (Shape) Builder.buildDelaunay(coords);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1, 1, 1, 1.0f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);

        // Create a chart that updates the surface colormapper when scaling changes
        chart = new Chart(Quality.Advanced);
		chart.getScene().getGraph().add(surface);
		
	}

	public Chart getChart() {
		return chart;
	}

	protected Chart chart;
    
	@Override
	public String getPitch() {
		return "Spheric data tesselation with Delaunay";
	}


    public static List<Coord3d> generateBug(Coord3d center, float radius, int steps, boolean half){
    	List<Coord3d> coords = new ArrayList<Coord3d>(1500); // TODO optimize

        coords.add(new Coord3d(8.2,0.5,5.8529350500E-11    ));
        coords.add(new Coord3d(8.3,0.5,3.4353673200E-03));
        coords.add(new Coord3d(8.4,0.5,3.5604140800E-03));
        coords.add(new Coord3d(8.5,0.5,3.6885463900E-03));
        coords.add(new Coord3d(8.6,0.5,3.8199394700E-03));
        coords.add(new Coord3d(8.7,0.5,3.9546201200E-03));
        coords.add(new Coord3d(8.8,0.5,4.0929576900E-03));
        coords.add(new Coord3d(8.9,0.5,4.2341162900E-03));
        coords.add(new Coord3d(9.0,0.5,4.3794032400E-03));
        coords.add(new Coord3d(8.2,0.6,6.8745159600E-03));
        coords.add(new Coord3d(8.3,0.6,7.0651198100E-03));
        coords.add(new Coord3d(8.4,0.6,7.2585992900E-03));
        coords.add(new Coord3d(8.5,0.6,7.4557875400E-03));
        coords.add(new Coord3d(8.6,0.6,7.6555664700E-03));
        coords.add(new Coord3d(8.7,0.6,7.8588523000E-03));
        coords.add(new Coord3d(8.8,0.6,8.0645842000E-03));
        coords.add(new Coord3d(8.9,0.6,8.2740864600E-03));
        coords.add(new Coord3d(9.0,0.6,8.4874580500E-03));
        coords.add(new Coord3d(8.2,0.6,1.1235721700E-02));
        coords.add(new Coord3d(8.3,0.6,1.1469927400E-02));
        coords.add(new Coord3d(8.4,0.6,1.1706922500E-02));
        coords.add(new Coord3d(8.5,0.6,1.1944866900E-02));
        coords.add(new Coord3d(8.6,0.6,1.2185329900E-02));
        coords.add(new Coord3d(8.7,0.6,1.2428637100E-02));
        coords.add(new Coord3d(8.8,0.6,1.2673785000E-02));
        coords.add(new Coord3d(8.9,0.6,1.2920018800E-02));
        coords.add(new Coord3d(9.0,0.6,1.3170161800E-02));
        coords.add(new Coord3d(8.2,0.7,1.5214034500E-02));
        coords.add(new Coord3d(8.3,0.7,1.5455817900E-02));
        coords.add(new Coord3d(8.4,0.7,1.5700063400E-02));
        coords.add(new Coord3d(8.5,0.7,1.5944899600E-02));
        coords.add(new Coord3d(8.6,0.7,1.6190601200E-02));
        coords.add(new Coord3d(8.7,0.7,1.6437111000E-02));
        coords.add(new Coord3d(8.8,0.7,1.6684786200E-02));
        coords.add(new Coord3d(8.9,0.7,1.6934251900E-02));
        coords.add(new Coord3d(9.0,0.7,1.7184626500E-02));
        coords.add(new Coord3d(8.2,0.8,1.7568817600E-02));
        coords.add(new Coord3d(8.3,0.8,1.7798141000E-02));
        coords.add(new Coord3d(8.4,0.8,1.8029042000E-02));
        coords.add(new Coord3d(8.5,0.8,1.8260645700E-02));
        coords.add(new Coord3d(8.6,0.8,1.8492396300E-02));
        coords.add(new Coord3d(8.7,0.8,1.8725852900E-02));
        coords.add(new Coord3d(8.8,0.8,1.8959537100E-02));
        coords.add(new Coord3d(8.9,0.8,1.9193946300E-02));
        coords.add(new Coord3d(9.0,0.8,1.9429253900E-02));
        coords.add(new Coord3d(8.2,1.0,1.7592884000E-02));
        coords.add(new Coord3d(8.3,1.0,1.7817314600E-02));
        coords.add(new Coord3d(8.4,1.0,1.8040934500E-02));
        coords.add(new Coord3d(8.5,1.0,1.8266213800E-02));
        coords.add(new Coord3d(8.6,1.0,1.8492401200E-02));
        coords.add(new Coord3d(8.7,1.0,1.8718550100E-02));
        coords.add(new Coord3d(8.8,1.0,1.8945993100E-02));
        coords.add(new Coord3d(8.9,1.0,1.9174295500E-02));
        coords.add(new Coord3d(9.0,1.0,1.9404660000E-02));
        coords.add(new Coord3d(8.2,1.1,1.5615471000E-02));
        coords.add(new Coord3d(8.3,1.1,1.5846687600E-02));
        coords.add(new Coord3d(8.4,1.1,1.6079306000E-02));
        coords.add(new Coord3d(8.5,1.1,1.6311595400E-02));
        coords.add(new Coord3d(8.6,1.1,1.6545513900E-02));
        coords.add(new Coord3d(8.7,1.1,1.6779271200E-02));
        coords.add(new Coord3d(8.8,1.1,1.7014185200E-02));
        coords.add(new Coord3d(8.9,1.1,1.7250208000E-02));
        coords.add(new Coord3d(9.0,1.1,1.7485324800E-02));
        coords.add(new Coord3d(8.2,1.2,1.2780027400E-02));
        coords.add(new Coord3d(8.3,1.2,1.3012921500E-02));
        coords.add(new Coord3d(8.4,1.2,1.3247985500E-02));
        coords.add(new Coord3d(8.5,1.2,1.3483681500E-02));
        coords.add(new Coord3d(8.6,1.2,1.3719251300E-02));
        coords.add(new Coord3d(8.7,1.2,1.3957447800E-02));
        coords.add(new Coord3d(8.8,1.2,1.4196269600E-02));
        coords.add(new Coord3d(8.9,1.2,1.4435049300E-02));
        coords.add(new Coord3d(9.0,1.2,1.4675871500E-02));
        coords.add(new Coord3d(8.2,1.3,1.0057820100E-02));
        coords.add(new Coord3d(8.3,1.3,1.0277318400E-02));
        coords.add(new Coord3d(8.4,1.3,1.0498129000E-02));
        coords.add(new Coord3d(8.5,1.3,1.0719588200E-02));
        coords.add(new Coord3d(8.6,1.3,1.0944305700E-02));
        coords.add(new Coord3d(8.7,1.3,1.1168844400E-02));
        coords.add(new Coord3d(8.8,1.3,1.1396469800E-02));
        coords.add(new Coord3d(8.9,1.3,1.1624592500E-02));
        coords.add(new Coord3d(9.0,1.3,1.1854719800E-02));
        coords.add(new Coord3d(8.2,1.4,7.8212473400E-03));
        coords.add(new Coord3d(8.3,1.4,8.0152034900E-03));
        coords.add(new Coord3d(8.4,1.4,8.2110547800E-03));
        coords.add(new Coord3d(8.5,1.4,8.4095207200E-03));
        coords.add(new Coord3d(8.6,1.4,8.6099287800E-03));
        coords.add(new Coord3d(8.7,1.4,8.8126579100E-03));
        coords.add(new Coord3d(8.8,1.4,9.0167999700E-03));
        coords.add(new Coord3d(8.9,1.4,9.2247912500E-03));
        coords.add(new Coord3d(9.0,1.4,9.4319812300E-03));
        coords.add(new Coord3d(8.2,1.5,6.0821551400E-03));
        coords.add(new Coord3d(8.3,1.5,6.2488945900E-03));
        coords.add(new Coord3d(8.4,1.5,6.4180222600E-03));
        coords.add(new Coord3d(8.5,1.5,6.5899754700E-03));
        coords.add(new Coord3d(8.6,1.5,6.7636623100E-03));
        coords.add(new Coord3d(8.7,1.5,6.9399635400E-03));
        coords.add(new Coord3d(8.8,1.5,7.1187238600E-03));
        coords.add(new Coord3d(8.9,1.5,7.2999462400E-03));
        coords.add(new Coord3d(9.0,1.5,7.4832835700E-03));

        return coords;
    }
}
