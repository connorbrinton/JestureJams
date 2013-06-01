package com.c2.sound;

import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;

public class SoundGenerator implements LeapParameterListener {

	public void start() {
		// TODO Auto-generated method stub
		}

	// LeapSensor runs this method every frame. Will eventually only run when parameters are changed.
	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		// TODO Auto-generated method stub
		System.out.println(newParameters);
	}

}
