package de.kbgame.util.sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Sound {
	
	public Clip clip = null;
	public FloatControl volume = null;
	
	public Sound(String Filename){
		File f = new File(Filename);
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(f);
			DataLine.Info inf = new DataLine.Info(Clip.class, ais.getFormat());
			clip = (Clip) AudioSystem.getLine(inf);
			clip.open(ais);
			if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN))
				volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN); // Does not work with Pulseaudio!
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void playRepeated(){
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void play(){
		if (clip == null) return;
		try{
			if (clip.isActive()){
				clip.stop();
			}
			clip.setFramePosition(0);
			clip.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void continuePlay(){
		if (clip == null) return;
		try{
			clip.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void stop(){
		if (clip == null) return;
		if (clip.isActive()){
			clip.stop();
			clip.setFramePosition(0);
		}
	}
	
	public void pause(){
		if (clip == null) return;
		if (clip != null && clip.isActive()){
			clip.stop();
		}
	}

	public void setVolume(float val){ // Between 0f and 1f
		if (volume == null) return;
		volume.setValue(val);
	}
	
}
