package org.jzy3d.plot3d.primitives.axes;

import org.jzy3d.factories.IJzyFactory;
import org.jzy3d.maths.BoundingBox3d;


public class AxeFactory implements IJzyFactory<IAxe>{

	public void forInput(BoundingBox3d box){
		this.box = box;
	}
	
	@Override
	public IAxe getInstance() {
		return new AxeBox(box);
	}
	
	protected BoundingBox3d box;
}
