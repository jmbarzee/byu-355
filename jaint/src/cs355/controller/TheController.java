package cs355.controller;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.Iterator;

import cs355.GUIFunctions;
import cs355.controller.clickHandlers.ClickHandler;
import cs355.controller.clickHandlers.SelectionClickHandler;
import cs355.controller.clickHandlers.shapeClickHandlers.CircleClickHandler;
import cs355.controller.clickHandlers.shapeClickHandlers.EllipseClickHandler;
import cs355.controller.clickHandlers.shapeClickHandlers.LineClickHandler;
import cs355.controller.clickHandlers.shapeClickHandlers.RectangleClickHandler;
import cs355.controller.clickHandlers.shapeClickHandlers.SquareClickHandler;
import cs355.controller.clickHandlers.shapeClickHandlers.TriangleClickHandler;
import cs355.model.collisionChecker.CollisionChecker;
import cs355.model.drawing.Shape;
import cs355.model.drawing.TheModel;

public class TheController implements CS355Controller, MouseListener, MouseMotionListener{

	private static TheController inst;
	
	public static TheController inst() {
		if (TheController.inst == null)
			TheController.inst = new TheController();
		return TheController.inst;
	}
	
	private TheController() {
		color = new Color(0);
		handler = new LineClickHandler(this);
		selectedShapePos = 0;
		xTrans = 0;
		yTrans = 0;
		zoom = 0;
		screenMoving = false;
	}
	
	private ClickHandler handler;
	private Color color;
	private Integer selectedShapePos;
	private double xTrans;
	private double yTrans;
	private double zoom;
	private boolean screenMoving;
	
	public Integer checkHandle(Point2D.Double loc) {
		if (selectedShapePos == null)
			return null;
		return CollisionChecker.checkHandle(TheModel.inst().getShape(selectedShapePos), loc);
	}
	
	private void swapHandler(ClickHandler h) {
		handler.clean();
		handler = h;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (handler == null)
			return;
		handler.mouseClicked(pointFromEvent(e));
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (handler == null)
			return;
		handler.mousePressed(pointFromEvent(e));
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (handler == null)
			return;
		handler.mouseReleased(pointFromEvent(e));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (handler == null)
			return;
		handler.mouseEntered(pointFromEvent(e));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (handler == null)
			return;
		handler.mouseExited(pointFromEvent(e));
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (handler == null)
			return;
		handler.mouseDragged(pointFromEvent(e));
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (handler == null)
			return;
		handler.mouseMoved(pointFromEvent(e));
	}

	@Override
	public void colorButtonHit(Color c) {
		GUIFunctions.changeSelectedColor(c);
		color = c;
		if (this.selectedShapePos != null) {
			TheModel.inst().getShape(this.selectedShapePos).setColor(c);
			GUIFunctions.refresh();
		}
	}

	@Override
	public void lineButtonHit() {
		swapHandler(new LineClickHandler(this));
	}

	@Override
	public void squareButtonHit() {
		swapHandler(new SquareClickHandler(this));
	}

	@Override
	public void rectangleButtonHit() {
		swapHandler(new RectangleClickHandler(this));
	}

	@Override
	public void circleButtonHit() {
		swapHandler(new CircleClickHandler(this));
	}

	@Override
	public void ellipseButtonHit() {
		swapHandler(new EllipseClickHandler(this));
	}

	@Override
	public void triangleButtonHit() {
		swapHandler(new TriangleClickHandler(this));
		
	}

	@Override
	public void selectButtonHit() {
		swapHandler(new SelectionClickHandler(this));
		
	}

	@Override
	public void zoomInButtonHit() {
		if (screenMoving)
			return;
		screenMoving = true;
		setZoom(getZoom()*2);
		screenMoving = false;
	}

	@Override
	public void zoomOutButtonHit() {
		if (screenMoving)
			return;
		screenMoving = true;
		setZoom(getZoom()/2);
		screenMoving = false;
	}

	@Override
	public void hScrollbarChanged(int value) {
		if (screenMoving)
			return;
		screenMoving = true;
		// TODO Auto-generated method stub
		screenMoving = false;
	}

	@Override
	public void vScrollbarChanged(int value) {
		if (screenMoving)
			return;
		screenMoving = true;
		// TODO Auto-generated method stub
		screenMoving = false;
	}

	@Override
	public void openScene(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toggle3DModelDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(Iterator<Integer> iterator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void openImage(File file) {
	}

	@Override
	public void saveImage(File file) {
	}

	@Override
	public void toggleBackgroundDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDrawing(File file) {
		TheModel.inst().save(file);
		
	}

	@Override
	public void openDrawing(File file) {
		TheModel.inst().open(file);
		
	}

	@Override
	public void doDeleteShape() {
		if (selectedShapePos == null)
			return;
		int pos = selectedShapePos;
		selectedShapePos = null;
		TheModel.inst().deleteShape(pos);
	}

	@Override
	public void doEdgeDetection() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSharpen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMedianBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doUniformBlur() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doGrayscale() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeContrast(int contrastAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doChangeBrightness(int brightnessAmountNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doMoveForward() {
		if (selectedShapePos == null)
			return;
		Shape s = TheModel.inst().getShape(selectedShapePos);
		TheModel.inst().moveForward(selectedShapePos);
		selectedShapePos = TheModel.inst().getShapePos(s);
	}

	@Override
	public void doMoveBackward() {
		if (selectedShapePos == null)
			return;
		Shape s = TheModel.inst().getShape(selectedShapePos);
		TheModel.inst().moveBackward(selectedShapePos);
		selectedShapePos = TheModel.inst().getShapePos(s);
	}

	@Override
	public void doSendToFront() {
		if (selectedShapePos == null)
			return;
		Shape s = TheModel.inst().getShape(selectedShapePos);
		TheModel.inst().moveToFront(selectedShapePos);
		selectedShapePos = TheModel.inst().getShapePos(s);
	}

	@Override
	public void doSendtoBack() {
		if (selectedShapePos == null)
			return;
		Shape s = TheModel.inst().getShape(selectedShapePos);
		TheModel.inst().movetoBack(selectedShapePos);
		selectedShapePos = TheModel.inst().getShapePos(s);
	}
	
	public Color getColor() {
		return color;
	}

	public Shape getSelectedShape() {
		if (selectedShapePos != null)
			return TheModel.inst().getShape(selectedShapePos);
		return null;
	}

	public void setSelectedShape(Integer selectedShape) {
		this.selectedShapePos = selectedShape;
		GUIFunctions.refresh();
		if (selectedShape != null) {
			this.color = TheModel.inst().getShape(selectedShape).getColor();
			GUIFunctions.changeSelectedColor(this.color);
		}
	}
	
	private Point2D.Double pointFromEvent(MouseEvent e) {
		Point2D.Double sLoc = new Point2D.Double(e.getX(), e.getY());
		AffineTransform screenToWorld = new AffineTransform();
		Point2D.Double wLoc = new Point2D.Double();
		screenToWorld.scale(this.zoom, this.zoom);
		screenToWorld.transform(sLoc, wLoc);
		return wLoc;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		screenMoving = true;
		if (zoom < .25)
			this.zoom = 0.25;
		else if (zoom > 4)
			this.zoom = 4;
		else
			this.zoom = zoom;
		System.out.println("Zoom: " + this.zoom);
		GUIFunctions.refresh();
		screenMoving = false;
	}

	public boolean isScreenMoving() {
		return screenMoving;
	}

}
