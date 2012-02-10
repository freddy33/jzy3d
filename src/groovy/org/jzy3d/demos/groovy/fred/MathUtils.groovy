package org.jzy3d.demos.groovy.fred

import org.jzy3d.maths.Coord3d

/**
 * Created by IntelliJ IDEA.
 * User: freds
 * Date: 2/10/12
 * Time: 8:25 PM
 * To change this template use File | Settings | File Templates.
 */
class MathUtils {
    static float sin120 = (float) Math.sin(2*Math.PI/3)
    static float cos120 = -0.5f

    static boolean eq(float a, float b) {
        Math.abs(a-b) < 1e-6f
    }

    static boolean eq(Coord3d a, Coord3d b) {
        eq(a.x, b.x) && eq(a.y, b.y) && eq(a.z, b.z)
    }

    static Coord3d cross(Coord3d v1, Coord3d v2){
        Coord3d v3 = new Coord3d();
        // V1    V2  =  V3
        v3.x = v1.y * v2.z - v1.z * v2.y; // x1    x2     x3  <-
        v3.y = v1.z * v2.x - v1.x * v2.z; // y1 \/ y2     y3
        v3.z = v1.x * v2.y - v1.y * v2.x; // z1 /\ z2     z3

        return v3;
    }
}
