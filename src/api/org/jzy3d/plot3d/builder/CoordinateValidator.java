package org.jzy3d.plot3d.builder;

/**
 * 
 * @author Mo
 */
public interface CoordinateValidator {
	float[] getX();
	float[] getY();
	float[][] get_Z_as_fxy();
}
