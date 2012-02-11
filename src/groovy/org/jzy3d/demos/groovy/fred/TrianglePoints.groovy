/**
 * @author Fred Simon
 */
package org.jzy3d.demos.groovy.fred

import java.awt.Rectangle
import org.jzy3d.chart.Chart
import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.TicToc
import org.jzy3d.plot3d.primitives.Scatter
import org.jzy3d.ui.ChartLauncher
import org.jzy3d.colors.Color
import org.jzy3d.maths.Vector3d
import java.awt.Graphics2D
import org.jzy3d.plot3d.rendering.view.Renderer2d

// G.var provides Global container for script vars
class G {
    static var = [:]
}

def buildChart(Scatter sc) {
    def chart = new Chart()
    sc.width = 5
    sc.color = Color.GREEN
    chart.getScene().add(sc)

    chart.addRenderer(
    {g ->
       Graphics2D g2d = (Graphics2D)g
       g2d.setColor(java.awt.Color.BLACK)
       g2d.drawString(G.var.fpsText, 50, 50)
    } as Renderer2d)

    return chart
}

G.var.fpsText = "0 FPS"
G.var.keyHit = false
def rectangle = new Rectangle(600, 600)
def tt = new TicToc()
Scatter scatter = new Scatter()
Calculator calculator = new Calculator(100)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(1000f, 0f, 0f),
        new Coord3d(-1f, 0f, 0f),
        new Coord3d(0f, 0f, 1f),
        100f
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(-1000f, 0f, 0f),
        new Coord3d(1f, 0f, 0f),
        new Coord3d(0f, 0f, 1f),
        100f
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, -1000f, 0f),
        new Coord3d(0f, 1f, 0f),
        new Coord3d(1f, 0f, 0f),
        100f
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, 1000f, 0f),
        new Coord3d(0f, -1f, 0f),
        new Coord3d(1f, 0f, 0f),
        100f
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, 0f, -1000f),
        new Coord3d(0f, 0f, 1f),
        new Coord3d(0f, 1f, 0f),
        100f
)
SpaceTime.addPhoton(calculator.spaceTime.spaces[0],
        new Coord3d(0f, 0f, 1000f),
        new Coord3d(0f, 0f, -1f),
        new Coord3d(0f, 1f, 0f),
        100f
)
scatter.setData(calculator.currentPoints())
def chart = buildChart(scatter)
ChartLauncher.openChart(chart, rectangle, "Triangles", false)

Thread.start {
    int next = 0
    while (!G.var.keyHit) {
        next++
        sleep 25
        tt.tic()
        scatter.setData(calculator.currentPoints())
        chart.render()
        calculator.manyCalc(10)
        tt.toc()
        G.var.fpsText = String.format('%4d: %.4f FPS', calculator.spaceTime.currentTime, 1.0 / tt.elapsedSecond())
    }
}
