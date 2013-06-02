package com.c2.leap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;
import com.leapmotion.leap.Gesture;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.Gesture.Type;
import com.leapmotion.leap.GestureList;
import com.leapmotion.leap.HandList;
import com.leapmotion.leap.KeyTapGesture;
import com.leapmotion.leap.Listener;
import com.leapmotion.leap.SwipeGesture;
import com.leapmotion.leap.Vector;

public class LeapSensor extends Listener {

	private List<LeapParameterListener> listeners = new ArrayList<LeapParameterListener>();
	public Controller controller;
	private LeapParameters parameters;

	public void start() {
		controller = new Controller();

		// Have the sample listener receive events from the controller
		controller.addListener(this);

		parameters = new LeapParameters();

	}


	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}


	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}


	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}


	public void onExit(Controller controller) {
		System.out.println("Exited");
	}


	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();

		parameterProcessing(frame);
		gestureProcessing(frame);
				System.out.println(parameters);
	}

	private void parameterProcessing(Frame frame) {

		// Count Fingers		
		int fingerCount = frame.fingers().count();

		HandList hands = frame.hands();

		// Size of open hand. Ignores outside hand interference to the left.
		double handSize = hands.rightmost().sphereRadius();

		// Vector location of hand in x,y, and z coordinates
		Vector handPosition = hands.rightmost().palmPosition();

		// Vector representing center of hand's sphere. May have more interesting musical applications than translational
		// locations.
		Vector sphereCenter = hands.rightmost().sphereCenter();
		
		Vector handPosition2 = hands.leftmost().palmPosition();
		double pitch2 = Math.sin(hands.leftmost().palmNormal().pitch());
		double yaw2 = Math.sin(hands.leftmost().palmNormal().yaw());
		double roll2 = Math.sin(hands.leftmost().palmNormal().roll());

		// Vector representing hand velocity.
		Vector velocity = hands.rightmost().palmVelocity();

		// Calculate Pitch, Yaw, and Roll
		Vector normal = hands.rightmost().palmNormal();
		double pitch = Math.sin(normal.pitch());
		double yaw = Math.sin(normal.yaw());
		double roll = Math.sin(normal.roll());

/*		addVectorToAverage(sphereCenter);
		Vector average = getVectorAverage();*/

		// Modify update method to include whatever parameters are desired. 
		parameters.update(handPosition, handPosition2, fingerCount, handSize, velocity, pitch, yaw, roll, pitch2, yaw2, roll2);

		if(!frame.hands().empty()) {
//			System.out.println(parameters);
			for (LeapParameterListener lpl : listeners) {
				lpl.onLeapParametersChanged(parameters);
			}
		}
	}
	
/*	private Vector getVectorAverage() {
		Vector average = new Vector();
		for (Vector v : data) {
			average.setX(average.getX() + v.getX()/data.size());
			average.setY(average.getY() + v.getY()/data.size());
			average.setZ(average.getZ() + v.getZ()/data.size());
		}
		return average;
	}


	private void addVectorToAverage(Vector sphereCenter) {
		data.add(sphereCenter);
		if (data.size() > DATA_CAP) {
			data.poll();
		}
	}*/


	private void gestureProcessing(Frame frame) {
		if (!frame.fingers().empty()) {


			// Configure Custom Gesture Settings
			controller.config().setFloat("Gesture.KeyTap.MinDownVelocity ", 30);
			controller.config().setFloat("Gesture.Swipe.MinVelocity ", 1000);
			controller.config().save();

			controller.enableGesture(Type.TYPE_SWIPE);
			controller.enableGesture(Type.TYPE_KEY_TAP);
			controller.enableGesture(Type.TYPE_SCREEN_TAP);
			controller.enableGesture(Type.TYPE_CIRCLE);



			GestureList gestureList = frame.gestures();

			for (Gesture gesture : gestureList) {
				if(gesture.type() == Type.TYPE_KEY_TAP) {
					KeyTapGesture tap = new KeyTapGesture(gesture);
					if(tap.state() ==State.STATE_STOP) {
						for (LeapParameterListener lpl : listeners) {
							lpl.onNewGesture(GestureType.KEY_PRESS, tap.hands().rightmost().palmPosition());
						}
						System.out.println("Key Gesture FINALLY!!");
					}
				}
				if(gesture.type() == Type.TYPE_SWIPE) {
					SwipeGesture swipe = new SwipeGesture(gesture);
					if (swipe.state() == State.STATE_STOP) {
						for (LeapParameterListener lpl : listeners) {
							lpl.onNewGesture(GestureType.SWIPE, swipe.direction());
						}
						System.out.println("Swipe Gesture!!");
					}

				}
			}
		}

	}

	public void addListener(LeapParameterListener lpl) {
		listeners.add(lpl);
	}
}
