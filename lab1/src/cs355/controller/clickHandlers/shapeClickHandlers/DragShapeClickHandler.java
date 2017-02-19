package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import cs355.controller.TheController;

public abstract class DragShapeClickHandler extends ShapeClickHandler {

	public DragShapeClickHandler(TheController c) {
		super(c);
	}

	protected Point2D.Double firstPoint;
	protected Point2D.Double finalPoint;

	public void mousePressed(MouseEvent e) {
		firstPoint = new Point2D.Double(e.getX(), e.getY());
		lastPoint = firstPoint;
		tempShapeToModel();
	}

	public void mouseReleased(MouseEvent e) {
		finalPoint = new Point2D.Double(e.getX(), e.getY());
		saveShapeToModel();
	}
	
	public void clean() {
		super.clean();
		firstPoint = null;
		finalPoint = null;
	}
}
