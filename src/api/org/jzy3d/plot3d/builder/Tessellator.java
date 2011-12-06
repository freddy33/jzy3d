package org.jzy3d.plot3d.builder;

import org.jzy3d.plot3d.primitives.AbstractDrawable;

/**
 * 
 * @author Mo
 */
public interface Tessellator {

	public void setX(final float[] x);

	public void setY(final float[] y);

	public void set_Z_as_fxy(final float[][] values);

	public AbstractDrawable buildDrawable();

}
