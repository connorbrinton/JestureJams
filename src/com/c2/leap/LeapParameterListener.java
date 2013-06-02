package com.c2.leap;

import com.leapmotion.leap.Vector;

public interface LeapParameterListener {
	
	public void onLeapParametersChanged(LeapParameters newParameters);
	
	public void onNewGesture(GestureType gt, Vector position);


}
