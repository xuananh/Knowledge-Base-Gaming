package de.kbgame.util.sound;

import java.util.ArrayList;
import java.util.LinkedList;

public class SoundThread extends Thread{
	
	boolean selfdestruct = false;
	boolean stopall = false;
	boolean setVolume = false;
	
	float setVolumeTo = 1f;
	float Volume = 1f;
	
	ArrayList<Sound> music = new ArrayList<Sound>();
	ArrayList<Sound> sounds = new ArrayList<Sound>();
	
	LinkedList<Integer> incToPlay = new LinkedList<Integer>();
	LinkedList<Integer> incToStop = new LinkedList<Integer>();
	LinkedList<Integer> incToPlayMusic = new LinkedList<Integer>();
	LinkedList<Integer> incToStopMusic = new LinkedList<Integer>();
	

	LinkedList<Integer> toPlay = new LinkedList<Integer>();
	LinkedList<Integer> toStop = new LinkedList<Integer>();
	LinkedList<Integer> toPlayMusic = new LinkedList<Integer>();
	LinkedList<Integer> toStopMusic = new LinkedList<Integer>();
	
	public SoundThread(){
		Sound s = new Sound("sound/SunnyDay.wav");
		s.playRepeated();
		s.play();
		//soundloop.run();
		this.start();
	}
	
	public void playMusic(int index){
		if (music.size() > index && index >= 0){
			incToPlayMusic.add(index);
		}
	}
	
	public void stopMusic(int index){
		if (music.size() > index && index >= 0){
			incToStopMusic.add(index);
		}
	}
	
	public void sound(int index){
		if (sounds.size() > index && index >= 0){
			incToPlay.add(index);
		}
	}
	
	public void stop(int index){
		if (sounds.size() > index && index >= 0){
			incToStop.add(index);
		}
	}
	
	public void stopall(){
		stopall = true;
	}
	
	public void dispose(){
		selfdestruct = true;
	}
	
	//Just a test.
	public void setAllVolume(float value){
		if (Volume != value){
			setVolume = true;
			setVolumeTo = value;
		}else{
			setVolume = false;
		}
	}
	
	@Override
	public void run(){
		while(!selfdestruct){

			if (stopall){
				for (Sound c:sounds) c.stop();
				for (Sound c:music) c.stop();
			}else{
				toStop.clear();
				toPlay.clear();
				toStopMusic.clear();
				toPlayMusic.clear();
				toStop.addAll(incToStop);
				toPlay.addAll(incToPlay);
				toStopMusic.addAll(incToStopMusic);
				toPlayMusic.addAll(incToPlayMusic);
				for (Integer i:toStop){
					sounds.get(i).stop();
					incToStop.remove(i);
				}
				for (Integer i:toPlay){
					sounds.get(i).play();
					incToPlay.remove(i);
				}
				for (Integer i:toStopMusic){
					music.get(i).stop();
					incToStopMusic.remove(i);
				}
				for (Integer i:toPlayMusic){
					System.out.println(incToPlayMusic.size());
					music.get(i).play();
					incToPlayMusic.remove(i);
				}
			}
			if (setVolume && Volume != setVolumeTo){
				Volume = setVolumeTo;
				setVolume = false;
				for (Sound c:sounds) c.setVolume(Volume);
				for (Sound c:music) c.setVolume(Volume);
			}
			
			try {
				Thread.sleep(100);//10 times each second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
