/**
 * Groovy version of InteractiveScatterDemo.java .
 *   usage: groovy InteractiveScatterDemo.groovy [numPoints[=25000] widPoints[=1]]
 * @author John/af4ex
 */
package org.jzy3d.demos.interactive 

import java.awt.Graphics
import java.awt.Rectangle
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.Random

import org.jzy3d.chart.Chart
import org.jzy3d.chart.controllers.thread.ChartThreadController
import org.jzy3d.chart.controllers.mouse.AbstractChartMouseSelector
import org.jzy3d.chart.controllers.mouse.ChartMouseController
import org.jzy3d.chart.controllers.mouse.interactives.ScatterMouseSelector
import org.jzy3d.colors.Color
import org.jzy3d.maths.Coord3d
import org.jzy3d.plot3d.primitives.interactive.InteractiveScatter
import org.jzy3d.plot3d.rendering.view.Renderer2d
import org.jzy3d.ui.Plugs

def numPoints = this.args.size()>=1?this.args[0] as int: 25000
def widPoints = this.args.size()==2?this.args[1] as int: 1

/*------------------ Build an interactive scatter Chart object -----------------------*/

def buildChart(numPoints, widPoints)
{
   def chart = new Chart("swing")
   def String MESSAGE_SELECTION_MODE = "Selection mode (hold 'c' to control camera)"
   def String MESSAGE_ROTATION_MODE = "Rotation mode (release 'c' to make a selection)"
   def holding = false
   def String message = ""
   def genScatter = {int npt,wid ->
                       Coord3d[] points = new Coord3d[npt]
                       Color[] colors = new Color[npt]
                       Random rng = new Random()
                       (0..<npt).each
                       {i -> 
                          def (r,g,b,a) = [0f, 0f, 0f, 0.5f]
                          colors[i] = new Color(r,g,b,a)
                          def (x,y,z) = [rng.nextFloat(), rng.nextFloat(), rng.nextFloat()] 
                          points[i] = new Coord3d(x,y,z)
                       }
                       def dots = new InteractiveScatter(points, colors)
                       dots.setWidth(wid)
                       return dots
                    }
   def scatterData = genScatter(numPoints, widPoints)
   def mouseSelection = new ScatterMouseSelector(scatterData)
   def threadCamera = new ChartThreadController(chart)
   def mouseCamera = new ChartMouseController()
   def modeCam={ 
                 mouseSelection.releaseChart()
                 chart.addController(mouseCamera)
               }
   def modeRot={
                 chart.removeController(mouseCamera)
                 mouseSelection.attachChart(chart)
               }
   chart.getScene().add(scatterData)
   chart.getView().setMaximized(true)
        
   // Create and add controllers
   mouseCamera.addSlaveThreadController(threadCamera)
   chart.getCanvas().addKeyListener(new KeyListener()
   {
      public void keyPressed(KeyEvent e) { }
      public void keyReleased(KeyEvent e) 
      {
            switch ( e.getKeyChar())
            {
            case 'c': modeRot(); break;            
            default: break
            }

            holding = false
            message = MESSAGE_SELECTION_MODE
            chart.render() // update message display
      }

      public void keyTyped(KeyEvent e) 
      {
         if (!holding)
         {
            switch (e.getKeyChar())
            {
            case 'c': modeCam(); break;            
            default: break
            }
            holding = true
            message = MESSAGE_ROTATION_MODE
            chart.render()
         }
      }
   })

   modeRot() 
        
   chart.addRenderer([paint: { Graphics g ->
                                g.setColor(java.awt.Color.RED)
                                g.drawString(MESSAGE_SELECTION_MODE, 10, 30)  
                             }] as Renderer2d)
   return chart
} /* end buildChart() */
    
def chart = buildChart(numPoints, widPoints)
def rectangle=new Rectangle(0,200,400,400)
Plugs.frame(chart, rectangle, "Groovy Interactive Graphics Demo") 
