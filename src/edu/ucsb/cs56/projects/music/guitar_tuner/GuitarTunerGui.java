package edu.ucsb.cs56.projects.music.guitar_tuner;
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;


/** GUI that displays Guitar tuner
 *
 * @author Omar Masri, Lupita Davila, Taylor Lin
 * @version Choice Points Assignment Issue 672, S12
 *
 */


/*					* Changes Summary *
 *
 *	- The application will now have the look and feel of the OS it is running on 
 *	
 *	- Each button now has a separate ActionListener   
 *	
 *	- Now each time the user clicks a sound button, it stops/interrupts the current Sound thread 
 *	  (if one is running), creates a new Sound thread, and runs the thread. 	
 *	
 *	- Now includes an image of a fretboard lined up with the appropriate buttons for each string	
 */

public class GuitarTunerGui{
    
    
    // Initialize swing objects
    
    private JFrame f = new JFrame("Guitar Tuner");
    private JPanel panelC = new JPanel(); 
    private JPanel panelN = new JPanel(); 
    private JPanel panelS = new JPanel();
    
    private JButton E = new JButton("E6");
    private JButton A  = new JButton("A5");
    private JButton D = new JButton("D4");
    private JButton G = new JButton("G3");
    private JButton B = new JButton("B2");
    private JButton e = new JButton("E1");
    private JButton stop = new JButton("Stop");
    
    //Initialize button group for radio buttons
    
    final ButtonGroup soundType = new ButtonGroup();
    
    // Radio Buttons
    
    private JRadioButton steel = new JRadioButton("Acoustic Steel");
    private JRadioButton harmonics = new JRadioButton("Harmonics");
    private JRadioButton nylon = new JRadioButton("Acoustic Nylon");
    
    //Initialize headline
    
    JLabel description= new JLabel("Pick type of sound: ");
    
	// Stores MIDI code for type of sound
    
    private int instrument;
    
    
    // Declare and initialize constants for instrument type and note sound MIDI codes
    
    public static final int ACOUSTIC_STEEL = 25; 
    public static final int HARMONICS = 32;
    public static final int ACOUSTIC_NYLON = 26;
    public static final int E6 = 40; // MIDI code for E string (lower octave) 
    public static final int A5 = 45; // MIDI code for A string
    public static final int D4 = 50; // MIDI code for D string
    public static final int G3 = 55; // MIDI code for G string
    public static final int B2 = 59; // MIDI code for B string
    public static final int E1 = 64; // MIDI code for e string
    
    
    public void runGUI() {
        
        /* 
         This block of code will make the frame look like other applications 
         on the platform (OS) they are running on. 
         */
        
        try{
            String look = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(look);
        } catch (Exception e) { e.printStackTrace(); }
        
        // Initialize label to store picture
        
        JLabel picLabel = null;
        
        try{
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            BufferedImage myPicture = ImageIO.read(classLoader.getResourceAsStream("fretboard.png"));
            picLabel = new JLabel(new ImageIcon(myPicture ));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("File not found.");
        }
        
        //Add description and radio buttons to north panel
        
        panelN.add(description);
        panelN.add(steel);
        panelN.add(nylon);
        panelN.add(harmonics);
        
        // Add the note sound buttons to a vertical box
        
        Box buttonBar = Box.createVerticalBox();
        buttonBar.add(Box.createVerticalGlue());
        buttonBar.add(e);
        buttonBar.add(Box.createVerticalGlue());
        buttonBar.add(B);
        buttonBar.add(Box.createVerticalGlue());
        buttonBar.add(G);
		buttonBar.add(Box.createVerticalGlue());
        buttonBar.add(D);
        buttonBar.add(Box.createVerticalGlue());
        buttonBar.add(A);
        buttonBar.add(Box.createVerticalGlue());
        buttonBar.add(E);
        buttonBar.add(Box.createVerticalGlue());
        
        // Add Fretboard picture to center 
        
        panelC.add(picLabel);
        
        // Add stop button to bottom of panel
        
        panelS.add(stop);
        
        // Button ActionListeners
        
        E.addActionListener(new E6Listener());	
        A.addActionListener(new A5Listener());			     
        D.addActionListener(new D4Listener());	
        G.addActionListener(new G3Listener());	
        B.addActionListener(new B2Listener());
        e.addActionListener(new E1Listener());
        
        // Added Stop button ActionListener
        
        stop.addActionListener(new StopListener());
        
        // set ActionCommand & ActionListener for radio buttons
        
        steel.setActionCommand("steel");
        harmonics.setActionCommand("harmonics");
        nylon.setActionCommand("nylon");
        
        // Set first button to be selected 
        
        steel.setSelected(true);
        
        // Added Radio Button ActionListeners
        
        steel.addActionListener(new SoundTypeListener());
        harmonics.addActionListener(new SoundTypeListener());
        nylon.addActionListener(new SoundTypeListener());
        
        // Add to button group
        
        soundType.add(steel);
        soundType.add(nylon);
        soundType.add(harmonics);
        
        // Display Frame
        
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Add panel/box content to frame
        
        f.add(buttonBar, BorderLayout.WEST);
        f.add(BorderLayout.CENTER, panelC);
        f.add(BorderLayout.NORTH, panelN);
        f.add(BorderLayout.SOUTH, panelS);
        
        // Let pack() method set the size of the GUI to fit the contents appropriately 
        
        f.pack();
		f.setResizable(false);
        f.setVisible(true);
        
    } // End of runGUI()
    
    
    // Create new thread for playing the pitch sounds
    
    Sound sample = new Sound();
    
    public class SoundTypeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            
            String choice = soundType.getSelection().getActionCommand();
            System.out.println(choice);  
            if(choice == "steel")
                instrument = ACOUSTIC_STEEL;
            if(choice == "nylon")
                instrument = ACOUSTIC_NYLON;
            if(choice == "harmonics")
                instrument = HARMONICS;
        }
    }
    
    public class E6Listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            sample = new Sound(instrument, E6);
            sample.start();
	    }
	}
    
    public class A5Listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            sample = new Sound(instrument, A5);
            sample.start();
        }
    }
    
    public class D4Listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            sample = new Sound(instrument, D4);
            sample.start();
        }
    }
    
    public class G3Listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            sample = new Sound(instrument, G3);
            sample.start();
        }
    }
    
    public class B2Listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            sample = new Sound(instrument, B2);
            sample.start();
        }
    }
    
    public class E1Listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
            sample = new Sound(instrument, E1);
            sample.start();
        }
    }
    
    public class StopListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if(!sample.interrupted())
                sample.requestStop();
        }
	}
    
    
    
} // GuitarTunerGui

