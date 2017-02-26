package cs355.view.drawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import cs355.controller.TheController;
import cs355.model.drawing.Circle;
import cs355.model.drawing.Ellipse;
import cs355.model.drawing.Line;
import cs355.model.drawing.Rectangle;
import cs355.model.drawing.Square;
import cs355.model.drawing.Triangle;

public class DrawingFactory {
	private DrawingFactory() {
	}
	

	final private static double handleDis = 75.0;
	final private static double handleDia = 100.0;
	final private static double handleRad = handleDia/2;
	
	public static java.awt.Shape create(cs355.model.drawing.Shape s) {
		return getObjToWorld(s).createTransformedShape(createRaw(s));
	}
	
	public static AffineTransform getObjToWorld(cs355.model.drawing.Shape s) {
		AffineTransform objToWorld = new AffineTransform();
		objToWorld.concatenate(new AffineTransform(1, 0, 0, 1, s.getCenter().getX(), s.getCenter().getY()));
//		objToWorld.translate(s.getCenter().getX(), s.getCenter().getY());
		double rot = s.getRotation();
		objToWorld.concatenate(new AffineTransform(Math.cos(rot), Math.sin(rot), -Math.sin(rot), Math.cos(rot), 0, 0));
//		objToWorld.rotate(s.getRotation());
		return objToWorld;
	}
	
	public static AffineTransform getWorldToObj(cs355.model.drawing.Shape s) {
		AffineTransform worldToObj = new AffineTransform();
		double rot = -s.getRotation();
		worldToObj.concatenate(new AffineTransform(Math.cos(rot), Math.sin(rot), -Math.sin(rot), Math.cos(rot), 0, 0));
		//worldToObj.rotate(-s.getRotation());
		worldToObj.concatenate(new AffineTransform(1, 0, 0, 1, -s.getCenter().getX(), -s.getCenter().getY()));
		//worldToObj.translate(-s.getCenter().getX(), -s.getCenter().getY());
		return worldToObj;
	}
	
	public static java.awt.Shape createRaw(cs355.model.drawing.Shape s) {
		java.awt.Shape drawableShape = null;
		if (s instanceof Line) {
			Line line = (Line) s;
			double endX = line.getEnd().getX();
			double endY = line.getEnd().getY();
			drawableShape = new Line2D.Double(0, 0, endX, endY);
		} else if (s instanceof Square) {
			Square square = (Square) s;
			double size = square.getSize();
			drawableShape = new Rectangle2D.Double(-size/2, -size/2, size, size);
		} else if (s instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) s;
			double width = rectangle.getWidth();
			double height = rectangle.getHeight();
			drawableShape = new Rectangle2D.Double(-width/2, -height/2, width, height);
		} else if (s instanceof Circle) {
			Circle circle = (Circle) s;
			double radius = circle.getRadius();
			double diameter = radius * 2;
			drawableShape = new Ellipse2D.Double(-radius, -radius, diameter, diameter);
		} else if (s instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) s;
			double width = ellipse.getWidth();
			double height = ellipse.getHeight();
			drawableShape = new Ellipse2D.Double(-width/2, -height/2, width, height);
		} else if (s instanceof Triangle) {
			Triangle triangle = (Triangle) s;
			Path2D path = new Path2D.Double();
			Double firstX = triangle.getA().getX();
			Double firstY = triangle.getA().getY();
			Double middleX = triangle.getB().getX();
			Double middleY = triangle.getB().getY();
			Double finalX = triangle.getC().getX();
			Double finalY = triangle.getC().getY();
			path.moveTo(firstX, firstY);
			path.lineTo(middleX, middleY);
			path.lineTo(finalX, finalY);
			path.lineTo(firstX, firstY);
			path.closePath();
			drawableShape = path;
		}
		return drawableShape;
	}
	
	public static java.awt.Shape draw(cs355.model.drawing.Shape s, Graphics2D g2d, boolean selected) {
		java.awt.Shape drawableShape = createRaw(s);
		Color c = s.getColor();
		g2d.setColor(c);
		AffineTransform af = TheController.inst().getWorldToScreen();
		af.concatenate(getObjToWorld(s));
		if (s instanceof Line) {
			Line line = (Line) s;
			double endX = line.getEnd().getX();
			double endY = line.getEnd().getY();
			g2d.draw(af.createTransformedShape(drawableShape));
			if (selected) {
				g2d.setColor(Color.WHITE);
				g2d.setStroke(new BasicStroke((float) (1/TheController.inst().getZoom())));
				g2d.draw(af.createTransformedShape(newHandle(endX, endY)));
				g2d.draw(af.createTransformedShape(newHandle(0, 0)));
			}
		} else {
			g2d.fill(af.createTransformedShape(drawableShape));
			if (selected) {
				g2d.setColor(Color.WHITE);
				g2d.setStroke(new BasicStroke((float) (1/TheController.inst().getZoom())));
				g2d.draw(af.createTransformedShape(drawableShape));
				g2d.draw(af.createTransformedShape(createHandleRaw(s)));
			}
		}
		return drawableShape;
	}

	public static Shape createHandleRaw(cs355.model.drawing.Shape s) {
		double x = 0;
		double y = 0;
		if (s instanceof Square) {
			Square square = (Square) s;
			double size = square.getSize();
			x = 0;
			y = -size/2-getHandleDis();
		} else if (s instanceof Rectangle) {
			Rectangle rectangle = (Rectangle) s;
			double height = rectangle.getHeight();
			x = 0;
			y = -height/2-getHandleDis();
		} else if (s instanceof Circle) {
			Circle circle = (Circle) s;
			double radius = circle.getRadius();
			x = 0;
			y = -radius-getHandleDis();
		} else if (s instanceof Ellipse) {
			Ellipse ellipse = (Ellipse) s;
			double height = ellipse.getHeight();
			x = 0;
			y = -height/2-getHandleDis();
		} else if (s instanceof Triangle) {
			Triangle triangle = (Triangle) s;
			Point2D.Double a = triangle.getA();
			Point2D.Double b = triangle.getB();
			double aX = a.getX();
			double aY = a.getY();
			double bX = b.getX();
			double bY = b.getY();
			double slopeOfAB = (aY - bY) / (aX - bX);
			// find the y-intercept of AB
			double bOfAB = aY - slopeOfAB*aX;
			// find the slope of line perpendicular to AB
			double slopeInv = -1 / slopeOfAB; 
			// find the intersection of AB and the perpendicular line passing through (0,0)
			double xOnAB = bOfAB / (slopeInv - slopeOfAB);
			double yOnAB = xOnAB * slopeInv;
			// find the additional offset needed to space the handle from AB
			double xOffset = getHandleDis() / Math.sqrt(1 + Math.pow(slopeInv, 2));
			double yOffset = xOffset * slopeInv;
			
			x = xOnAB - xOffset; // * Math.signum(xOnAB);
			y = yOnAB - yOffset; // * Math.signum(yOnAB);
		}
		return newHandle(x, y);
	}
	
	public static Ellipse2D.Double newHandle(double x, double y) {
		return new Ellipse2D.Double( x-getHandleRad(), y-getHandleRad(), getHandleDia(), getHandleDia());
	}
	
	public static double getHandleRad() {
		return handleRad / TheController.inst().getZoom();
	}
	public static double getHandleDia() {
		return handleDia / TheController.inst().getZoom();
	}
	public static double getHandleDis() {
		return handleDis / TheController.inst().getZoom();
	}
}
