package CS355.LWJGL;

import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.util.glu.GLU.gluPerspective;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3d;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;

public class StudentLWJGLController implements CS355LWJGLController {
	private static final int ONE = 1;
	private static final Vector3f HOMEPOS = new Vector3f(0, -1.5f, -20);
	private static final double HOMEYAW = 0.0f;

	private int projection;

	private Vector3f position = new Vector3f();
	private double yaw = 0.0f;
	private WireFrame houseModel = new HouseModel();

	@Override
	public void resizeGL() {
		this.projection = 1;
		int width = LWJGLSandbox.DISPLAY_WIDTH;
		int height = LWJGLSandbox.DISPLAY_HEIGHT;
		glViewport(0, 0, width, height);
		
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		set(HOMEPOS, HOMEYAW);
	}

	@Override
	public void updateKeyboard() {
		if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
			this.projection = 0;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			this.projection = 1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			this.set(HOMEPOS, HOMEYAW);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.shiftLeft(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.shiftForward(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.shiftBackward(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.shiftRight(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			this.shiftUp(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			this.shiftDown(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			this.rotateLeft(ONE);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			this.rotateRight(ONE);
		}
	}

	@Override
	public void render() {
		glClear(GL_COLOR_BUFFER_BIT);

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();

		if (this.projection == 1) {
			gluPerspective(48.0f, (float) LWJGLSandbox.DISPLAY_WIDTH / LWJGLSandbox.DISPLAY_HEIGHT, 1.0f, 300.0f);
		} else {
			float left = -24f;
			float right = 24f;
			float bottom = -24f;
			float top = 24f;
			float zNear = 10f;
			float zFar = 300f;
			glOrtho(left, right, bottom, top, zNear, zFar);
		}

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		float x = 0f;
		float y = 1f;
		float z = 0f;
		glRotatef((float) yaw, x, y, z);
		glTranslatef(position.x, position.y, position.z);

		this.drawHouses();
	}
	
	private void drawHouse() {
		glBegin(GL_LINES);
		Iterator<Line3D> i = houseModel.getLines();
		while (i.hasNext()) {
			Line3D line = i.next();
			Point3D start = line.start;
			glVertex3d(start.x, start.y, start.z);
			Point3D end = line.end;
			glVertex3d(end.x, end.y, end.z);
		}
		glEnd();
	}
	
	private void drawHouses() {
		for (int z = 0; z < 10; z += 1) {
			for (int x = -1; x <= 1; x += 2) {
				float rotation = (x == 1) ? 270.0f : 90.0f;
				float r = 1.0f - ((z + 1.0f) * 0.1f);
				float g = 0.5f - (x * 0.25f);
				float b = ((z + 1.0f) * 0.1f);

				glPushMatrix();
				glColor3f(r, g, b);
				glTranslatef(x * 15, 0, z * -15);
				glRotatef(rotation, 0.0f, 1.0f, 0.0f);
				this.drawHouse();
				glPopMatrix();
			}
		}

	}

	private void set(Vector3f pos, double yaw) {
		position.x = pos.x;
		position.y = pos.y;
		position.z = pos.z;
		this.yaw = yaw;
	}

	private void shiftUp(int dis) {
		position.y -= dis;
	}

	private void shiftDown(int dis) {
		position.y += dis;
	}

	private void shiftForward(int dis) {
		position.x -= dis * (float) Math.sin(Math.toRadians(yaw));
		position.z += dis * (float) Math.cos(Math.toRadians(yaw));
	}

	private void shiftBackward(int dis) {
		position.x += dis * (float) Math.sin(Math.toRadians(yaw));
		position.z -= dis * (float) Math.cos(Math.toRadians(yaw));
	}

	private void shiftLeft(int dis) {
		position.x -= dis * (float) Math.sin(Math.toRadians(yaw - 90));
		position.z += dis * (float) Math.cos(Math.toRadians(yaw - 90));
	}

	private void shiftRight(int dis) {
		position.x -= dis * (float) Math.sin(Math.toRadians(yaw + 90));
		position.z += dis * (float) Math.cos(Math.toRadians(yaw + 90));
	}

	private void rotateLeft(int dis) {
		yaw -= dis;
	}

	private void rotateRight(int dis) {
		yaw += dis;
	}

	@Override
	public void update() {

	}

}