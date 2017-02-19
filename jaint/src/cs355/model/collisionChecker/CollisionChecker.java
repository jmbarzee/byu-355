package cs355.model.collisionChecker;

import java.awt.geom.Point2D;

import cs355.model.drawing.Line;
import cs355.model.drawing.Shape;
import cs355.view.drawer.DrawingFactory;

public class CollisionChecker {
	private CollisionChecker() {
	}

	public static boolean check(Shape s, Point2D.Double worldLoc) {
		Point2D.Double shapeLoc = new Point2D.Double();
		DrawingFactory.getWorldToObj(s).transform(worldLoc, shapeLoc);
		
		if (s instanceof Line) {
			double range = 10;
			double x = shapeLoc.getX();
			double y = shapeLoc.getY();
			Line line = (Line) s;
			Point2D.Double start = new Point2D.Double(0, 0);
			Point2D.Double end = line.getEnd();
			double aX = start.getX();
			double aY = start.getY();
			double bX = end.getX();
			double bY = end.getY();
			double slopeOfAB = (aY - bY) / (aX - bX);
			// find the slope of line perpendicular to AB
			double slopePerpToAB = -1 / slopeOfAB; 
			// find the y-intercept of line perpendicular to AB through the test point
			double bOfPerp = y - slopePerpToAB*x;
			// find the intersection of AB and the perpendicular line passing through (0,0)
			double xOnAB = bOfPerp / (slopeOfAB - slopePerpToAB);
			double yOnAB = xOnAB * slopePerpToAB + bOfPerp;
			double xdiff = x - xOnAB;
			double ydiff = y - yOnAB;
			double distance = Math.sqrt(Math.pow(xdiff, 2) + Math.pow(ydiff, 2));
			if (distance <= range)
				return true;
			return false;
		}
		
		java.awt.Shape drawable = DrawingFactory.createRaw(s);
		return drawable.contains(shapeLoc);
	}
	


	public static Integer checkHandle(Shape s, Point2D.Double worldLoc) {
		Point2D.Double shapeLoc = new Point2D.Double();
		DrawingFactory.getWorldToObj(s).transform(worldLoc, shapeLoc);
		
		if (s instanceof Line) {
			Line line = (Line) s;
			double endX = line.getEnd().getX();
			double endY = line.getEnd().getY();
			if (DrawingFactory.newCenteredCircle(0, 0).contains(shapeLoc))
				return 1;
			else if (DrawingFactory.newCenteredCircle(endX, endY).contains(shapeLoc))
				return 2;
		} else {
			java.awt.Shape drawable = DrawingFactory.createHandleRaw(s);
			if (drawable.contains(shapeLoc))
				return 1;
		}
		return null;
	}
}
