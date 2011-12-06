/**
 * SimpleSurfaceDemo.groovy
 *   usage: groovy SimpleSurfaceDemo.groovy 
 * @author John/af4ex (original coded in Scala by Aurimas Anskaitis)
 */

package org.jzy3d.demos.groovy.animation

import org.jzy3d.chart.*
import org.jzy3d.colors.*
import org.jzy3d.colors.colormaps.*
import org.jzy3d.plot3d.builder.*
import org.jzy3d.plot3d.builder.concrete.*
import org.jzy3d.plot3d.primitives.Shape
import org.jzy3d.maths.Range
import java.awt.Rectangle
import org.jzy3d.ui.ChartLauncher

//========================================================
class ParmMapper extends Mapper
{
   double f(double x, double y)
   { 
      def sigma = 15
      return -x*x-y*y
   }
}
// Define range and precision for the function to plot
range = new Range(-1.0, 1.0)
steps = 50
surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps),
                                   new ParmMapper()) as Shape
surface.setColorMapper(new ColorMapper(new ColorMapRainbow(),
                       surface.getBounds().getZmin(),
                       surface.getBounds().getZmax()))
surface.setFaceDisplayed(true)
surface.setWireframeDisplayed(true)
surface.setWireframeColor(Color.BLACK)
chart = new Chart()
chart.getScene().getGraph().add(surface)
ChartLauncher.openChart(chart, new Rectangle(0,0,800,800), "")

