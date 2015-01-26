/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sachy;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Lukáš
 */
public class hudba extends Thread{

    @Override
    public void run() {

        try {
            Clip clip;
            AudioInputStream inputStream;
            inputStream = AudioSystem.getAudioInputStream(new File("tiger.wav"));
            clip = AudioSystem.getClip();
            clip.open(inputStream);
            clip.start();
            clip.loop(100);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            Logger.getLogger(hudba.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.printf("A");

        
    }
    
}
