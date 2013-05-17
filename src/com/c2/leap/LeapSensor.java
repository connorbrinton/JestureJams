package com.c2.leap;

public class LeapSensor {

	/**
	 * @param args
	 */
	
	
	private LeapParameterListener listener;
	
	public void start() {
		// listener.onLeapParametersChanged(newParameters);
	}

	public void addListener(LeapParameterListener lpl) {
		listener = lpl;
		
	}
}
