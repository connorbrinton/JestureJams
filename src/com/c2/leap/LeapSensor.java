package com.c2.leap;

import com.leapmotion.leap.Controller;
import com.leapmotion.leap.Frame;

public class LeapSensor extends Listener {

	private LeapParameterListener listener;

	public void start() {
		// listener.onLeapParametersChanged(newParameters);
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
	}

	public void addListener(LeapParameterListener lpl) {
		listener = lpl;

	}
}
