package com.c2.leap;
import com.leapmotion.leap.*;
import com.leapmotion.leap.Gesture.State;
import com.leapmotion.leap.Gesture.Type;

public class LeapSensor extends Listener {

	private LeapParameterListener listener;
	public Controller controller;
	private LeapParameters parameters;
	private  boolean runOnce = false;
	private int gestureID;

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
//		System.out.println(parameters);
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

		// Vector representing hand velocity.
		Vector velocity = hands.rightmost().palmVelocity();

		// Calculate Pitch, Yaw, and Roll
		Vector normal = hands.rightmost().palmNormal();
		double pitch = Math.sin(normal.pitch());
		double yaw = Math.sin(normal.yaw());
		double roll = Math.sin(normal.roll());


		// Modify update method to include whatever parameters are desired. 
		parameters.update(sphereCenter, fingerCount, handSize, velocity, pitch, yaw, roll);

		listener.onLeapParametersChanged(parameters);
	}

	private void gestureProcessing(Frame frame) {
		if (!frame.fingers().empty()) {

			controller.enableGesture(Type.TYPE_SWIPE);
			controller.enableGesture(Type.TYPE_KEY_TAP);
			controller.enableGesture(Type.TYPE_SCREEN_TAP);
			controller.enableGesture(Type.TYPE_CIRCLE);
			
			Config config = new Config();
		

			GestureList gestureList = frame.gestures();

			for (Gesture gesture : gestureList) {
				if(gesture.type() == Type.TYPE_KEY_TAP) {

					if (!runOnce){
						gestureID=gesture.id();
						KeyTapGesture keyTap = new KeyTapGesture(gesture); // Can give more info about the key tap later
						runOnce=true;
					}
					if (gesture.state() == State.STATE_START && gesture.id() == gestureID) {
						listener.onNewGesture(GestureType.KEY_PRESS);
						System.out.println("Key Tap Start");
					}
					
					if (gesture.state() == State.STATE_STOP && gesture.id() == gestureID) {
						runOnce = false;
						System.out.println("Key Tap Stop");
					}
				}				
			}
		}


	}

	public void addListener(LeapParameterListener lpl) {
		listener = lpl;
	}
}
