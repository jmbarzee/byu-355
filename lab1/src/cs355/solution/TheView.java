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
		GUIFunctions.changeSelectedColor(TheController.inst().getColor());
		Shape selectedShape = TheController.inst().getSelectedShape();
		for (Shape shape : TheModel.inst().getShapes()) {
			DrawingFactory.draw(shape, g2d, (shape == selectedShape));
		}
		Shape tempShape = TheModel.inst().getTempShape();
		if (tempShape != null) {
			g2d.setColor(tempShape.getColor());
			g2d.fill(DrawingFactory.create(tempShape));
			g2d.draw(DrawingFactory.create(tempShape));
		}
	}

}
