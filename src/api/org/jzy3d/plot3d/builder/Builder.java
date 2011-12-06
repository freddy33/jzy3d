package org.jzy3d.plot3d.builder;

import java.util.List;

import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Coordinates;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.builder.concrete.OrthonormalTesselator;
import org.jzy3d.plot3d.builder.delaunay.DelaunayCoordinateValidator;
import org.jzy3d.plot3d.builder.delaunay.DelaunayTessellator;
import org.jzy3d.plot3d.builder.delaunay.OrthonormalCoordinateValidator;
import org.jzy3d.plot3d.builder.delaunay.Triangulation;
import org.jzy3d.plot3d.builder.delaunay.jdt.Delaunay_Triangulation;
import org.jzy3d.plot3d.primitives.AbstractDrawable;


public class Builder {

	public static AbstractDrawable build(Grid grid, Mapper mapper, Tesselator tesselator){
		return tesselator.build(grid.apply(mapper));
	}
	
	public static AbstractDrawable build(List<Coord3d> coordinates, Tesselator tesselator){
		return tesselator.build(coordinates);
	}
	
	public static AbstractDrawable build(float[] x, float[] y, float[] z, Tesselator tesselator){
		return tesselator.build(x, y, z);
	}
	
	/*******************************************************************************/

	//TODO: review these additions
	public static AbstractDrawable build(Grid grid, Mapper mapper, Tessellator tesselator) {
		CoordinateValidator cv = new OrthonormalCoordinateValidator(new Coordinates(grid.apply(mapper)));
		return buildDrawable(tesselator, cv);
	}

	public static AbstractDrawable build(List<Coord3d> coordinates, Tessellator tesselator) {
		CoordinateValidator cv = new OrthonormalCoordinateValidator(new Coordinates(coordinates));
		return buildDrawable(tesselator, cv);
	}

	public static AbstractDrawable build(float[] x, float[] y, float[] z, Tessellator tesselator) {
		CoordinateValidator cv = new OrthonormalCoordinateValidator(new Coordinates(x, y, z));
		return buildDrawable(tesselator, cv);
	}

	/*******************************************************************************/

	public static AbstractDrawable buildDelaunay(List<Coord3d> coordinates) {
		CoordinateValidator cv = new DelaunayCoordinateValidator(new Coordinates(coordinates));
		Triangulation dt = new Delaunay_Triangulation();
		Tessellator tesselator = new DelaunayTessellator(cv, dt);
		return tesselator.buildDrawable();

	}

	// public static Drawable buildOrthonormal(OrthonormalGrid grid, Mapper
	// mapper){
	// CoordinateValidator cv = new OrthonormalCoordinateValidator(new
	// Coordinates(grid.apply(mapper)));
	// Tessellator tesselator = new OrthonormalTesselator(cv);
	// return tesselator.buildDrawable();
	// }

	public static AbstractDrawable buildOrthonormal(OrthonormalGrid grid, Mapper mapper) {
		OrthonormalTesselator tesselator = new OrthonormalTesselator();
		return tesselator.build(grid.apply(mapper));
	}

	/*******************************************************************************/
	private static AbstractDrawable buildDrawable(Tessellator tesselator, CoordinateValidator cv) {
		tesselator.setX(cv.getX());
		tesselator.setY(cv.getY());
		tesselator.set_Z_as_fxy(cv.get_Z_as_fxy());
		return tesselator.buildDrawable();
	}

	/*
	 * public static Drawable buildRing(OrthonormalGrid grid, Mapper mapper){
	 * RingTesselator tesselator = new RingTesselator(); return
	 * tesselator.build(grid.apply(mapper)); }
	 */
}
