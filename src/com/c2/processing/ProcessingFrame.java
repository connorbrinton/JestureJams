package com.c2.processing;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.c2.leap.GestureType;
import com.c2.leap.LeapParameterListener;
import com.c2.leap.LeapParameters;
import com.leapmotion.leap.Vector;

import processing.core.PApplet;

public class ProcessingFrame extends JFrame implements LeapParameterListener {
	
	ProcessingApplet sketch;

	public ProcessingFrame() {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		((FlowLayout)panel.getLayout()).setVgap(0);
		sketch = new ProcessingApplet();
		panel.add(sketch);
		this.setContentPane(panel);
		
		this.setVisible(true);
		this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		sketch.init();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sketch.repaint();
	}
	
	public void start() {
		this.setVisible(true);
	}

	@Override
	public void onLeapParametersChanged(LeapParameters newParameters) {
		sketch.onLeapParametersChanged(newParameters);
	}

	@Override
	public void onNewGesture(GestureType gt, Vector position) {
		sketch.onNewGesture(gt, position);
	}
	
}
