package com.c2.central;

import java.io.IOException;

import com.c2.leap.LeapSensor;
import com.c2.processing.ProcessingFrame;
import com.c2.sound.SoundGenerator;

public class Central {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProcessingFrame pa = new ProcessingFrame();
		pa.start();
		
		SoundGenerator sg = new SoundGenerator();
		sg.start(); // Start sound generator
		
		LeapSensor ls = new LeapSensor();
		ls.start(); // Start leap sensing
		
		ls.addListener(pa);
		ls.addListener(sg);// Connect leap sensing to sound generator
		
        System.out.println("Press Enter to quit...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
