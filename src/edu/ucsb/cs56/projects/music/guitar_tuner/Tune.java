package edu.ucsb.cs56.projects.music.guitar_tuner;
import javax.sound.sampled.*;
import javax.swing.JLabel;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.*;
import ddf.minim.*;
import ddf.minim.analysis.FFT;

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

/*
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
*/
	// Override run method of Thread
	public void run(){
			msg.setText("Tuning...");
							try {
			final AudioFormat format = presetFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			int bufferSize = (int) format.getSampleRate()*format.getFrameSize();
			buffer = new byte[bufferSize];
			MyAudioBuffer audioBuffer = new MyAudioBuffer(bufferSize);
		
			while(!stopRequested) {

				line.read(buffer, 0, buffer.length);

				//System.out.println(Arrays.toString(buffer));
				/* Now FFT with Minim package
					Declare a MAudioBuffer with the buffer array we have
					Declare an FFT with the MAudioBuffer
					Find amp of pitch
					If pitch isn't strong, need to tune
					Find which way to tune
					Display Up/Down/Done

				*/

				//Convert bytes to float
				ByteArrayInputStream bas = new ByteArrayInputStream(buffer);
				DataInputStream ds = new DataInputStream(bas);
				float[] fArr = new float[buffer.length / 4];  // 4 bytes per float
				for (int i = 0; i < fArr.length; i++) {
	    			try{
    				fArr[i] = ds.readFloat();
				} catch (IOException e) {System.err.println("shit");}
				}

				//set buffer and FFT stuff
				audioBuffer.set(fArr);
				FFT myFFT = new FFT(fArr.length, format.getSampleRate());
				myFFT.forward(fArr);
				//harcoded for D4
				msg.setText(Float.toString(myFFT.getFreq((float) 146.832)));

			}
			} catch(LineUnavailableException e) {
			System.err.println("Line Unavailable: " + e);
			System.exit(-1);
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

	public class MyAudioBuffer implements AudioBuffer {
private float[] samples;

  /**
   * Constructs and MAudioBuffer that is <code>bufferSize</code> samples long.
   * 
   * @param bufferSize
   *          the size of the buffer
   */
  MyAudioBuffer(int bufferSize)
  {
    samples = new float[bufferSize];
  }

  public synchronized int size()
  {
    return samples.length;
  }

  public synchronized float get(int i)
  {
    return samples[i];
  }

  public synchronized void set(float[] buffer)
  {
    if (buffer.length != samples.length)
      Minim
          .error("MAudioBuffer.set: passed array (" + buffer.length + ") " + 
              "must be the same length (" + samples.length + ") as this MAudioBuffer.");
    else
      samples = buffer;
  }

  /**
   * Mixes the two float arrays and puts the result in this buffer. The
   * passed arrays must be the same length as this buffer. If they are not, an
   * error will be reported and nothing will be done. The mixing function is:
   * <p>
   * <code>samples[i] = (b1[i] + b2[i]) / 2</code>
   * 
   * @param b1
   *          the first buffer
   * @param b2
   *          the second buffer
   */
  public synchronized void mix(float[] b1, float[] b2)
  {
    if ((b1.length != b2.length)
        || (b1.length != samples.length || b2.length != samples.length))
    {
      Minim.error("MAudioBuffer.mix: The two passed buffers must be the same size as this MAudioBuffer.");
    }
    else
    {
      for (int i = 0; i < samples.length; i++)
      {
        samples[i] = (b1[i] + b2[i]) / 2;
      }
    }
  }

  /**
   * Sets all of the values in this buffer to zero.
   */
  public synchronized void clear()
  {
    samples = new float[samples.length];
  }
  
  public synchronized float level()
  {
    float level = 0;
    for (int i = 0; i < samples.length; i++)
    {
      level += (samples[i] * samples[i]);
    }
    level /= samples.length;
    level = (float) Math.sqrt(level);
    return level;
  }

  public synchronized float[] toArray()
  {
    float[] ret = new float[samples.length];
    System.arraycopy(samples, 0, ret, 0, samples.length);
    return ret;
  }
	}
}