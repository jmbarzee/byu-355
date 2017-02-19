package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.geom.Point2D;

import cs355.controller.TheController;

public abstract class DragShapeClickHandler extends ShapeClickHandler {

	public DragShapeClickHandler(TheController c) {
		super(c);
	}

	protected Point2D.Double firstPoint;
	protected Point2D.Double finalPoint;

	public void mousePressed(Point2D.Double wLoc) {
		firstPoint = wLoc;
		lastPoint = firstPoint;
		tempShapeToModel();
	}

	public void mouseReleased(Point2D.Double wLoc) {
		finalPoint = wLoc;
		saveShapeToModel();
	}
	
	public void clean() {
		super.clean();
		firstPoint = null;
		finalPoint = null;
	}
}
