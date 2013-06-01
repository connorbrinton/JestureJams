package com.c2.leap;

import com.leapmotion.leap.Vector;

public class LeapParameters {
	
	private Vector handPosition;
	private int fingerCount;
	private double handSize;
	
	public String toString() {
		String visualParameters = "[";
		
		double positionX = handPosition.getX();
		double positionY = handPosition.getY();
		double positionZ = handPosition.getZ();
		visualParameters += "X = " + Double.toString(positionX) + ", ";	
		visualParameters += "Y = " + Double.toString(positionY) + ", ";
		visualParameters += "Z = " + Double.toString(positionZ) + ", ";
		visualParameters += "Fingers = " + Integer.toString(fingerCount) + ", ";
		visualParameters += "Hand Size = " + Double.toString(handSize) + "]";
		
		return visualParameters;
	}
	
	public Vector getHandPosition() {
		return handPosition;
	}
	
	public int getFingerCount() {
		return fingerCount;
	}
	
	public double getHandSize() {
		return handSize;
	}
	
	public void update(Vector position, int fingers, double size) {
		handPosition = position;
		fingerCount = fingers;
		handSize = size;
	}

}
