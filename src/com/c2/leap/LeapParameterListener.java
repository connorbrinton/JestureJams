package com.c2.leap;

public interface LeapParameterListener {
	
	public void onLeapParametersChanged(LeapParameters newParameters);
	
	public void onNewGesture(GestureType gt, double height);


}
