package cs355.controller.clickHandlers;

import java.awt.geom.Point2D;

import cs355.controller.TheController;

public abstract class ClickHandler{
	
	protected TheController theController;
	
	public ClickHandler(TheController c) {
		theController = c;
	}
	

	public abstract void mouseDragged(Point2D.Double wLoc);
	
	public abstract void mouseMoved(Point2D.Double wLoc);

	public abstract void mouseClicked(Point2D.Double wLoc);
	
	public void mouseEntered(Point2D.Double wLoc){}
	
	public void mouseExited(Point2D.Double wLoc){}

	public abstract void mousePressed(Point2D.Double wLoc);

	public abstract void mouseReleased(Point2D.Double wLoc);

	public abstract void clean();
}
