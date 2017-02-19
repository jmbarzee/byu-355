package cs355.controller.clickHandlers.shapeClickHandlers;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import cs355.controller.TheController;
import cs355.controller.clickHandlers.ClickHandler;
import cs355.model.drawing.Shape;
import cs355.model.drawing.TheModel;

public abstract class ShapeClickHandler extends ClickHandler{
	
	protected Point2D.Double lastPoint;
	
	public ShapeClickHandler(TheController c) {
		super(c);
	}
	

	public void mouseDragged(MouseEvent e) {
		lastPoint = new Point2D.Double(e.getX(), e.getY());
		tempShapeToModel();
	}
	
	public void mouseMoved(MouseEvent e) {
		lastPoint = new Point2D.Double(e.getX(), e.getY());
		tempShapeToModel();
	}

	public void mouseClicked(MouseEvent e) {}

	public abstract void mousePressed(MouseEvent e);

	public abstract void mouseReleased(MouseEvent e);
	
	public abstract Shape buildShape();

	public void clean() {
		lastPoint = null;
		TheModel.inst().setTempShape(null);
	}
	
	public void saveShapeToModel() {
		Shape shape = buildShape();
		if (shape != null)
			TheModel.inst().addShape(shape);
		clean();
	}
	protected void tempShapeToModel() {
		Shape shape = buildShape();
		if (shape != null)
			TheModel.inst().setTempShape(shape);
	}
}
