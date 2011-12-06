/**
 * Groovy version of AnimatedSurfaceDemo.java 
 *   usage: groovy AnimatedSurfaceDemo.groovy [mapperParm [range [steps]]] 
 *                                       defaults=[0.9 [-150..150 [50]]]
 * @author John/af4ex
 */
package org.jzy3d.demos.groovy.animation

import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Rectangle
import java.util.List
import java.awt.event.KeyListener
import org.jzy3d.chart.Chart
import org.jzy3d.colors.Color
import org.jzy3d.colors.ColorMapper
import org.jzy3d.colors.colormaps.ColorMapRainbow
import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.Range
import org.jzy3d.maths.Scale
import org.jzy3d.maths.TicToc
import org.jzy3d.maths.Utils
import org.jzy3d.plot3d.builder.Builder
import org.jzy3d.plot3d.builder.Mapper
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid
import org.jzy3d.plot3d.primitives.AbstractDrawable
import org.jzy3d.plot3d.primitives.Point
import org.jzy3d.plot3d.primitives.Polygon
import org.jzy3d.plot3d.primitives.Shape
//import org.jzy3d.plot3d.primitives.faces.ColorbarFace
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.ChartLauncher
import static java.lang.Math.cos
import static java.lang.Math.sin

// G.var provides Global container for script vars
class G { static var=[:] }

/*---------- Define parameterized mapper -----------------*/

class ParameterizedMapper extends Mapper
{
   def Param=1D
   double f(double x, double y)    
   {
      10D*Math.sin(x*Param)*Math.cos(y*Param)*x
   }
} /* end class ParameterizedMapper */ 

/*------- Class for function-plotting Chart --------------*/

class FnChart extends Chart
{
   public void setScale(Scale scale)
   {
      super.setScale(scale)
      ColorMapper cm = surface.getColorMapper()
      cm.setScale(scale)
      surface.setColorMapper(cm)
   }
} /* end class FnChart */

/*-------------- Remapper function ------------------------*/

def remap(shape, mapper)
{
   List<AbstractDrawable> polygons = shape.getDrawables()
   for (d in polygons)
   {
      def p= (Polygon)d
      for (i in 0..<p.size())
      {
         def pt = p.get(i)
         def c = pt.xyz
         c.z = (float)mapper.f(c.x, c.y)
      }
   }
} /* end remap() */

/*---------- Build function-plotting chart ----------------*/

def buildChart(mapper, range, steps)
{
   def grid = new OrthonormalGrid(range, steps, range, steps)
   def surf = (Shape)Builder.buildOrthonormal(grid, mapper)
   def fnChart = new FnChart()

   // Create surface to map the function over the given range.
   surf.setColorMapper(new ColorMapper(new ColorMapRainbow(), 
                                       surf.getBounds().getZmin(), 
                                       surf.getBounds().getZmax(), 
                                       new Color(1,1,1,0.5f)))
   surf.setFaceDisplayed(true)
   surf.setWireframeDisplayed(true)
   surf.setWireframeColor(Color.BLACK)

   // Setup a colorbar for the surface object and add it to the scene
   //def zTR = fnChart.getView().getAxe().getLayout().getZTickRenderer() 
   //def zTP = fnChart.getView().getAxe().getLayout().getZTickProvider()
   //def face = new ColorbarFace(surf, zTR, zTP) 
   //surf.setFace(face) 
   fnChart.getScene().getGraph().add(surf)

   // add renderer to display FPS text
   fnChart.addRenderer(
   {g -> 
      Graphics2D g2d = (Graphics2D)g
      g2d.setColor(java.awt.Color.BLACK)
      g2d.drawString(G.var.fpsText, 50, 50)
   } as Renderer2d)

   // listen for any key to set keyHit flag
   fnChart.getCanvas().addKeyListener(
   [
        keyTyped: {G.var.keyHit=true}, keyPressed: {}, keyReleased: {}
   ] as KeyListener)

   return [surf, fnChart] 
} /* end buildChart() */

/*--------------------- Command-line interface --------------------*/
G.var.fpsText  = "0 FPS"
G.var.keyHit   = false
def expr       = new GroovyShell()
def param      = this.args.size()>=1?args[0] as float:0.9f
def range      = this.args.size()>=2?expr.evaluate(args[1]):-150..150
def steps      = this.args.size()==3?args[2] as int:50
def rectangle  = new Rectangle(600,600)
def mapper     = new ParameterizedMapper(Param: param)
def tt         = new TicToc()

// Need to convert Groovy range to jzy Range
def jzyRange = new Range(range.getFrom(), range.getTo())
def (surf, fnChart) = buildChart(mapper, jzyRange, steps)
ChartLauncher.openChart(fnChart, rectangle, "Animation", false)

Thread.start
{
   while (!G.var.keyHit)
   {
      sleep 25   // function parens are optional in Groovy (like Perl) 
      tt.tic()
         mapper.setParam(mapper.getParam() + 0.0001)
         remap(surf, mapper)
         fnChart.render()
      tt.toc()
      G.var.fpsText = String.format('%.4f FPS', 1.0/tt.elapsedSecond())
   } 
} /* end Thread.start */

