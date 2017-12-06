package cs355.model.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

public class Pic extends CS355Image {

	private BufferedImage bufImg = null;
	private int[][] changedPixs = null; // TODO make into 3d array

	public Pic() {
		super();
	}

	@Override
	public BufferedImage getImage() {
		if (bufImg == null)
			initBufImage();
		return bufImg;
	}

	private void initBufImage() {
		int w = getWidth();
		int h = getHeight();
		bufImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		WritableRaster wr = bufImg.getRaster();

		int[] rgb = new int[3];

		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				wr.setPixel(x, y, getPixel(x, y, rgb));
			}
		}

		bufImg.setData(wr);
	}

	private void initChangedPixs() {
		if (changedPixs == null)
			changedPixs = new int[getWidth() * getHeight()][3];
	}

	private void updatePixels() {
		int height = getHeight();
		int width = getWidth();

		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				setPixel(x, y, changedPixs[width * y + x]);
			}
		}
	}

	@Override
	public void doEdgeDetection() {
		initChangedPixs();

		int[] filterX = { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
		int[] filterY = { -1, -2, -1, 0, 0, 0, 1, 2, 1 };

		int[] rgb = new int[3];
		float[] hsb = new float[3];

		// TODO make arrays 3x3 instead of 9
		int[] xPos = new int[9];
		int[] yPos = new int[9];

		int h = getHeight();
		int w = getWidth();
		int lastx, lasty, nextx, nexty;

		double xTotal = 0;
		double yTotal = 0;

		for (int y = 0; y < h; ++y) {
			lasty = max(y - 1, 0);
			nexty = min(y + 1, h - 1);

			yPos[0] = lasty;
			yPos[1] = lasty;
			yPos[2] = lasty;
			yPos[3] = y;
			yPos[4] = y;
			yPos[5] = y;
			yPos[6] = nexty;
			yPos[7] = nexty;
			yPos[8] = nexty;
			for (int x = 0; x < w; ++x) {
				lastx = max(x - 1, 0);
				nextx = min(x + 1, w - 1);

				xPos[0] = lastx;
				xPos[1] = x;
				xPos[2] = nextx;
				xPos[3] = lastx;
				xPos[4] = x;
				xPos[5] = nextx;
				xPos[6] = lastx;
				xPos[7] = x;
				xPos[8] = nextx;

				xTotal = 0;
				yTotal = 0;
				for (int i = 0; i < 9; i++) {
					rgb = getPixel(xPos[i], yPos[i], rgb);
					hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

					xTotal += (filterX[i] * hsb[2]);
					yTotal += (filterY[i] * hsb[2]);
				}
				xTotal /= 8;
				yTotal /= 8;

				double magnitude = Math.sqrt(Math.pow(xTotal, 2) + Math.pow(yTotal, 2));
				int colorValue = Math.min((int) (magnitude * 255) + 128, 255);

				changedPixs[w * y + x][0] = colorValue;
				changedPixs[w * y + x][1] = colorValue;
				changedPixs[w * y + x][2] = colorValue;
			}
		}

		updatePixels();

		bufImg = null;
	}

	@Override
	public void sharpen() {
		initChangedPixs();
		int h = getHeight();
		int w = getWidth();
		int lastx, lasty, nextx, nexty;

		int[] rgb = new int[3];

		for (int y = 0; y < h; ++y) {
			lasty = max(y - 1, 0);
			nexty = min(y + 1, h - 1);
			for (int x = 0; x < w; ++x) {
				lastx = max(x - 1, 0);
				nextx = min(x + 1, w - 1);

				rgb[0] = (
							- getRed(x, lasty)
							- getRed(lastx, y)
							+ (6 * getRed(x, y))
							- getRed(nextx, y)
							- getRed(x, nexty)
						) / 2;
				rgb[0] = max(min(rgb[0], 255), 0);

				rgb[1] = (
							- getGreen(x, lasty)
							- getGreen(lastx, y)
							+ (6 * getGreen(x, y))
							- getGreen(nextx, y)
							- getGreen(x, nexty)
						) / 2;
				rgb[1] = max(min(rgb[1], 255), 0);

				rgb[2] = (
							- getBlue(x, lasty)
							- getBlue(lastx, y)
							+ (6 * getBlue(x, y))
							- getBlue(nextx, y)
							- getBlue(x, nexty)
						) / 2;
				rgb[2] = max(min(rgb[2], 255), 0);

				changedPixs[w * y + x][0] = rgb[0];
				changedPixs[w * y + x][1] = rgb[1];
				changedPixs[w * y + x][2] = rgb[2];
			}
		}

		updatePixels();

		bufImg = null;
	}

	@Override
	public void medianBlur() {
		initChangedPixs();
		int h = getHeight();
		int w = getWidth();
		int lastx, lasty, nextx, nexty;

		int[] red = new int[9];
		int[] green = new int[9];
		int[] blue = new int[9];

		for (int y = 0; y < h; ++y) {
			lasty = max(y - 1, 0);
			nexty = min(y + 1, h - 1);
			for (int x = 0; x < w; ++x) {
				lastx = max(x - 1, 0);
				nextx = min(x + 1, w - 1);

				red[0] = getRed(lastx, lasty);
				red[1] = getRed(x, lasty);
				red[2] = getRed(nextx, lasty);
				red[3] = getRed(lastx, y);
				red[4] = getRed(x, y);
				red[5] = getRed(nextx, y);
				red[6] = getRed(lastx, nexty);
				red[7] = getRed(x, nexty);
				red[8] = getRed(nextx, nexty);
				Arrays.sort(red);
				changedPixs[w * y + x][0] = red[4];

				green[0] = getGreen(lastx, lasty);
				green[1] = getGreen(x, lasty);
				green[2] = getGreen(nextx, lasty);
				green[3] = getGreen(lastx, y);
				green[4] = getGreen(x, y);
				green[5] = getGreen(nextx, y);
				green[6] = getGreen(lastx, nexty);
				green[7] = getGreen(x, nexty);
				green[8] = getGreen(nextx, nexty);
				Arrays.sort(green);
				changedPixs[w * y + x][1] = green[4];

				blue[0] = getBlue(lastx, lasty);
				blue[1] = getBlue(x, lasty);
				blue[2] = getBlue(nextx, lasty);
				blue[3] = getBlue(lastx, y);
				blue[4] = getBlue(x, y);
				blue[5] = getBlue(nextx, y);
				blue[6] = getBlue(lastx, nexty);
				blue[7] = getBlue(x, nexty);
				blue[8] = getBlue(nextx, nexty);
				Arrays.sort(blue);
				changedPixs[w * y + x][2] = blue[4];
			}
		}
		updatePixels();

		bufImg = null;
	}

	@Override
	public void uniformBlur() {
		initChangedPixs();
		int h = getHeight();
		int w = getWidth();
		int lastx, lasty, nextx, nexty;

		for (int y = 0; y < h; ++y) {
			lasty = max(y - 1, 0);
			nexty = min(y + 1, h - 1);
			for (int x = 0; x < w; ++x) {
				lastx = max(x - 1, 0);
				nextx = min(x + 1, w - 1);

				changedPixs[w * y + x][0] = (
							getRed(lastx, lasty)
							+ getRed(x, lasty)
							+ getRed(nextx, lasty)
							+ getRed(lastx, y)
							+ getRed(x, y)
							+ getRed(nextx, y)
							+ getRed(lastx, nexty)
							+ getRed(x, nexty)
							+ getRed(nextx, nexty)
						) / 9;

				changedPixs[w * y + x][1] = (
							getGreen(lastx, lasty)
							+ getGreen(x, lasty)
							+ getGreen(nextx, lasty)
							+ getGreen(lastx, y)
							+ getGreen(x, y)
							+ getGreen(nextx, y)
							+ getGreen(lastx, nexty)
							+ getGreen(x, nexty)
							+ getGreen(nextx, nexty)
						) / 9;

				changedPixs[w * y + x][2] = (
							getBlue(lastx, lasty)
							+ getBlue(x, lasty)
							+ getBlue(nextx, lasty)
							+ getBlue(lastx, y)
							+ getBlue(x, y)
							+ getBlue(nextx, y)
							+ getBlue(lastx, nexty)
							+ getBlue(x, nexty)
							+ getBlue(nextx, nexty)
						) / 9;
			}
		}
		updatePixels();

		bufImg = null;
	}

	@Override
	public void grayscale() {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int h = getHeight();
		int w = getWidth();

		int argb = 0;

		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				rgb = getPixel(x, y, rgb);

				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);
				argb = Color.HSBtoRGB(hsb[0], 0, hsb[2]);
				rgb[0] = (argb>>16)&0xFF;
				rgb[1] = (argb>>8)&0xFF;
				rgb[2] = (argb)&0xFF;

				setPixel(x, y, rgb);
			}
		}

		bufImg = null;
	}

	@Override
	public void contrast(int amount) {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int h = getHeight();
		int w = getWidth();
		
		int argb = 0;
		float change = (float) Math.pow(((amount + 100) / 100), 4);

		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				rgb = getPixel(x, y, rgb);

				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

				hsb[2] = change * (hsb[2] - 0.5f) + 0.5f;

				hsb[2] = max(min(hsb[2], 1.0f), 0);

				argb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
				rgb[0] = (argb>>16)&0xFF;
				rgb[1] = (argb>>8)&0xFF;
				rgb[2] = (argb)&0xFF;

				setPixel(x, y, rgb);
			}
		}
		bufImg = null;
	}

	@Override
	public void brightness(int change) {
		int[] rgb = new int[3];
		float[] hsb = new float[3];
		int h = this.getHeight();
		int w = getWidth();
		
		int argb = 0;
		float brightChange = change / 100.0f;

		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				rgb = getPixel(x, y, rgb);

				hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], hsb);

				hsb[2] = Math.max(Math.min(hsb[2] + brightChange, 1.0f), 0.0f);

				argb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
				rgb[0] = (argb>>16)&0xFF;
				rgb[1] = (argb>>8)&0xFF;
				rgb[2] = (argb)&0xFF;

				setPixel(x, y, rgb);
			}
		}
		bufImg = null;
	}

	private static int min(int a, int b) {
		return Math.min(a, b);
	}

	private static int max(int a, int b) {
		return Math.max(a, b);
	}
	

	private static float min(float a, float b) {
		return Math.min(a, b);
	}

	private static float max(float a, float b) {
		return Math.max(a, b);
	}

}
