package edu.ucsb.cs56.projects.music.guitar_tuner;

/** Main Class for Guitar tuner
 *
 * @author Omar Masri, Lupita Davila, Taylor Lin
 * @version Choice Points Assignment Issue 672, S12
 *
 */

public class GuitarTuner {
    
    public static void main(String[] args){
        
        // Make new tuner
        GuitarTunerGui tuner= new GuitarTunerGui();
        
        // Run GUI	
        tuner.runGUI();
    }
    
}
