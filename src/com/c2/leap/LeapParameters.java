package com.c2.leap;

import com.leapmotion.leap.Vector;

public class LeapParameters {
	
	public Vector handPosition;
	public int fingerCount;
	public double handSize;
	public Vector handVelocity;
	public double pitch;
	public double yaw;
	public double roll;
	
	public String toString() {
		String visualParameters = "[";
		
		double positionX = handPosition.getX();
		double positionY = handPosition.getY();
		double positionZ = handPosition.getZ();
		visualParameters += "X = " + Double.toString(positionX) + ", ";	
//		visualParameters += "Y = " + Double.toString(positionY) + ", ";
		visualParameters += "Z = " + Double.toString(positionZ) + ", ";
//		visualParameters += "Fingers = " + Integer.toString(fingerCount) + ", ";
//		visualParameters += "Hand Size = " + Double.toString(handSize) + ", ";
//		visualParameters += "Hand Velocity = " + handVelocity.magnitude() + ", ";
//		visualParameters += "Pitch = " + Double.toString(pitch) + ", ";
//		visualParameters += "Yaw = " + Double.toString(yaw) + ", ";
//		visualParameters += "Roll = " + Double.toString(roll) + "]";
		
		return visualParameters;
	}
	
	public void update(Vector position, int fingers, double size, Vector velocity, double inputPitch, double inputYaw, double inputRoll) {
		handPosition = position;
		fingerCount = fingers;
		handSize = size;
		handVelocity = velocity;
		pitch = inputPitch;
		yaw = inputYaw;
		roll = inputRoll;
	}

}
