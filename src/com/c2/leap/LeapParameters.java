package com.c2.leap;

import com.leapmotion.leap.Vector;

public class LeapParameters {
	
	public Vector handPosition1;
	public Vector handPosition2;
	public int fingerCount;
	public double handSize;
	public Vector handVelocity;
	public double pitch;
	public double yaw;
	public double roll;
	
	public String toString() {
		String visualParameters = "[";
		
		double positionX = handPosition1.getX();
		double positionY = handPosition1.getY();
		double positionZ = handPosition1.getZ();
		visualParameters += "X = " + Double.toString(positionX) + ", ";	
		visualParameters += "Y = " + Double.toString(positionY) + ", ";
		visualParameters += "Z = " + Double.toString(positionZ) + ", ";
//		visualParameters += "Fingers = " + Integer.toString(fingerCount) + ", ";
//		visualParameters += "Hand Size = " + Double.toString(handSize) + ", ";
		visualParameters += "Hand Velocity = " + handVelocity.magnitude() + ", ";
//		visualParameters += "Pitch = " + Double.toString(pitch) + ", ";
//		visualParameters += "Yaw = " + Double.toString(yaw) + ", ";
//		visualParameters += "Roll = " + Double.toString(roll) + "]";
		
		return visualParameters;
	}
	
	public void update(Vector position1, Vector position2, int fingers, double size, Vector velocity, double inputPitch, double inputYaw, double inputRoll) {
		handPosition1 = position1;
		handPosition2 = position2;
		fingerCount = fingers;
		handSize = size;
		handVelocity = velocity;
		pitch = inputPitch;
		yaw = inputYaw;
		roll = inputRoll;
	}

}
