package org.jzy3d.plot3d.primitives.interactive;

import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import org.jzy3d.chart.controllers.mouse.AbstractChartMouseSelector;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.view.Camera;
import org.jzy3d.plot3d.rendering.view.View;


/** An {@link IInteractive} object is supposed to be able to compute its projection
 * on the screen according to the {@link Camera} settings (viewport, viewpoint, etc).
 * 
 * The {@link View} provides a mean to update the projection of all {@link IInteractive}
 * held by the scene's {@link Graph}. Indeed, interaction sources such as an
 * {@link AbstractChartMouseSelector} are supposed to call {@link view.project()}
 * to query an update of all {@link IInteractive} objects' projections.
 * 
 * Projection implementation is rather fast but one should consider projecting only
 * when required, i.e. when the actual scene's Graph projection changes, which is:
 * - when view point changes
 * - when view scaling changes
 * - when the objects data (shape) changes
 * 
 * So the user is responsible of handling appropriate calls to view.project()
 * 
 * @author Martin Pernollet
 */
public interface IInteractive {
	public void project(GL2 gl, GLU glu, Camera cam);
	public java.awt.Polygon getHull2d();
	public List<Coord3d> getLastProjection();
}
