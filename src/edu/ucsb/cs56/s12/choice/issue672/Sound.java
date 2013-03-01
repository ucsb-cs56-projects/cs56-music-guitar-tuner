package edu.ucsb.cs56.s12.choice.issue763;
import javax.sound.midi.*;

/**  Handles the sound for guitar tuner
 *
 * @author Omar Masri Lupita Davila, Taylor Lin
 * @version Choice Points Assignment Issue 672, S12
 *
 */

/*
 
 FOR MIDI CODES USED 
 SEE http://soundprogramming.net/file_formats/general_midi_instrument_list
 
 25 Acoustic Guitar (steel)
 26 Acoustic Guitar (nylon)
 27 Electric Guitar (jazz)
 28 Electric Guitar (clean)
 29 Electric Guitar (muted)
 30 Overdriven Guitar
 31 Distortion Guitar
 32 Guitar harmonics
 */


public class Sound extends Thread {
    
    private int instrument;
    private int note;
    
    // Variable is volatile because it may need to be accessed by multiple threads
    private volatile boolean stopRequested = false;
    
    
    
    /** Default no-arg constructor just initializes data members to 0 
     */
    public Sound() { instrument = 0; note = 0; }
    
    /** Class constructor takes two args and initializes stopRequested 
     *  false
     *   
     *   @param instrument - the MIDI value of the sound type 
     *   @param note  - the MIDI value of the pitch to be played
     */
    public Sound(int instrument, int note) {
        this.instrument = instrument;
        this.note = note;
        stopRequested = false;
    }
    
    
    /** Overridden run() method from class Thread
     *
     *  @see Thread
     */
    public void run(){  
        while(!stopRequested)
        {
            try{  
                Sequencer player = MidiSystem.getSequencer();  
                player.open();  
                Sequence seq = new Sequence(Sequence.PPQ, 4);  
                Track track = seq.createTrack();  
                MidiEvent event = null;  
                
                ShortMessage first = new ShortMessage();  
                first.setMessage(192, 1, instrument, 0);  
                MidiEvent changeInstrument = new MidiEvent(first, 1);  
                track.add(changeInstrument);  
                
                ShortMessage a = new ShortMessage();  
                a.setMessage(144,1,note, 100);  
                MidiEvent noteOn = new MidiEvent(a,1);  
                track.add(noteOn);  
                
                ShortMessage b = new ShortMessage();  
                b.setMessage(128,1,note, 100);  
                MidiEvent noteOff = new MidiEvent(b,16);  
                track.add(noteOff);  
                player.setSequence(seq);  
                player.start();
                try {
                    Thread.sleep(2000);
                } 
                catch (Exception iex) {
                    System.err.println("Thread interrupted");
                }
                player.close();
            }
            catch(Exception ex){  
                ex.printStackTrace();  
            }  
        }
    }
    
    /** sets stopRequested to true and interrupts the current thread
     *
     *  @see Thread
     */
    public void requestStop(){
        stopRequested = true;
        this.interrupt();
    }
    
}  // close
