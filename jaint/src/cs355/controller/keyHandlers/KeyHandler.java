package cs355.controller.keyHandlers;

import cs355.controller.Camera;

public class KeyHandler {
	
	private Camera camera;
	
	public KeyHandler(Camera camera) {
		this.camera = camera;
	}


	
	public void handleKey(Integer key) {
		camera.printLoc();
		if (key == 72) {
			camera.goHome();
		} else if (key == 65) {
			camera.shiftRight();
		} else if (key == 87) {
			camera.shiftForward();
		} else if (key == 83) {
			camera.shiftBackward();
		} else if (key == 68) {
			camera.shiftLeft();
		} else if (key == 82) {
			camera.shiftDown();
		} else if (key == 70) {
			camera.shiftUp();
		} else if (key == 81) {
			camera.rotateRight();
		} else if (key == 69) {
			camera.rotateLeft();
		}
	}
	
}
