package org.jzy3d.demos.groovy.fred

import org.jzy3d.maths.Coord3d

def evt1 = new Event(0f, 0f, 0f, 3, new Coord3d(3f, 0f, 0f))
assert evt1.direction.x == 1f
assert evt1.direction.y == 0f
assert evt1.direction.z == 0f

def evt2 = new Event(0f, 0f, 1f, 3, new Coord3d(3f, 3f, 0f))
def s22 = (float)(Math.sqrt(2d)/2d)
assert MathUtils.eq(evt2.direction.x, s22)
assert MathUtils.eq(evt2.direction.y, s22)
assert evt2.direction.z == 0f

def sin120 = (float) Math.sin(2*Math.PI/3)
def cos120 = -0.5f
def evt3 = new Event(0f, sin120, cos120, 3, evt1.direction)
def evt4 = new Event(0f, -sin120, cos120, 3, evt1.direction)

// So 1,2,3,4 perfect triangle happening a t=3 moving in +x direction
def bigTr = new Triangle([evt2, evt3, evt4])
assert !bigTr.isFlat()
assert MathUtils.eq(bigTr.findCenter(), new Coord3d(0f, 0f, 0f))
assert MathUtils.eq(bigTr.finalDir(evt1.direction), new Coord3d(1f, 0f, 0f))
assert MathUtils.eq(bigTr.finalDir(evt2.direction), new Coord3d(1f, 0f, 0f))
assert MathUtils.eq(bigTr.finalDir(evt1.direction.mul(-1f)), new Coord3d(-1f, 0f, 0f))
assert MathUtils.eq(bigTr.finalDir(evt2.direction.mul(-1f)), new Coord3d(-1f, 0f, 0f))
assert bigTr.findEvent(1, evt1.direction) == null
assert MathUtils.eq(bigTr.findEvent(2, evt1.direction), new Coord3d((float)Math.sqrt(3), 0f, 0f))

def smallTr = new Triangle([evt1, evt3, evt4])
assert !smallTr.isFlat()
assert MathUtils.eq(smallTr.finalDir(evt1.direction), new Coord3d(1f, 0f, 0f))
assert MathUtils.eq(smallTr.finalDir(evt2.direction), new Coord3d(1f, 0f, 0f))
assert MathUtils.eq(smallTr.finalDir(evt1.direction.mul(-1f)), new Coord3d(-1f, 0f, 0f))
assert MathUtils.eq(smallTr.finalDir(evt2.direction.mul(-1f)), new Coord3d(-1f, 0f, 0f))
assert smallTr.findEvent(1, evt1.direction) == null
assert MathUtils.eq(smallTr.radius2(), 1f)
assert MathUtils.eq(smallTr.findCenter(), new Coord3d(0f, 0f, -1f))
assert MathUtils.eq(smallTr.findEvent(2, evt1.direction), new Coord3d((float)Math.sqrt(3), 0f, -1f))

/*
int ratio = 1
def st = new SpaceTime(ratio)
assert st.spaces.last().events.size() == 4
Calculator calculator = new Calculator(st)
calculator.calc()
assert st.spaces.last().events.size() == 4
*/
