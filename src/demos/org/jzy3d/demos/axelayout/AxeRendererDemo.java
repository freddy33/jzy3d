package org.jzy3d.demos.axelayout;

import java.util.Date;
import java.util.Random;

import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapWhiteRed;
import org.jzy3d.demos.AbstractDemo;
import org.jzy3d.demos.Launcher;
import org.jzy3d.maths.Utils;
import org.jzy3d.plot3d.primitives.FlatLine2d;
import org.jzy3d.plot3d.primitives.axes.AxeBox;
import org.jzy3d.plot3d.primitives.axes.layout.providers.StaticTickProvider;
import org.jzy3d.plot3d.primitives.axes.layout.renderers.DateTickRenderer;
import org.jzy3d.plot3d.text.ITextRenderer;
import org.jzy3d.plot3d.text.renderers.TextBillboardRenderer;
import org.jzy3d.plot3d.text.renderers.jogl.JOGLTextRenderer;
import org.jzy3d.plot3d.text.renderers.jogl.ShadowedTextStyle;


public class AxeRendererDemo extends AbstractDemo{
	public static int LENGTH = 15;
	public static void main(String[] args) throws Exception{
		Launcher.openDemo(new AxeRendererDemo());
	}
	
	public AxeRendererDemo(){
		float[] x = new float[LENGTH];
		float[] y = new float[LENGTH];
		
		Date date = new Date();
		Random rng = new Random();		
		for (int i = 0; i < x.length; i++) {
			x[i] = (float)Utils.dat2num(date);
			y[i] = rng.nextFloat();
			date = incrementDate( date );
		}
		
		FlatLine2d line2d = new FlatLine2d(x, y, 10f);
		line2d.setColorMapper( new ColorMapper( new ColorMapWhiteRed(), 0f, 1f ) );
		line2d.setWireframeDisplayed(true);
		line2d.setWireframeColor(Color.BLACK);
		
		chart = new Chart();
		chart.getScene().getGraph().add( line2d );	
		chart.getAxeLayout().setXAxeLabelDisplayed(false);
		chart.getAxeLayout().setXTickLabelDisplayed(false);
		chart.getAxeLayout().setYAxeLabel( "Date" );
		chart.getAxeLayout().setYTickRenderer( new DateTickRenderer( "dd/MM/yyyy" ) );
		chart.getAxeLayout().setZAxeLabel( "Ratio" );
		//chart.getAxeLayout().setZTickRenderer( new ScientificNotationTickRenderer(2) );	

		float[] ticks = {0f,0.5f,1f};
		chart.getAxeLayout().setZTickProvider( new StaticTickProvider(ticks) );
		
		AxeBox box = (AxeBox)chart.getView().getAxe();
		ITextRenderer renderer2 = new JOGLTextRenderer(new ShadowedTextStyle(72f, 10, java.awt.Color.RED, java.awt.Color.CYAN));
		ITextRenderer renderer3 = new TextBillboardRenderer();
		box.setTextRenderer(renderer3);
	}
		
	public Chart getChart(){
		return chart;
	}
		
	@SuppressWarnings("deprecation")
	protected Date incrementDate(Date d){
		Date d2 = new Date(d.getTime());
		//d2.setYear(d2.getYear()+1);
		d2.setMonth( d2.getMonth()+1 );
		return d2;
	}
	
	protected Chart chart;
}
