package com.c2.sound;

import org.jfugue.MusicStringParser;
import org.jfugue.Note;

import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;
import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.FilterLowPass;
import com.jsyn.unitgen.LineOut;
import com.jsyn.unitgen.SawtoothOscillator;
import com.jsyn.unitgen.UnitOscillator;

public class SoundGenerator implements LeapParameterListener {
	
	private static final double HUMAN_LOW = 20.0;
	private static final double HUMAN_HIGH = 20000.0;

	Synthesizer synth;
	boolean receivingParameters = false;
	
	UnitOscillator osc;

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

		float cfreq = getFrequency("C4");
		// Initialization
		osc = new SawtoothOscillator();
		synth.add(osc);
		FilterLowPass flp = new FilterLowPass();
		synth.add(flp);
		// Parameters
		osc.frequency.set(cfreq);
		osc.amplitude.set(0.5);
		
		osc.output.connect(flp);
		flp.output.connect(lo);
		
		lo.start();
	}
	
	public float getFrequency(String note) {
		return (float) Note.getFrequencyForNote(MusicStringParser.getNote(note).getValue());
	}
	
	public void onFirstLeapParameters(LeapParameters newParameters) {
	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		if (!receivingParameters) {
			receivingParameters = true;
			onFirstLeapParameters(newParameters);
		} else {
			// Do OnChanged stuff
		}
	}

}
