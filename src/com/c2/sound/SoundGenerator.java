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
import com.leapmotion.leap.Vector;

public class SoundGenerator implements LeapParameterListener {

	private static final double LEAP_X_RANGE = 300.0;
	private static final double LEAP_Y_RANGE = 650.0;
	private static final double LEAP_Z_RANGE = 250.0;
	private static final double LEAP_VEL_RANGE = 1000.0;
	private static final double HUMAN_LOW = 20.0;
	private static final double HUMAN_HIGH = 20000.0;
	private static final double C4_FREQ = getFrequency("C4");
	private static final double[] FREQS = new double[] {
		getFrequency("C4"),
		(getFrequency("C4") + getFrequency("D4"))/2,
		getFrequency("D4"),
		(getFrequency("E4") + getFrequency("D4"))/2,
		getFrequency("E4"),
		(getFrequency("E4") + getFrequency("F4"))/2,
		getFrequency("F4"),
		(getFrequency("F4") + getFrequency("G4"))/2,
		getFrequency("G4"),
		(getFrequency("G4") + getFrequency("A4"))/2,
		getFrequency("A4"),
		(getFrequency("A4") + getFrequency("B4"))/2,
		getFrequency("B4"),
		(getFrequency("B4") + getFrequency("C5"))/2,
		getFrequency("C5"),
		(getFrequency("C5") + getFrequency("D5"))/2,
		getFrequency("D5"),
		(getFrequency("E5") + getFrequency("D5"))/2,
		getFrequency("E5"),
		(getFrequency("E5") + getFrequency("F5"))/2,
		getFrequency("F5"),
		(getFrequency("F5") + getFrequency("G5"))/2,
		getFrequency("G5"),
		(getFrequency("G5") + getFrequency("A5"))/2,
		getFrequency("A5"),
		(getFrequency("A5") + getFrequency("B5"))/2,
		getFrequency("B5"),
	};

	Synthesizer synth;
	boolean receivingParameters = false;

	UnitOscillator oscLeft;
	UnitOscillator oscRight;
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
		oscLeft = new SawtoothOscillator();
		synth.add(oscLeft);
		oscLeft.frequency.set(C4_FREQ);
		oscLeft.amplitude.set(0.5);
		// Sawtooth
		oscRight = new SawtoothOscillator();
		synth.add(oscRight);
		oscRight.frequency.set(C4_FREQ);
		oscRight.amplitude.set(0.5);

		// Filter
		flp = new FilterLowPass();
		synth.add(flp);
		flp.frequency.set(C4_FREQ);

		// Connecting stuff upppp!
		oscLeft.output.connect(flp);
		flp.output.connect(lo);

		lo.start();
	}

	public static double getFrequency(String note) {
		return Note.getFrequencyForNote(MusicStringParser.getNote(note)
				.getValue());
	}

	public void onFirstLeapParameters(LeapParameters newParameters) {
	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		double ampRatio = 0.5*leapNormalizeX(newParameters.handPosition1.getX());
		ampRatio = ampRatio > 1 ? 1 : ampRatio;
		oscLeft.amplitude.set(ampRatio);
		ampRatio = 0.5*leapNormalizeX(newParameters.handPosition2.getX());
		ampRatio = ampRatio > 1 ? 1 : ampRatio;
		oscRight.amplitude.set(ampRatio);

		double cutRatio = 0.5 - 0.5*leapNormalizeZ(newParameters.handPosition1.getZ());
		// System.out.println(cutRatio);
		flp.frequency.set(HUMAN_HIGH/2 * cutRatio);

		double freqFrac = leapNormalizeY(newParameters.handPosition2.getY());
		double freq = FREQS[(int) (freqFrac*(FREQS.length - 1))];
		oscLeft.frequency.set(freq);
		
		freqFrac = leapNormalizeY(newParameters.handPosition2.getY());
		freq = FREQS[(int) (freqFrac*(FREQS.length - 1))];
		oscRight.frequency.set(freq);
	}

	public double leapNormalizeX(double coord) {
		return Math.min(Math.abs(coord) / LEAP_X_RANGE, 1);
	}

	public double leapNormalizeY(double coord) {
		return Math.min(Math.abs(coord) / LEAP_Y_RANGE, 1);
	}

	public double leapNormalizeZ(double coord) {
		return Math.min(Math.abs(coord) / LEAP_Z_RANGE, 1);
	}
	
	public double leapNormalizeVel(double velocity) {
		return Math.min(Math.abs(velocity) / LEAP_VEL_RANGE, 1);
	}

	@Override
	public void onNewGesture(GestureType gt, Vector position) {
		// TODO Auto-generated method stub

	}

}
