package com.c2.leap;
import com.leapmotion.leap.*;

public class LeapSensor extends Listener {

	private LeapParameterListener listener;
	public Controller controller;
	private LeapParameters parameters;

	public void start() {
		controller = new Controller();
		
		// Have the sample listener receive events from the controller
		controller.addListener(this);
	}

	@Override
	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	@Override
	public void onConnect(Controller controller) {
		System.out.println("Connected");
	}

	@Override
	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}

	@Override
	public void onExit(Controller controller) {
		System.out.println("Exited");
		System.exit(0);
	}

	@Override
	public void onFrame(Controller controller) {
		// Get the most recent frame and report some basic information
		Frame frame = controller.frame();
		
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
		
		
		System.out.println("Frame Received");
		
		// Modify update method to include whatever parameters are desired. 
		parameters.update(sphereCenter, fingerCount, handSize, velocity, pitch, yaw, roll);
		
		listener.onLeapParametersChanged(parameters);
	}

	public void addListener(LeapParameterListener lpl) {
		listener = lpl;

	}
}
