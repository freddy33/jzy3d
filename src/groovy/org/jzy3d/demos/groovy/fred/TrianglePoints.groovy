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

// G.var provides Global container for script vars
class G {
    static var = [:]
}

def buildChart(Scatter sc) {
    def chart = new Chart()
    sc.width = 5
    sc.color = Color.GREEN
    chart.getScene().add(sc)
    return chart
}

G.var.fpsText = "0 FPS"
G.var.keyHit = false
def rectangle = new Rectangle(600, 600)
def tt = new TicToc()
Scatter scatter = new Scatter()
Calculator calculator = new Calculator()
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
        if (next%25 == 0) calculator.calc()
        tt.toc()
        G.var.fpsText = String.format('%.4f FPS', 1.0 / tt.elapsedSecond())
    }
}
