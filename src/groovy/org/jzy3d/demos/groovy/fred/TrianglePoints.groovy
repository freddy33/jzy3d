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

// G.var provides Global container for script vars
class G {
    static var = [:]
}

class SpaceTime {
    int currentTime = 0
    List<Space> spaces = []
    List<Event> allEvents = []

    SpaceTime() {
        init()
    }

    def init() {
        Space first = new Space(currentTime)
        spaces.add(first)
        first.addEvent(0f,0f,0f)
        first.addEvent(0f,-1f,-1f)
        first.addEvent(0f,-1f,1f)
        first.addEvent(0f,1f,0f)
    }
    
    Space addTime() {
        Space previous = spaces.last()
        currentTime++
        Space newSpace = new Space(currentTime)
        spaces << newSpace
        allEvents.addAll(previous.events)
        return newSpace
    }

    Coord3d[] currentPoints() {
        spaces.last().currentPoints()
    }
}

class Event {
    final Coord3d point
    int time
    boolean used = false

    Event(float x, float y, float z, int time) {
        this.point = new Coord3d(x,y,z)
        this.time = time
    }
}

class Space {
    int time
    List<Event> events = []

    Space(int time) {
        this.time = time
    }

    Coord3d[] currentPoints() {
        return events.collectAll { events.point }
    }
    
    def addEvent(float x, float y, float z) {
        def res = new Event(x, y, z, time)
        events.add(res)
        return res
    }
}

class Calculator {
    int N = 4
    SpaceTime spaceTime = new SpaceTime()
    Scatter scatter = new Scatter()

    def render() {
        scatter.setData(spaceTime.currentPoints())
    }

    def calc() {
        Space newSpace = spaceTime.addTime()
        findOne(0,[]) { List<Event> tri ->
            
        }
    }

    def findOne(int idx, List<Event> tri, Closure doStuff) {
        spaceTime.allEvents.each { Event evt ->
            if (!evt.used && !tri.contains(evt)) {
                tri[idx] = evt
                idx++
                if (idx == N) {
                    doStuff(tri)
                } else {
                    findOne(idx, tri, doStuff)
                }
            }
        }
    }
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
Calculator calculator = new Calculator()
def chart = buildChart(calculator.scatter)
ChartLauncher.openChart(chart, rectangle, "Triangles", false)

Thread.start {
    while (!G.var.keyHit) {
        sleep 500
        tt.tic()
        calculator.render()
        chart.render()
        calculator.calc()
        tt.toc()
        G.var.fpsText = String.format('%.4f FPS', 1.0 / tt.elapsedSecond())
    }
}
