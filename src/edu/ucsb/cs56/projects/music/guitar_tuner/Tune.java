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
	Tune() {this.pitch = 0; this.msg = new JLabel(); line = null;}

	// Constructor to set pitch
	Tune(int pitch, JLabel msg) {
		this.pitch = pitch;
		this.msg = msg;
		line = null;
		stopRequested = false;
	}

	static byte buffer[];
	static TargetDataLine line;

	static {
		try {
			final AudioFormat format = presetFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			int bufferSize = (int) format.getSampleRate()*format.getFrameSize();
			buffer = new byte[bufferSize];
		} catch(LineUnavailableException e) {
			System.err.println("Line Unavailable: " + e);
			System.exit(-1);
		}
	}

	// Override run method of Thread
	public void run(){
			msg.setText("Tuning...");
			while(!stopRequested) {
				line.read(buffer, 0, buffer.length);

				/* Now FFT with Minim package
					Declare a MAudioBuffer with the buffer array we have
					Declare an FFT with the MAudioBuffer
					Find amp of pitch
					If pitch isn't strong, need to tune
					Find which way to tune
					Display Up/Down/Done

				*/

			}
	}

	private static AudioFormat presetFormat() {
	float sampleRate = 8000;
    int sampleSizeInBits = 8;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = true;
    return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	public void requestStop() {
		stopRequested = true;
		this.interrupt();
	}
}