package edu.ucsb.cs56.projects.music.guitar_tuner;
import javax.sound.sampled.*;
import javax.swing.JLabel;

public class Tune extends Thread {

	// The pitch we want to tune to
	private int pitch;
	JLabel msg;
	// Boolean to keep track of running
	private volatile boolean stopRequested = false;

	// Default Constructor
	Tune() {this.pitch = 0; this.msg = new JLabel();}

	// Constructor to set pitch
	Tune(int pitch, JLabel msg) {
		this.pitch = pitch;
		this.msg = msg;
		stopRequested = false;
	}

	// Override run method of Thread
	public void run(){
		while(!stopRequested) {
			// do stuff
			msg.setText("Tuning...");

		}
	}

	public void requestStop() {
		stopRequested = true;
		this.interrupt();
	}
}