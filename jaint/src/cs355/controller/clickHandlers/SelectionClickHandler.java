package cs355.controller.clickHandlers;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import cs355.GUIFunctions;
import cs355.controller.TheController;
import cs355.model.collisionChecker.CollisionChecker;
import cs355.model.drawing.Line;
import cs355.model.drawing.Shape;
import cs355.model.drawing.TheModel;
import cs355.view.drawer.DrawingFactory;

public class SelectionClickHandler extends ClickHandler {
	
	private Point2D.Double lastClick;
	private Integer handle;

	public SelectionClickHandler(TheController c) {
		super(c);
	}

	@Override
	public void mousePressed(Point2D.Double wLoc) {
		lastClick = wLoc;
		Shape s = theController.getSelectedShape();
		
		if (s != null) {
			handle = CollisionChecker.checkHandle(s, lastClick);
		}

		if (handle == null) {
			Integer selectedShape = TheModel.inst().getCollision(lastClick);
			theController.setSelectedShape(selectedShape);
		}
	}

	@Override
	public void mouseReleased(Point2D.Double wLoc) {
		handle = null;
		lastClick = null;
	}

	@Override
	public void mouseDragged(Point2D.Double wLoc) {
		Shape s = theController.getSelectedShape();
		Point2D.Double click = wLoc;
		if (s == null)
			return;
		if (handle == null) {
			slide(s, click);
		} else {
			if (s instanceof Line) {
				shift((Line)s, click);
			} else {
				rotate(s, click);
			}
		}
	}

	@Override
	public void mouseMoved(Point2D.Double wLoc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(Point2D.Double wLoc) {
	}

	@Override
	public void clean() {
		theController.setSelectedShape(null);
		handle = null;
		lastClick = null;
	}
	
	private void slide(Shape s, Point2D.Double click) {
		Point2D.Double center = s.getCenter();
		double slideX = click.getX() - lastClick.getX();
		double slideY = click.getY() - lastClick.getY();
		Point2D.Double newCenter = new Point2D.Double(slideX + center.getX() , slideY + center.getY());
		s.setCenter(newCenter);
		lastClick = click;
		GUIFunctions.refresh();
	}
	
	private void rotate(Shape s, Point2D.Double click) {
		Point2D.Double center = s.getCenter();
		double aX = click.getX();
		double aY = click.getY();
		double bX = center.getX();
		double bY = center.getY();
		double cX = lastClick.getX();
		double cY = lastClick.getY();
		double AB  = Math.sqrt(Math.pow(aX - bX, 2) + Math.pow(aY - bY, 2));
		double BC  = Math.sqrt(Math.pow(bX - cX, 2) + Math.pow(bY - cY, 2));
		double CA  = Math.sqrt(Math.pow(cX - aX, 2) + Math.pow(cY - aY, 2));
		double top = (AB * AB - CA * CA + BC *BC);
		double bot = (2 * AB * BC);
		double angle = Math.acos(top/bot);
		AffineTransform af = DrawingFactory.getWorldToObj(s);
		Point2D.Double lastClickInObj = new Point2D.Double();
		af.transform(lastClick, lastClickInObj);
		Point2D.Double clickInObj = new Point2D.Double();
		af.transform(click, clickInObj);
		double bias = Math.signum(lastClickInObj.getX() - clickInObj.getX());
		s.setRotation((s.getRotation() - angle * bias));
		lastClick = click;
		GUIFunctions.refresh();
	}
	
	private void shift(Line l, Point2D.Double click) {
		double slideX = click.getX() - lastClick.getX();
		double slideY = click.getY() - lastClick.getY();
		lastClick = click;
		if (handle == 1) {
			Point2D.Double start = l.getCenter();
			Point2D.Double newStart = new Point2D.Double(slideX + start.getX() , slideY + start.getY());
			Point2D.Double end = l.getEnd();
			Point2D.Double newEnd = new Point2D.Double(end.getX() - slideX , end.getY() - slideY);
			l.setEnd(newEnd);
			l.setCenter(newStart);
		} else if (handle == 2) {
			Point2D.Double end = l.getEnd();
			Point2D.Double newEnd = new Point2D.Double(slideX + end.getX() , slideY + end.getY());
			l.setEnd(newEnd);
		}
		GUIFunctions.refresh();
	}
}
