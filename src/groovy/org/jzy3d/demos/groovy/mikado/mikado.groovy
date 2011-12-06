/**  Groovy version of mikado.java demo
 *   usage: groovy mikado.groovy [numlines]
 *   @author John/af4ex
 */
package org.jzy3d.demos.groovy.mikado

import java.awt.Rectangle
import java.util.List
import java.util.Random
import java.util.Vector
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.AbstractDrawable
import org.jzy3d.plot3d.primitives.LineStrip
import org.jzy3d.plot3d.primitives.Point
import org.jzy3d.ui.ChartLauncher

/** Show how to add/remove elements from the scene graph at runtime.
 *  Display slowness due to a rendering update after each drawn line.
 */
        
def numlines = this.args.size()>=1?this.args[0] as int: 1000 
def chart    = new Chart()
def rng      = new Random()
List<AbstractDrawable> memory = new Vector<AbstractDrawable>()
ChartLauncher.openChart(chart, new Rectangle(800,600), "Mikado", true)
                
// Add numline random lines
for (k in 0..<numlines)
{
   def line = new LineStrip()
   def (x,y,z) = [rng.nextFloat(),rng.nextFloat(),rng.nextFloat()]
   def (r,g,b) = [rng.nextFloat(),rng.nextFloat(),rng.nextFloat()]
   line.add(new Point(new Coord3d(x,y,z), new Color(r,g,b)))
   (x,y,z) = [rng.nextFloat(),rng.nextFloat(),rng.nextFloat()]
   (r,g,b) = [rng.nextFloat(),rng.nextFloat(),rng.nextFloat()]
   line.add(new Point(new Coord3d(x,y,z), new Color(r,g,b)))
   chart.getScene().getGraph().add(line)                  
   memory.add(line)
}
                
// Remove all lines
for (k in (numlines-1)..0)
   chart.getScene().getGraph().remove(memory.get(k))
