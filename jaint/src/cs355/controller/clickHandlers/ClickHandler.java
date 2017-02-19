package cs355.controller.clickHandlers;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import cs355.controller.TheController;
import cs355.model.drawing.Shape;
import cs355.model.drawing.TheModel;

public abstract class ClickHandler{
	
	protected TheController theController;
	
	public ClickHandler(TheController c) {
		theController = c;
	}
	

	public abstract void mouseDragged(MouseEvent e);
	
	public abstract void mouseMoved(MouseEvent e);

	public abstract void mouseClicked(MouseEvent e);
	
	public void mouseEntered(MouseEvent e){}
	
	public void mouseExited(MouseEvent e){}

	public abstract void mousePressed(MouseEvent e);

	public abstract void mouseReleased(MouseEvent e);

	public abstract void clean();
}
