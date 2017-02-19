package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.geom.Point2D;

import cs355.controller.TheController;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Square;

public class SquareClickHandler extends DragShapeClickHandler {

	public SquareClickHandler(TheController c) {
		super(c);
	}

	@Override
	public Shape buildShape() {
		if (firstPoint == null)
			return null;
		Point2D.Double endPoint = (finalPoint == null) ? lastPoint : finalPoint;
		double width = Math.abs(firstPoint.getX() - endPoint.getX());
		double height = Math.abs(firstPoint.getY() - endPoint.getY());
		double size = (width < height) ? width : height;
		double centerX = (firstPoint.getX() < endPoint.getX()) ? firstPoint.getX() + size/2 : firstPoint.getX() - size/2;
		double centerY = (firstPoint.getY() < endPoint.getY()) ? firstPoint.getY() + size/2 : firstPoint.getY() - size/2;
		Point2D.Double center = new Point2D.Double(centerX, centerY);
		return new Square(theController.getColor(), center, size);
	}

}
