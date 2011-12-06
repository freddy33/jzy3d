package org.jzy3d.plot3d.rendering.ordering;

import org.jzy3d.factories.IJzyFactory;

public class OrderingStrategyFactory implements IJzyFactory<AbstractOrderingStrategy>{
	@Override
	public AbstractOrderingStrategy getInstance() {
		return DEFAULT;
	}
	
	public static AbstractOrderingStrategy DEFAULT = new BarycentreOrderingStrategy();
}
