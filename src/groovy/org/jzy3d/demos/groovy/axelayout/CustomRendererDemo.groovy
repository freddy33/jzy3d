/**
 *  Groovy version of CustomRendererDemo.java
 *  @author John/af4ex 
 */
package org.jzy3d.demos.groovy.axelayout

import java.awt.Rectangle
import org.jzy3d.maths.Utils
import org.jzy3d.plot3d.primitives.FlatLine2d
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.colors.ColorMapper
import org.jzy3d.colors.colormaps.ColorMapWhiteRed
import org.jzy3d.maths.Utils
import org.jzy3d.plot3d.primitives.axes.layout.renderers.DateTickRenderer
import org.jzy3d.plot3d.primitives.axes.layout.renderers.ScientificNotationTickRenderer
import org.jzy3d.ui.ChartLauncher

LENGTH=15
x=new float[LENGTH]
y=new float[LENGTH]
rng=new Random()
date=new Date()

(0..<LENGTH).each  // same as (0..LENGTH-1).each 
{i ->
   x[i] = (float) Utils.dat2num(date++)
   y[i] = rng.nextFloat()
}

line2d = new FlatLine2d(x,y, 10f)
line2d.setColorMapper(new ColorMapper(new ColorMapWhiteRed(), 0f, 1f))
line2d.setWireframeDisplayed(true)
line2d.setWireframeColor(Color.BLACK)

chart = new Chart()
chart.getScene().getGraph().add(line2d)
chart.getAxeLayout().setXAxeLabelDisplayed(false)
chart.getAxeLayout().setXTickLabelDisplayed(false)
chart.getAxeLayout().setYAxeLabel("Date")
chart.getAxeLayout().setYTickRenderer(new DateTickRenderer("dd/MM/yyyy"))
chart.getAxeLayout().setZAxeLabel("Ratio")
chart.getAxeLayout().setZTickRenderer(new
ScientificNotationTickRenderer(2))

ChartLauncher.openChart(chart, new Rectangle(800, 600), "Groovy CustomRendererDemo", true) 
