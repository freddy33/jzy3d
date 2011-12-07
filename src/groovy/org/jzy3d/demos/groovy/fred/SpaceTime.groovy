package org.jzy3d.demos.groovy.fred

import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.Vector3d
import static java.lang.Math.round

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
class SpaceTime {
    int currentTime = 0
    List<Space> spaces = []
    List<Event> allEvents = []

    SpaceTime() {
        init()
    }

    SpaceTime(Closure firstSpace) {
        Space first = new Space(currentTime)
        spaces.add(first)
        firstSpace.call(first)
    }

    def init() {
        Space first = new Space(currentTime)
        spaces.add(first)
        first.addEvent(0f,0f,0f)
        first.addEvent(0f,-10f,-10f)
        first.addEvent(0f,-10f,10f)
        first.addEvent(0f,10f,0f)
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
        this.point = new Coord3d((float)round(x),(float)round(y),(float)round(z))
        this.time = time
    }

    Event(Coord3d point, int time) {
        this(point.x,point.y,point.z,time)
    }
}

class Space {
    int time
    List<Event> events = []

    Space(int time) {
        this.time = time
    }

    List<Coord3d> currentPoints() {
        return events.collect { it.point }
    }

    def addEvent(float x, float y, float z) {
        def res = new Event(x, y, z, time)
        events.add(res)
        return res
    }
}

class Calculator {
    List<Coord3d> fixedPoints = [
            new Coord3d(-500f, -50f, -50f),
            new Coord3d(500f, 50f, 50f)
    ]
    int N = 4
    SpaceTime spaceTime

    Calculator() {
        spaceTime = new SpaceTime()
    }

    Calculator(SpaceTime spaceTime) {
        this.spaceTime = spaceTime
    }

    Coord3d[] currentPoints() {
        List<Coord3d> points = spaceTime.currentPoints()
        points.addAll(fixedPoints)
        points.toArray(new Coord3d[points.size()])
    }

    def calc() {
        println "Calculating next scene for time ${spaceTime.currentTime}"
        Space newSpace = spaceTime.addTime()
        // Go back in time one by one and find all group of N
        for (int dt=1; dt <= spaceTime.currentTime; dt++) {
            List<Event> events = spaceTime.spaces[spaceTime.currentTime - dt]?.events
            if (events == null || events.size() < N) continue
            forAllN(events,0,[]) { List<Event> block ->
                // For all N blocks try to find new events
                Set<List<Event>> subsequences = block.subsequences()
                Set<List<Event>> allPairs = subsequences.findAll { it.size() == 2 }
                // If any 2 points of the blocks could not have dt time to join => toFar
                boolean toFar = allPairs.any { it[0].point.distance(it[1].point) > dt }
                if (!toFar) {
                    Set<List<Event>> allTriangles = subsequences.findAll { it.size() == 3 }
                    List<Event> newEvents = []
                    allTriangles.each {
                        // For each triangle find the equidistant ( = dt ) points
                        Coord3d P1 = it[0].point
                        Coord3d P2 = it[1].point
                        Coord3d P3 = it[2].point
                        Vector3d P12 = new Vector3d(P1, P2)
                        Vector3d P13 = new Vector3d(P1, P3)
                        Coord3d cross = P12.cross(P13)
                        float P12cP23squared2 = 2* cross.magSquared()
                        if (P12cP23squared2 > 1e-10f) {
                            // OK, it's a non flat triangle => find center from
                            // "Barycentric coordinates from cross- and dot-products"
                            Vector3d P23 = new Vector3d(P2, P3)
                            float alpha = P23.vector().magSquared()*P12.dot(P13)/P12cP23squared2
                            float beta = P13.vector().magSquared()*P12.neg().dot(P23)/P12cP23squared2
                            float gama = P12.vector().magSquared()*P13.neg().dot(P23.neg())/P12cP23squared2
                            Coord3d center = new Coord3d(
                                    alpha* P1.x + beta* P2.x + gama* P3.x,
                                    alpha* P1.y + beta* P2.y + gama* P3.y,
                                    alpha* P1.z + beta* P2.z + gama* P3.z
                            )
                            float radius = P12.norm()*P23.norm()*P13.norm()/P12cP23squared2
                            // If radius almost dt, Center is the event
                            if (dt-radius < 0.5f) {
                                newEvents.add(new Event(center, spaceTime.currentTime))
                            } else {
                                float abovePlane = Math.sqrt(dt*dt - radius*radius)
                                Coord3d newEventPoint = center.add(cross.getNormalizedTo(abovePlane))
                                newEvents.add(new Event(newEventPoint, spaceTime.currentTime))
                            }
                        }
                    }
                    if (newEvents.size() == block.size()) {
                        // Conservation of events good
                        newSpace.events.addAll(newEvents)
                        block.each { it.used = true }
                    }
                }
            }
        }
        // Clean all used events
        spaceTime.spaces.each { Space space ->
            space.events.removeAll { it.used }
        }
        // Clean all used spaces
        spaceTime.spaces.removeAll { it.events.isEmpty() }
    }

    def forAllN(List<Event> evtList, int idx, List<Event> block, Closure doStuff) {
        evtList.each { Event evt ->
            if (!evt.used && !block.contains(evt)) {
                block[idx] = evt
                idx++
                if (idx == N) {
                    doStuff(block)
                } else {
                    forAllN(evtList, idx, block, doStuff)
                }
            }
        }
    }
}
