package cs355.model.drawing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cs355.model.collisionChecker.CollisionChecker;

public class TheModel extends CS355Drawing {
	
	private static TheModel inst;

	public static TheModel inst() {
		if (TheModel.inst == null)
			TheModel.inst = new TheModel();
		return TheModel.inst;
	}

	public Integer getCollision(Point2D.Double loc) {
		for (int i = shapes.size()-1; i>=0; i--) {
			if (CollisionChecker.check(shapes.get(i), loc))
				return i;
		}
		return null;
	}
	
	private List<Shape> shapes;
	private Shape tempShape;
	
	public TheModel() {
		shapes = new ArrayList<Shape>();
	}

	@Override
	public Shape getShape(int index) {
		return shapes.get(index);
	}
	
	public Integer getShapePos(Shape s) {
		for (int i = shapes.size()-1; i>=0; i--) {
			if (shapes.get(i) == s)
				return i;
		}
		return null;
	}

	@Override
	public int addShape(Shape s) {
		tempShape = null;
		int pos = shapes.size();
		shapes.add(s);
		setChanged();
		notifyObservers();
		return pos;
	}
	
	public void setTempShape(Shape s) {
		tempShape = s;
		setChanged();
		notifyObservers();
	}

	@Override
	public void deleteShape(int index) {
		shapes.remove(index);
		setChanged();
		notifyObservers();
	}

	@Override
	public void moveToFront(int index) {
		Shape shape = shapes.remove(index);
		shapes.add(shape);
		setChanged();
		notifyObservers();
	}

	@Override
	public void movetoBack(int index) {
		Shape shape = shapes.remove(index);
		shapes.add(0, shape);
		setChanged();
		notifyObservers();
	}

	@Override
	public void moveForward(int index) {
		if (index+1 > shapes.size()-1)
			return;
		Shape shape = shapes.get(index);
		shapes.set(index, shapes.get(index + 1));
		shapes.set(index+1, shape);
		setChanged();
		notifyObservers();
	}

	@Override
	public void moveBackward(int index) {
		if (index < 1)
			return;
		Shape shape = shapes.get(index);
		shapes.set(index, shapes.get(index - 1));
		shapes.set(index-1, shape);
		setChanged();
		notifyObservers();
	}

	@Override
	public List<Shape> getShapes() {
		return shapes;
	}

	@Override
	public List<Shape> getShapesReversed() {
		@SuppressWarnings("unchecked")
		List<Shape> shapesRev = (List<Shape>) ((ArrayList) shapes).clone();
		Collections.reverse(shapesRev);
		return shapesRev;
	}

	@Override
	public void setShapes(List<Shape> shapes) {
		this.shapes = shapes;
		setChanged();
		notifyObservers();
	}

	public Shape getTempShape() {
		return tempShape;
	}

}
