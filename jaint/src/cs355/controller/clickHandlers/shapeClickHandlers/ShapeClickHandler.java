package cs355.controller.clickHandlers.shapeClickHandlers;

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
	

	public void mouseDragged(Point2D.Double wLoc) {
		lastPoint = wLoc;
		tempShapeToModel();
	}
	
	public void mouseMoved(Point2D.Double wLoc) {
		lastPoint = wLoc;
		tempShapeToModel();
	}

	public void mouseClicked(Point2D.Double wLoc) {}
	
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
