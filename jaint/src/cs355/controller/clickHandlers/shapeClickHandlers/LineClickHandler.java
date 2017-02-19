package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.geom.Point2D;


import cs355.controller.TheController;
import cs355.model.drawing.Line;
import cs355.model.drawing.Shape;

public class LineClickHandler extends DragShapeClickHandler {
	
	public LineClickHandler(TheController c) {
		super(c);
	}

	@Override
	public Shape buildShape() {
		if (firstPoint == null)
			return null;
		Point2D.Double endPoint = (finalPoint == null) ? lastPoint : finalPoint; 
		Double relativeX = endPoint.getX() - firstPoint.getX();
		Double relativeY = endPoint.getY() - firstPoint.getY();
		Point2D.Double relativeEnd = new Point2D.Double(relativeX, relativeY);
		return new Line(theController.getColor(), firstPoint, relativeEnd);
	}

}
