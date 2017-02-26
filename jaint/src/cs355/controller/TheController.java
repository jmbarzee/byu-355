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
import cs355.model.drawing.Square;
import cs355.model.drawing.TheModel;

public class TheController implements CS355Controller, MouseListener, MouseMotionListener{

	private static TheController inst;
	public static int worldSize = 2048;
	public static int swRatio = 4;
	
	public static TheController inst() {
		if (TheController.inst == null)
			TheController.inst = new TheController();
		return TheController.inst;
	}
	
	private TheController() {
		color = new Color(0);
		handler = new LineClickHandler(this);
		selShapePos = null;
		transX = (worldSize - (worldSize / swRatio)) / 2;
		transY = (worldSize - (worldSize / swRatio)) / 2;
		zoom = swRatio;
		vPortMoving = false;
	}
	
	private ClickHandler handler;
	private Color color;
	private Integer selShapePos;
	private double transX;
	private double transY;
	private double zoom;
	private boolean vPortMoving;
	
	public Integer checkHandle(Point2D.Double loc) {
		if (selShapePos == null)
			return null;
		return CollisionChecker.checkHandle(TheModel.inst().getShape(selShapePos), loc);
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
		if (this.selShapePos != null) {
			TheModel.inst().getShape(this.selShapePos).setColor(c);
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
		if (vPortMoving)
			return;
		vPortMoving = true;
		setZoom(getZoom()*2);
		vPortMoving = false;
	}

	@Override
	public void zoomOutButtonHit() {
		if (vPortMoving)
			return;
		vPortMoving = true;
		setZoom(getZoom()/2);
		vPortMoving = false;
	}

	@Override
	public void hScrollbarChanged(int value) {
		if (vPortMoving)
			return;
		vPortMoving = true;
		System.out.println("Hval: " + value);
		this.transX = value;
		GUIFunctions.refresh();
		vPortMoving = false;
	}

	@Override
	public void vScrollbarChanged(int value) {
		if (vPortMoving)
			return;
		vPortMoving = true;
		System.out.println("Vval: " + value);
		this.transY = value;
		GUIFunctions.refresh();
		vPortMoving = false;
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
		if (selShapePos == null)
			return;
		int pos = selShapePos;
		selShapePos = null;
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
		if (selShapePos == null)
			return;
		int size = TheModel.inst().getShapes().size();
		int oldShapePos =  (selShapePos + 1 < size) ? selShapePos++ : selShapePos;
		Shape s = TheModel.inst().getShape(oldShapePos);
		TheModel.inst().moveForward(oldShapePos);
		selShapePos = TheModel.inst().getShapePos(s);
	}

	@Override
	public void doMoveBackward() {
		if (selShapePos == null)
			return;
		int oldShapePos =  (selShapePos - 1 > -1) ? selShapePos-- : selShapePos;
		Shape s = TheModel.inst().getShape(oldShapePos);
		TheModel.inst().moveBackward(oldShapePos);
		selShapePos = TheModel.inst().getShapePos(s);
	}

	@Override
	public void doSendToFront() {
		if (selShapePos == null)
			return;
		int size = TheModel.inst().getShapes().size();
		int oldShapePos =  selShapePos = size - 1;
		Shape s = TheModel.inst().getShape(oldShapePos);
		TheModel.inst().moveToFront(oldShapePos);
		selShapePos = TheModel.inst().getShapePos(s);
	}

	@Override
	public void doSendtoBack() {
		if (selShapePos == null)
			return;
		Shape s = TheModel.inst().getShape(selShapePos);
		TheModel.inst().movetoBack(selShapePos);
		selShapePos = TheModel.inst().getShapePos(s);
	}
	
	public Color getColor() {
		return color;
	}

	public Shape getSelectedShape() {
		if (selShapePos != null)
			return TheModel.inst().getShape(selShapePos);
		return null;
	}

	public void setSelectedShape(Integer selectedShape) {
		this.selShapePos = selectedShape;
		GUIFunctions.refresh();
		if (selectedShape != null) {
			this.color = TheModel.inst().getShape(selectedShape).getColor();
			GUIFunctions.changeSelectedColor(this.color);
		}
	}
	
	private Point2D.Double pointFromEvent(MouseEvent e) {
		Point2D.Double sLoc = new Point2D.Double(e.getX(), e.getY());
		Point2D.Double wLoc = new Point2D.Double();
		getScreenToWorld().transform(sLoc, wLoc);
		return wLoc;
	}

	public double getZoom() {
		return zoom;
	}

	public void setZoom(double zoom) {
		vPortMoving = true;

		double oldVisibleSize = worldSize / this.zoom;
		double oldTransDueToZoom = (worldSize - oldVisibleSize) / 2;
		double transXDiff = this.transX - oldTransDueToZoom;
		double transYDiff = this.transY - oldTransDueToZoom;
		if (zoom < 1)
			this.zoom = 1;
		else if (zoom > 16)
			this.zoom = 16;
		else
			this.zoom = zoom;
		double newVisibleSize = worldSize / this.zoom;
		double newTransDueToZoom = (worldSize - newVisibleSize) / 2;
		double transX = transXDiff + newTransDueToZoom;
		double transY = transYDiff + newTransDueToZoom;
		if (transX < 0)
			this.transX = 0;
		else if (transX > worldSize - newVisibleSize)
			this.transX = worldSize - newVisibleSize;
		else
			this.transX = transX;
		
		if (transY < 0)
			this.transY = 0;
		else if (transY > worldSize - newVisibleSize)
			this.transY = worldSize - newVisibleSize;
		else
			this.transY = transY;
		
//		System.out.println("Scale: " + this.zoom/swRatio);
//		System.out.println("transX: " + this.transX);
//		System.out.println("transY: " + this.transY);
		GUIFunctions.refresh();
		
		vPortMoving = false;
	}

	public boolean isScreenMoving() {
		return vPortMoving;
	}
	
	public AffineTransform getWorldToScreen() {
		AffineTransform worldToScreen = new AffineTransform();
		worldToScreen.concatenate(new AffineTransform(zoom/swRatio, 0, 0, zoom/swRatio, 0, 0));
		worldToScreen.concatenate(new AffineTransform(1, 0, 0, 1, -transX, -transY));
//		worldToScreen.scale(zoom/swRatio, zoom/swRatio);
//		worldToScreen.translate(-transX, -transY);
//		System.out.println("World to Screen");
//		System.out.println("transX: " + -this.transX);
//		System.out.println("transY: " + -this.transY);
//		System.out.println("scale: " + zoom/swRatio);
		return worldToScreen;
	}
	
	public AffineTransform getScreenToWorld() {
		AffineTransform screenToWorld = new AffineTransform();
		screenToWorld.concatenate(new AffineTransform(1, 0, 0, 1, transX, transY));
		screenToWorld.concatenate(new AffineTransform(swRatio/zoom, 0, 0, swRatio/zoom, 0, 0));
//		screenToWorld.translate(transX, transY);
//		screenToWorld.scale(swRatio/zoom, swRatio/zoom);
//		System.out.println("Screen to World");
//		System.out.println("scale: " + swRatio/zoom);
//		System.out.println("transX: " + this.transX);
//		System.out.println("transY: " + this.transY);
		return screenToWorld;
	}

	public double getTransX() {
		return transX;
	}
	
	public double getTransY() {
		return transY;
	}

	public int getScrollBarSize() {
		return (int) (worldSize / zoom);
	}
}
