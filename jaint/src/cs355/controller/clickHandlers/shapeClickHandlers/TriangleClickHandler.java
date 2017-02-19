package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.controller.TheController;
import cs355.model.drawing.Shape;
import cs355.model.drawing.Triangle;
import cs355.view.drawer.DrawingFactory;

public class TriangleClickHandler extends ShapeClickHandler {

	public TriangleClickHandler(TheController c) {
		super(c);
	}

	protected Point2D.Double firstPoint;
	protected Point2D.Double middlePoint;
	protected Point2D.Double finalPoint;
	
	public void mousePressed(Point2D.Double wLoc) {}
	public void mouseReleased(Point2D.Double wLoc) {}
	
	private Point2D.Double averagePoints(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
		Double xAvg = (a.getX() + b.getX() + c.getX()) / 3;
		Double yAvg = (a.getY() + b.getY() + c.getY()) / 3;
		return new Point2D.Double(xAvg, yAvg); 
	}
	
	
	@Override
	public Shape buildShape() {
		if (firstPoint == null)
			return null;
		
		Point2D.Double firstPoint = this.firstPoint;
		Point2D.Double middlePoint = (this.middlePoint == null) ? lastPoint : this.middlePoint;
		Point2D.Double finalPoint = (this.finalPoint == null) ? lastPoint : this.finalPoint;
		System.out.println("firstPoint " + firstPoint.toString());
		System.out.println("middlePoint " + middlePoint.toString());
		System.out.println("finalPoint " + finalPoint.toString());
		Point2D.Double center = averagePoints(firstPoint, middlePoint, finalPoint);
		System.out.println("center " + center.toString());
		System.out.println();

		Double firstRelativeX = firstPoint.getX() - center.getX();
		Double firstRelativeY = firstPoint.getY() - center.getY();
		Point2D.Double firstRel = new Point2D.Double (firstRelativeX, firstRelativeY);
		Double middleRelativeX = middlePoint.getX() - center.getX();
		Double middleRelativeY = middlePoint.getY() - center.getY();
		Point2D.Double middleRel = new Point2D.Double (middleRelativeX, middleRelativeY);
		Double finalRelativeX = finalPoint.getX() - center.getX();
		Double finalRelativeY = finalPoint.getY() - center.getY();
		Point2D.Double finalRel = new Point2D.Double (finalRelativeX, finalRelativeY);

		return new Triangle(theController.getColor(), center, firstRel, middleRel, finalRel);
	}
	@Override
	public void mouseClicked(Point2D.Double wLoc) {
		if (firstPoint == null) {
			firstPoint = wLoc;
			lastPoint = firstPoint;
			tempShapeToModel();
		} else if (middlePoint == null) {
			middlePoint = wLoc;
			lastPoint = middlePoint;
			tempShapeToModel();
		} else {
			finalPoint = wLoc;
			lastPoint = finalPoint;
			saveShapeToModel();
			clean();
		}
	}


	private Shape newTriangle(Color color, Point2D.Double center, Point2D.Double a, Point2D.Double b, Point2D.Double c) {
		Point2D.Double handle = getHandlePosition(a,b);
		AffineTransform af = new AffineTransform();
		double angle = getAngle(handle);
		af.rotate(-angle);
		Point2D.Double ar = new Point2D.Double();
		af.transform(a, ar);
		Point2D.Double br = new Point2D.Double();
		af.transform(b, br);
		Point2D.Double cr = new Point2D.Double();
		af.transform(c, cr);
		Triangle tri = new Triangle(color, center, ar, br, cr);
		tri.setRotation(angle);
		return tri;
	}
	private double getAngle(Point2D.Double handle) {
		Point2D.Double pin = new Point2D.Double(1,0);
		double aX = handle.getX();
		double aY = handle.getY();
		double bX = 0;
		double bY = 0;
		double cX = pin.getX();
		double cY = pin.getY();
		double AB  = Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));
		double BC  = Math.sqrt(Math.pow(bX - cX, 2) + Math.pow(bY - cY, 2));
		double CA  = Math.sqrt(Math.pow(cX - aX, 2) + Math.pow(cY - aY, 2));
		double top = (AB * AB - CA * CA + BC *BC);
		double bot = (2 * AB * BC);
		double angle = Math.acos(top/bot);
		double bias = Math.signum(pin.getX() - handle.getX());
		return bias * angle;
	}
	
	private Point2D.Double getHandlePosition(Point2D.Double a, Point2D.Double b) {
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
		double xOffset = DrawingFactory.handleDis / Math.sqrt(1 + Math.pow(slopeInv, 2));
		double yOffset = xOffset * slopeInv;
		
		double x = xOnAB - xOffset; // * Math.signum(xOnAB);
		double y = yOnAB - yOffset; // * Math.signum(yOnAB);
		
		return new Point2D.Double(x, y);
	}
	
	@Override
	public void clean() {
		super.clean();
		firstPoint = null;
		middlePoint = null;
		finalPoint = null;
	}
}
