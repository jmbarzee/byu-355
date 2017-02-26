package cs355.solution;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.TheController;
import cs355.model.drawing.Shape;
import cs355.model.drawing.TheModel;
import cs355.view.ViewRefresher;
import cs355.view.drawer.DrawingFactory;

public class TheView implements ViewRefresher {
	
	public TheView() {
		TheModel.inst().addObserver(this);
	}

	@Override
	public void update(Observable o, Object arg) {
		GUIFunctions.refresh();
	}

	@Override
	public void refreshView(Graphics2D g2d) {
		refreshScrollBars();
		GUIFunctions.changeSelectedColor(TheController.inst().getColor());
		Shape selectedShape = TheController.inst().getSelectedShape();

//		g2d.transform(TheController.inst().getWorldToScreen());
		
		for (Shape shape : TheModel.inst().getShapes()) {
			DrawingFactory.draw(shape, g2d, (shape == selectedShape));
		}
		Shape tempShape = TheModel.inst().getTempShape();
		if (tempShape != null) {
			DrawingFactory.draw(tempShape, g2d, false);
		}
		
	}
	
	public void refreshScrollBars() {
		int worldSize = TheController.worldSize;
		int scrollBarSize = TheController.inst().getScrollBarSize();
		int transX = (int) TheController.inst().getTransX();
		int transY = (int) TheController.inst().getTransY();

		GUIFunctions.setHScrollBarMin(0);
		GUIFunctions.setHScrollBarMax(worldSize + 1);
		GUIFunctions.setHScrollBarPosit(transX);
		GUIFunctions.setHScrollBarKnob(scrollBarSize);
		GUIFunctions.setHScrollBarPosit(transX);

		GUIFunctions.setVScrollBarMin(0);
		GUIFunctions.setVScrollBarMax(worldSize + 1);
		GUIFunctions.setVScrollBarPosit(transY);
		GUIFunctions.setVScrollBarKnob(scrollBarSize);
		GUIFunctions.setVScrollBarPosit(transY);
	}

}
