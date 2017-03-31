package cs355.controller;

import java.awt.geom.AffineTransform;

import cs355.model.scene.Instance;
import cs355.model.scene.Point3D;

public class Camera {
	
	private TheController controller; 
	
	private Point3D position = new Point3D();
	private double yaw = 0.0;

	private static final Point3D HOMEPOS = new Point3D(77, 5, -15);
	private static final double HOMEYAW = 90.0;
	
	private static final double nearPlane = 1.0;
	private static final double farPlane = 1000.0;
	
	private static final int ONE = 1;
	
	public Camera(TheController c) {
		controller = c;
		goHome();
	}

	private void set(Point3D pos, double yaw) {
		position.x = pos.x;
		position.y = pos.y;
		position.z = pos.z;
		this.yaw = yaw;
	}
	
	public void goHome() {
		this.set(HOMEPOS, HOMEYAW);
	}
	

	public void shiftUp() {
		position.y -= ONE;
	}

	public void shiftDown() {
		position.y += ONE;
	}

	public void shiftForward() {
		position.x -= ONE * (float) Math.sin(Math.toRadians(yaw));
		position.z += ONE * (float) Math.cos(Math.toRadians(yaw));
	}

	public void shiftBackward() {
		position.x += ONE * (float) Math.sin(Math.toRadians(yaw));
		position.z -= ONE * (float) Math.cos(Math.toRadians(yaw));
	}

	public void shiftLeft() {
		position.x -= ONE * (float) Math.sin(Math.toRadians(yaw - 90));
		position.z += ONE * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	public void shiftRight() {
		position.x -= ONE * (float) Math.sin(Math.toRadians(yaw + 90));
		position.z += ONE * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	public void rotateLeft() {
		yaw -= ONE;
	}

	public void rotateRight() {
		yaw += ONE;
	}
	
	public double[] camera_clip(Point3D point, Instance instance)
	{
		float theta = (float) Math.toRadians(yaw);
		double camX = position.x + instance.getPosition().x;
		double camY = position.y + instance.getPosition().y;
		double camZ = position.z + instance.getPosition().z;
		double e = (farPlane + nearPlane) / (farPlane - nearPlane);
		double f = (-2 * nearPlane * farPlane) / (farPlane - nearPlane);

		double x1 = point.x - camX;
		double y1 = point.y - camY;
		double z1 = point.z - camZ;
		
		double x2 = x1 * Math.cos(theta) + z1 * Math.sin(theta);
		double z2 = -x1 * Math.sin(theta) + z1 * Math.cos(theta);

		double x = x2 * Math.sqrt(3) + Math.sqrt(3);
		double y = y1 * Math.sqrt(3);
		double z = f + e * z2;		
		double bigW = (-camZ * Math.cos(theta) + point.z * Math.cos(theta) + camX * Math.sin(theta) - point.x * Math.sin(theta));

		double[] result = {x, y, z, bigW};

		return result;
	}

	public Point3D clip_screen(Point3D point)
	{
		
		double x = 1024 + (1024 * point.x);
		double y = 1024 - (1024 * point.y);
		return new Point3D(x, y, 1);
	}

	public boolean clipTest(double[] one, double[] two)
	{
		double sX = one[0];
		double sY = one[1];
		double sZ = one[2];
		double sW = one[3];

		double eX = two[0];
		double eY = two[1];
		double eZ = two[2];
		double eW = two[3];

		if (sX > sW && eX > eW)
			return true;
		if (sX < -sW && eX < -eW)
			return true;
		if (sY > sW && eY > eW)
			return true;
		if (sY < -sW && eY < -eW)
			return true;
		if (sZ > sW && eZ > eW)
			return true;
		if (sZ <= -sW || eZ <= -eW) 
			return true;
		return false;
	}

	public void printLoc() {
		System.out.println("(" + position.x + ", " + position.y + ", " + position.z + ", " + yaw + ")" );
	}
}
