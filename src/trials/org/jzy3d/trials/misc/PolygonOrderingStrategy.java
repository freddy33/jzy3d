package org.jzy3d.trials.misc;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.Polygon;
import org.jzy3d.plot3d.rendering.ordering.BarycentreOrderingStrategy;
import org.jzy3d.plot3d.rendering.view.Camera;


/** 
 * The {@link PolygonOrderingStrategy} enhances the {@link BarycentreOrderingStrategy}
 * by applying a specific ordering policy when the input pair of {@link AbstractDrawable} to be
 * compared is a pair of {@link Polygon}s.
 * 
 * When comparing two {@link Polygon}s, their relative order is obtained by comparing
 * the farest point of {@link AbstractDrawable} d1 to the nearest point of {@link AbstractDrawable} d2.
 * 
 * The {@link PolygonOrderingStrategy} requires a reference to a {@link Camera} for
 * computing the distance to it, and optionnaly a common {@link Transform} applied to 
 * both {@link AbstractDrawable}s.
 * 
 * @author Martin Pernollet
 */
public class PolygonOrderingStrategy extends BarycentreOrderingStrategy{
	
	public int compare(AbstractDrawable d1, AbstractDrawable d2) {
		if(camera == null)
			throw new RuntimeException("No available camera for computing BarycentreOrderingStrategy");
		
		if(d1 instanceof Polygon && d2 instanceof Polygon)
			return compareFarestAndFarest((Polygon)d1, (Polygon)d2); // compare polygons smartly
		else
			return super.compare(d1, d2); // or other kind of drawable using their barycentre
	}
	
	
	public int compareFarestAndFarest(Polygon p1, Polygon p2) {
		// must find FAREST point of d1
		double dist1 = 0;
		double tmp1;
		for(int i1=0; i1<p1.size(); i1++){
			Coord3d point = p1.get(i1).xyz;
			if(transform!=null)
				point = transform.compute( point );
			
			tmp1 = point.distance(camera.getEye());
			if(tmp1 > dist1)
				dist1 = tmp1;
		}
		// must find CLOSEST point of d2
		double dist2 = 0;
		double tmp2;
		for(int i2=0; i2<p2.size(); i2++){
			Coord3d point = p2.get(i2).xyz;
			if(transform!=null)
				point = transform.compute( point );
			
			tmp2 = point.distance(camera.getEye());
			if(tmp2 > dist2)
				dist2 = tmp2;
		}
		
		return comparison(dist1, dist2);
	}
	
	/*public int compare(Polygon p1, Polygon p2) {
		// must find FAREST point of d1
		double dist1 = 0;
		double tmp1;
		for(int i1=0; i1<p1.size(); i1++){
			Coord3d point = p1.get(i1).xyz;
			if(transform!=null)
				point = transform.compute( point );
			
			tmp1 = point.distance(camera.getEye());
			if(tmp1 > dist1)
				dist1 = tmp1;
		}
		// must find CLOSEST point of d2
		double dist2 = Double.MAX_VALUE;
		double tmp2;
		for(int i2=0; i2<p2.size(); i2++){
			Coord3d point = p2.get(i2).xyz;
			if(transform!=null)
				point = transform.compute( point );
			
			tmp2 = point.distance(camera.getEye());
			if(tmp2 < dist2)
				dist2 = tmp2;
		}
		
		return comparison(dist1, dist2);
	}*/
}
