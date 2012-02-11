package org.jzy3d.demos.groovy.fred

import org.jzy3d.maths.Coord3d
import org.jzy3d.maths.Vector3d

/**
 * Date: 12/6/11
 * Time: 11:51 PM
 * @author Fred Simon
 */
class SpaceTime {
    int initialRatio
    int currentTime = 0
    Map<Integer, Space> spaces = [:]
    List<Event> allEvents = []

    SpaceTime(int ratio) {
        initialRatio = ratio
        init()
    }

    SpaceTime(Closure firstSpace) {
        Space first = new Space(currentTime)
        spaces.put(0, first)
        firstSpace.call(first)
    }

    def init() {
        Space first = new Space(currentTime)
        spaces.put(0, first)
        addPhoton(first,
                new Coord3d(0f, 0f, 0f),
                new Coord3d(1f, 0f, 0f),
                new Coord3d(0f, 0f, 1f),
                (float) initialRatio)
    }

    def static addPhoton(Space space, Coord3d c, Coord3d v, Coord3d p, float size) {
        p.normalizeTo(1f)
        v.normalizeTo(1f)
        // v and p needs to be perpendicular so cross should be normalized sin(teta)= 1
        Coord3d py = MathUtils.cross(v, p)
        if (!MathUtils.eq(py.magSquared(), 1f)) {
            throw new IllegalArgumentException("Polarization $v and vector $v are not perpendicular!");
        }
        space.addEvent(c, v)
        space.addEvent(c.add(p.mul(size)), v)
        Coord3d cos120 = p.mul((float) size * MathUtils.cos120)
        Coord3d sin120 = py.mul((float) size * MathUtils.sin120)
        space.addEvent(c.add(cos120).add(sin120), v)
        space.addEvent(c.add(cos120).sub(sin120), v)
    }

    Space addTime() {
        Space previous = spaces[currentTime]
        currentTime++
        Space newSpace = new Space(currentTime)
        spaces.put(currentTime, newSpace)
        if (previous != null && !previous.events.isEmpty()) allEvents.addAll(previous.events)
        return newSpace
    }

    List<Coord3d> currentPoints() {
        return allEvents.findAll { !it.used }.collect { it.point }
    }
}

class Event {
    final Coord3d point
    final Coord3d direction
    int time
    boolean used = false

    Event(float x, float y, float z, int time, Coord3d dir) {
//        this.point = new Coord3d((float)round(x),(float)round(y),(float)round(z))
        this.point = new Coord3d(x, y, z)
        this.time = time
        this.direction = dir.getNormalizedTo(1f)
    }

    Event(Coord3d point, int time, Coord3d dir) {
        this(point.x, point.y, point.z, time, dir)
    }
}

class Space {
    int time
    List<Event> events = []

    Space(int time) {
        this.time = time
    }

    def addEvent(float x, float y, float z, float xd, float yd, float zd) {
        def res = new Event(x, y, z, time, new Coord3d(xd, yd, zd))
        events.add(res)
        return res
    }

    def addEvent(Coord3d point, Coord3d direction) {
        def res = new Event(point, time, direction)
        events.add(res)
        return res
    }
}

class Triangle {
    List<Coord3d> p
    Coord3d p12
    Coord3d p13
    Coord3d p23
    Coord3d p12p23cross
    Float p12p13cross22

    Triangle(List<Event> p) {
        this(p[0].point, p[1].point, p[2].point)
    }

    Triangle(Coord3d p1, Coord3d p2, Coord3d p3) {
        this.p = []
        this.p << p1
        this.p << p2
        this.p << p3
    }

    Coord3d v12() {
        p12 != null ? p12 : (p12 = new Vector3d(p[0], p[1]).vector())
    }

    Coord3d v13() {
        p13 != null ? p13 : (p13 = new Vector3d(p[0], p[2]).vector())
    }

    Coord3d v23() {
        p23 != null ? p23 : (p23 = new Vector3d(p[1], p[2]).vector())
    }

    Coord3d v12v23cross() {
        p12p23cross != null ? p12p23cross : (p12p23cross = MathUtils.cross(v12(), v23()))
    }

    float v12v23s2() {
        p12p13cross22 != null ? p12p13cross22 : (p12p13cross22 = v12v23cross().magSquared() * 2f)
    }

    Coord3d finalDir(Coord3d origDirection) {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $v12v23s2 is too small!
                                                  Cannot find final direction of a flat triangle!""")
        }
        // Final direction (perpendicular to triangle plane) is cross vector
        Coord3d finalDir = v12v23cross().getNormalizedTo(1f)
        if (finalDir.dot(origDirection) < 0) {
            return finalDir.mul(-1f)
        }
        return finalDir
    }

    Coord3d findEvent(int dt, Coord3d origDirection) {
        if (isFlat()) {
            return null
        }
        float dt2 = dt * dt
        if (
                (v12().magSquared() > dt2) ||
                        (v13().magSquared() > dt2) ||
                        (v23().magSquared() > dt2)
        ) {
            // too big triangle for dt
            return null
        }
        // OK, it's a non flat triangle and dt is big enough => find center
        Coord3d center = findCenter()
        // Then find how much above plane on center need to add
        float radius2 = radius2()
        if (dt2 - radius2 < 1f) {
            throw new IllegalStateException("Don't know how to do math!")
        } else {
            float abovePlane = Math.sqrt(dt2 - radius2)
            return center.add(finalDir(origDirection).mul(abovePlane))
        }
    }

    float radius2() {
        return (float)(v12().magSquared() * v13().magSquared() * v23().magSquared() / (2f * v12v23s2()))
    }

    boolean isFlat() {
        return MathUtils.eq(v12v23s2(), 0f)
    }

    Coord3d findCenter() {
        if (isFlat()) {
            // Flat triangle
            throw new IllegalArgumentException("""Triangle $this is flat since $v12v23s2 is too small!
                                                   Cannot find center of a flat triangle!""")
        }
        // From "Barycentric coordinates from cross- and dot-products"
        float alpha = v23().magSquared() * v12().dot(v13()) / v12v23s2()
        float beta = v13().magSquared() * v12().mul(-1f).dot(v23()) / v12v23s2()
        float gama = v12().magSquared() * v13().mul(-1f).dot(v23().mul(-1f)) / v12v23s2()
        Coord3d center = new Coord3d(
                alpha * p[0].x + beta * p[1].x + gama * p[2].x,
                alpha * p[0].y + beta * p[1].y + gama * p[2].y,
                alpha * p[0].z + beta * p[1].z + gama * p[2].z
        )
        return center
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "p=" + p +
                ", p12=" + p12 +
                ", p13=" + p13 +
                ", p23=" + p23 +
                '}';
    }

}

class Calculator {
    static int N = 4

    List<Coord3d> fixedPoints
    SpaceTime spaceTime

    Calculator(int ratio) {
        spaceTime = new SpaceTime(ratio)
        initFixPoints()
    }

    Calculator(SpaceTime spaceTime) {
        this.spaceTime = spaceTime
        initFixPoints()
    }

    def initFixPoints() {
        fixedPoints = [
                new Coord3d(-50f, -50f, -50f).mul((float)spaceTime.initialRatio),
                new Coord3d(50f, 50f, 50f).mul((float)spaceTime.initialRatio)
        ]
    }

    Coord3d[] currentPoints() {
        List<Coord3d> points = spaceTime.currentPoints()
        points.addAll(fixedPoints)
        points.toArray(new Coord3d[points.size()])
    }

    def manyCalc(int n) {
        for (int i=0;i<n;i++) calc()
    }

    def calc() {
        if (spaceTime.currentTime % spaceTime.initialRatio == 0) println "Current time: ${spaceTime.currentTime}"
        print "."
        Space newSpace = spaceTime.addTime()
        // Go back in time one by one and find all group of N
        for (int dt = 1; dt <= spaceTime.currentTime; dt++) {
            List<Event> events = spaceTime.spaces[spaceTime.currentTime - dt]?.events
            if (events == null || events.size() < N) continue
            forAllN(events, 0, []) { List<Event> block ->
                // For all N blocks try to find new events
                Set<List<Event>> subsequences = block.subsequences()
                Set<List<Event>> allPairs = subsequences.findAll { it.size() == 2 }
                // If any 2 points of the blocks could not have dt time to join => toFar
                boolean toFar = allPairs.any { it[0].point.distance(it[1].point) > dt }
                // If 2 directions have more than 90deg angle (cos<0) no match
                boolean sameDir = allPairs.every { it[0].direction.dot(it[1].direction) > 0f }
                if (!toFar && sameDir) {
                    Set<List<Event>> allTriangles = subsequences.findAll { it.size() == 3 }
                    List<Event> newEvents = []
                    allTriangles.each {
                        // For each triangle find the equidistant ( = dt ) points
                        def tr = new Triangle(it)
                        Coord3d newPoint = tr.findEvent(dt, it[0].direction)
                        if (newPoint != null) newEvents.add(new Event(newPoint, spaceTime.currentTime, tr.finalDir(it[0].direction)))
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
        spaceTime.spaces.each { dt, Space space ->
            space.events.removeAll { it.used }
        }
        // Clean all used spaces
        spaceTime.spaces = spaceTime.spaces.findAll { !it.value.events.isEmpty() }
    }

    static def forAllN(List<Event> evtList, int idx, List<Event> block, Closure doStuff) {
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
