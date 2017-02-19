package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.geom.Point2D;


import cs355.controller.TheController;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;

public class RectangleClickHandler extends DragShapeClickHandler {

	public RectangleClickHandler(TheController c) {
		super(c);
	}

	@Override
	public Shape buildShape() {
		if (firstPoint == null)
			return null;
		Point2D.Double endPoint = (finalPoint == null) ? lastPoint : finalPoint;
		double upperLeftX = (firstPoint.getX() < endPoint.getX()) ? firstPoint.getX() : endPoint.getX();
		double upperLeftY = (firstPoint.getY() < endPoint.getY()) ? firstPoint.getY() : endPoint.getY();
		double width = Math.abs(firstPoint.getX() - endPoint.getX());
		double height = Math.abs(firstPoint.getY() - endPoint.getY());
		Point2D.Double center = new Point2D.Double(upperLeftX + width/2, upperLeftY + height/2);
		return new Rectangle(theController.getColor(), center, width, height);
	}

}
