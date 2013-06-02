package com.c2.processing;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.c2.leap.GestureType;
import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;
import com.leapmotion.leap.Vector;

import processing.core.PApplet;

public class ProcessingApplet extends PApplet implements LeapParameterListener {

	ImgProc imgProc = new ImgProc();

	float noiseScale = 0.005f;
	float noiseZ = 0;
	int particlesDensity = 8;
	int particleMargin = 64;
	Particle[] particles;
	int[] currFrame;
	int[] prevFrame;
	int[] tempFrame;

	private static final double LEAP_X_RANGE = 300.0;
	private static final double LEAP_Y_RANGE = 650.0;
	private static final double LEAP_Z_RANGE = 250.0;
	private static final double LEAP_VEL_RANGE = 1000.0;
	double varX = 0.5;

	public void setup() {
		getParent().addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentResized(ComponentEvent e) {
				/*
				 * noLoop(); size(e.getComponent().getWidth(),
				 * e.getComponent().getHeight()); repeatSetup(); loop();
				 */
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub

			}
		});
		size(getParent().getWidth(), getParent().getHeight());
		frameRate(50);
		colorMode(HSB, 255);
		repeatSetup();
	}

	public void repeatSetup() {
		currFrame = new int[width * height];
		prevFrame = new int[width * height];
		tempFrame = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			currFrame[i] = color(0, 0, 0);
			prevFrame[i] = color(0, 0, 0);
			tempFrame[i] = color(0, 0, 0);
		}

		particles = new Particle[(width + particleMargin * 2)
				/ particlesDensity * (height + particleMargin * 2)
				/ particlesDensity];
		int i = 0;
		for (int y = -particleMargin; y < height + particleMargin; y += particlesDensity) {
			for (int x = -particleMargin; x < width + particleMargin; x += particlesDensity) {
				if (i == particles.length) {
					println(i);
					break;
				}
				int c = color((int) (50 + 50 * sin(PI * x / width)), 127,
						255 * sin(PI * y / width));
				// Modulate color
				particles[i++] = new Particle(x, y, c);
			}
		}
	}

	public void draw() {
		noiseZ += 2 * noiseScale;

		imgProc.blur(prevFrame, tempFrame, width, height);
		imgProc.scaleBrightness(tempFrame, tempFrame, width, height, 0.2f);
		arraycopy(tempFrame, currFrame);

		for (int i = 0; i < particles.length; i++) {
			particles[i].update();
			particles[i].draw();
		}
//		imgProc.updateByParameters(currFrame, width, height);
		imgProc.drawPixelArray(currFrame, 0, 0, width, height);
		arraycopy(currFrame, prevFrame);
	}

	class Particle {
		float x;
		float y;
		int c;
		
		
		float speed = 2;

		Particle(int x, int y, int c) {
			this.x = x;
			this.y = y;
			this.c = c;
		}

		void update() {
			float noiseVal = noise(x * noiseScale, y * noiseScale, noiseZ);
			float angle = noiseVal * 2 * PI;
			x += speed * cos(angle);
			y += speed * sin(angle);

			if (x < -particleMargin) {
				x += width + 2 * particleMargin;
			} else if (x > width + particleMargin) {
				x -= width + 2 * particleMargin;
			}

			if (y < -particleMargin) {
				y += height + 2 * particleMargin;
			} else if (y > height + particleMargin) {
				y -= height + 2 * particleMargin;
			}
		}

		void draw() {
			if ((x >= 0) && (x < width - 1) && (y >= 0) && (y < height - 1)) {
				int currC = currFrame[(int) x + ((int) y) * width];
				currFrame[(int) x + ((int) y) * width] = blendColor(c, currC,
						ADD);
			}
		}
	}

	public class ImgProc {

		void ImgProc() {
		}

		void drawPixelArray(int[] src, int dx, int dy, int w, int h) {
			// backBuf.loadPixels();
			// arraycopy(src, backBuf.pixels);
			// backBuf.updatePixels();
			// image(backBuf, dx, dy);
			loadPixels();
			int x;
			int y;
			for (int i = 0; i < w * h; i++) {
				x = dx + i % w;
				y = dy + i / w;
				pixels[x + y * w] = src[i];
			}
			updatePixels();
		}

		void blur(int[] src, int[] dst, int w, int h) {
			int c;
			int r;
			int g;
			int b;
			for (int y = 1; y < h - 1; y++) {
				for (int x = 1; x < w - 1; x++) {
					r = 0;
					g = 0;
					b = 0;
					for (int yb = -1; yb <= 1; yb++) {
						for (int xb = -1; xb <= 1; xb++) {
							c = src[(x + xb) + (y - yb) * w];
							r += (c >> 16) & 0xFF;
							g += (c >> 8) & 0xFF;
							b += (c) & 0xFF;
						}
					}
					r /= 9;
					g /= 9;
					b /= 9;
					dst[x + y * w] = 0xFF000000 | (r << 16) | (g << 8) | b;
				}
			}
		}

		// you must be in RGB colorModel
		void scaleBrightness(int[] src, int[] dst, int w, int h, float s) {
			int r;
			int g;
			int b;
			int c;
			int a;
			float as = s;
			s = 1.0f;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					c = src[x + y * w];
					a = (int) (as * ((c >> 24) & 0xFF));
					g = (int) (s * ((c >> 8) & 0xFF));
					r = (int) ( varX*g + (s * ((c >> 16) & 0xFF)))/2;
					b = (int) (s * ((c) & 0xFF));
					dst[x + y * w] = (a << 24) | (r << 16) | (g << 8) | b;
					// ch = hue(c);
					// cs = saturation(c);
					// cb = brightness(c) * s;
					// dst[x + y*w] = color(ch, cs, cb);
					// dst[x + y*w] = src[x + y*w];
				}
			}
		}

		public void updateByParameters(int[] currFrame, int w, int h) {
			int r;
			int g;
			int b;
			int c;
			int a;
			for (int y = 0; y < h; y++) {
				for (int x = 0; x < w; x++) {
					c = currFrame[x + y * w];
					a = (int) (((c >> 24) & 0xFF));
//					r = (int) (2 * varX * ((c >> 16) & 0xFF));
					r = (int) (varX * 255);
					g = (int) (((c >> 8) & 0xFF));
					b = (int) (((c) & 0xFF));
					currFrame[x + y * w] = (a << 24) | (r << 16) | (g << 8) | b;
					// ch = hue(c);
					// cs = saturation(c);
					// cb = brightness(c) * s;
					// dst[x + y*w] = color(ch, cs, cb);
					// dst[x + y*w] = src[x + y*w];
				}
			}
		}

	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		varX = Math.max(leapNormalizeX(newParameters.handPosition1.getX()), leapNormalizeX(newParameters.handPosition2.getX()) );
//		System.out.println(varX);
	}

	public double leapNormalizeX(double coord) {
		return Math.min(Math.abs(coord) / LEAP_X_RANGE, 1);
	}

	public double leapNormalizeY(double coord) {
		return Math.min(Math.abs(coord) / LEAP_Y_RANGE, 1);
	}

	public double leapNormalizeZ(double coord) {
		return Math.min(Math.abs(coord) / LEAP_Z_RANGE, 1);
	}

	public double leapNormalizeVel(double velocity) {
		return Math.min(Math.abs(velocity) / LEAP_VEL_RANGE, 1);
	}

	@Override
	public void onNewGesture(GestureType gt, Vector position) {
		// TODO Auto-generated method stub

	}

}