package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.geom.Point2D;

import cs355.controller.TheController;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;

public class CircleClickHandler extends DragShapeClickHandler {

	public CircleClickHandler(TheController c) {
		super(c);
	}

	@Override
	public Shape buildShape() {
		if (firstPoint == null)
			return null;
		Point2D.Double endPoint = (finalPoint == null) ? lastPoint : finalPoint;
		double width = Math.abs(firstPoint.getX() - endPoint.getX());
		double height = Math.abs(firstPoint.getY() - endPoint.getY());
		double diameter = (width < height) ? width : height;
		double radius = diameter/2;
		double centerX = (firstPoint.getX() < endPoint.getX()) ? firstPoint.getX() + radius : firstPoint.getX() - radius;
		double centerY = (firstPoint.getY() < endPoint.getY()) ? firstPoint.getY() + radius : firstPoint.getY() - radius;
		Point2D.Double center = new Point2D.Double(centerX, centerY);
		return new Circle(theController.getColor(), center, radius);
	}

}
