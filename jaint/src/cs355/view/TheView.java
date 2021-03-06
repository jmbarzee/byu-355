package cs355.view;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import cs355.GUIFunctions;
import cs355.controller.TheController;
import cs355.model.drawing.Shape;
import cs355.model.drawing.TheModel;
import cs355.model.image.Pic;
import cs355.model.scene.Instance;
import cs355.model.scene.Line3D;
import cs355.model.scene.Point3D;
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
		drawPic(g2d);
		drawDrawing(g2d);
		drawScene(g2d);
	}

	private void refreshScrollBars() {
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
	
	private void drawPic(Graphics2D g2d) {
		TheController con = TheController.inst();
		if (!con.shouldDisplayPic())
			return;
		
		g2d.setTransform(con.getWorldToViewPort());
		
		Pic pic = con.getPic();
		
		int x = (TheController.worldSize - pic.getWidth()) / 2;
		int y = (TheController.worldSize - pic.getHeight()) / 2;
		
		g2d.drawImage(pic.getImage(), null, x, y);
		g2d.setTransform(new AffineTransform());
	}
	
	private void drawDrawing(Graphics2D g2d) {
		GUIFunctions.changeSelectedColor(TheController.inst().getColor());
		Shape selectedShape = TheController.inst().getSelectedShape();

		for (Shape shape : TheModel.inst().getShapes()) {
			DrawingFactory.draw(shape, g2d, (shape == selectedShape));
		}
		Shape tempShape = TheModel.inst().getTempShape();
		if (tempShape != null) {
			DrawingFactory.draw(tempShape, g2d, false);
		}
		g2d.setTransform(new AffineTransform());
	}

	private void drawScene(Graphics2D g2d) {
		TheController con = TheController.inst();
		if (!con.shouldDisplayScene())
			return;

		ArrayList<Instance> instances = con.getScene().instances();

		g2d.setTransform(con.getWorldToViewPort());
		for (Instance inst : instances) {
			g2d.setColor(inst.getColor());
			List<Line3D> list = inst.getModel().getLines();

			for (Line3D l : list) {
				double[] startCoord = con.getCamera().camera_clip(l.start, inst);
				double[] endCoord = con.getCamera().camera_clip(l.end, inst);
				if (!con.getCamera().clipTest(startCoord, endCoord)) {
					Point3D start = con.getCamera().clip_screen(new Point3D(startCoord[0] / startCoord[3],
							startCoord[1] / startCoord[3], startCoord[2] / startCoord[3]));
					Point3D end = con.getCamera().clip_screen(new Point3D(endCoord[0] / endCoord[3],
							endCoord[1] / endCoord[3], endCoord[2] / endCoord[3]));
					g2d.drawLine((int) Math.round(start.x), (int) Math.round(start.y), (int) Math.round(end.x),
							(int) Math.round(end.y));
				}
			}
		}
		g2d.setTransform(new AffineTransform());
	}

}
