package org.jzy3d.picking;

import org.jzy3d.plot3d.primitives.IGLRenderer;
import org.jzy3d.plot3d.transform.Transform;

public interface IPickable extends IGLRenderer{
	public void setPickingId(int id);
	public int getPickingId();
	
	// required method from an AbstractDrawable
	public void setTransform(Transform transform);
}
