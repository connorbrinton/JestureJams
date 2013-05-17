package com.c2.central;

import com.c2.leap.LeapSensor;
import com.c2.sound.SoundGenerator;

public class Central {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SoundGenerator sg = new SoundGenerator();
		sg.start(); // Start sound generator
		LeapSensor ls = new LeapSensor();
		ls.start(); // Start leap sensing
		
		ls.addListener(sg);// Connect leap sensing to sound generator
	}

}
