package com.c2.sound;

import org.jfugue.MusicStringParser;
import org.jfugue.Note;

import com.c2.leap.GestureType;
import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.UnitOscillator;

public class SoundGenerator implements LeapParameterListener {
	
	private static final double LEAP_X_RANGE = 300.0;
	private static final double LEAP_Y_RANGE = 650.0;
	private static final double LEAP_Z_RANGE = 250.0;
	
	private static final double HUMAN_LOW = 20.0;
	private static final double HUMAN_HIGH = 20000.0;
	private static final double C7_FREQ = getFrequency("C7");

	Synthesizer synth;
	boolean receivingParameters = false;
	
	UnitOscillator osc;
	FilterLowPass flp;

	public void start() {
		synth = JSyn.createSynthesizer();
		synth.start();
		// Set up the graph
		setUpGraph(synth);
	}
	
	public void setUpGraph(Synthesizer synth) {
		// Graph terminal
		LineOut lo = new LineOut();
		synth.add(lo);

		// Sawtooth
		osc = new SawtoothOscillator();
		synth.add(osc);
		flp = new FilterLowPass();
		synth.add(flp);
		// Parameters
		osc.frequency.set(C7_FREQ);
		osc.amplitude.set(0.5);
		osc.output.connect(flp);
		flp.output.connect(lo);
		
		lo.start();
	}
	
	public static double getFrequency(String note) {
		return Note.getFrequencyForNote(MusicStringParser.getNote(note).getValue());
	}
	
	public void onFirstLeapParameters(LeapParameters newParameters) {
	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		if (!receivingParameters) {
			receivingParameters = true;
			onFirstLeapParameters(newParameters);
		} else {
			double freqRatio = leapNormalizeY(newParameters.handPosition.getY());
			freqRatio = freqRatio > 1 ? 1 : freqRatio;
			osc.frequency.set(C7_FREQ*freqRatio);
			
			double cutRatio = 1 - leapNormalizeZ(newParameters.handPosition.getZ());
			flp.frequency.set(HUMAN_HIGH*cutRatio);
		}
	}

	public double leapNormalizeX(double coord) {
		return Math.min(Math.abs(coord)/LEAP_X_RANGE, 1);
	}
	
	public double leapNormalizeY(double coord) {
		return Math.min(Math.abs(coord)/LEAP_Y_RANGE, 1);
	}
	
	public double leapNormalizeZ(double coord) {
		return Math.min(Math.abs(coord)/LEAP_Z_RANGE, 1);
	}

	@Override
	public void onNewGesture(GestureType gt , double height) {
		// TODO Auto-generated method stub
		
	}

}
