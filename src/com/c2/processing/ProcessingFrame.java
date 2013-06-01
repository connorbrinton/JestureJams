package com.c2.processing;

import javax.swing.JFrame;
import javax.swing.JPanel;

import processing.core.PApplet;

public class ProcessingFrame extends JFrame {

	public class ProcessingApplet extends PApplet {

	}

	public ProcessingFrame() {
		this.setSize(600, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel = new JPanel();
		PApplet sketch = new ProcessingApplet();
		panel.add(sketch);
		this.add(panel);
		
		sketch.init();
		this.setVisible(true);
	}
	
	public void start() {
		this.setVisible(true);
	}
	
}
