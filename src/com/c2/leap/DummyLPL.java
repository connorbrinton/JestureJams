package com.c2.leap;

public class DummyLPL implements LeapParameterListener {

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		System.out.println(newParameters);
	}

}
