package com.c2.leap;

import com.leapmotion.leap.Vector;

public class DummyLPL implements LeapParameterListener {

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		System.out.println(newParameters);
	}

	@Override
	public void onNewGesture(GestureType gt, Vector position) {
		// TODO Auto-generated method stub
		
	}

}
