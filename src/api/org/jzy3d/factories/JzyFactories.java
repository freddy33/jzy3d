package org.jzy3d.factories;

import org.jzy3d.plot3d.primitives.axes.AxeFactory;
import org.jzy3d.plot3d.rendering.ordering.OrderingStrategyFactory;


public class JzyFactories {
	public static OrderingStrategyFactory ordering = new OrderingStrategyFactory();
	public static AxeFactory axe = new AxeFactory();
}
