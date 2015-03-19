package de.kbgame.util.sound;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class SoundThread extends Thread{
	
	boolean selfdestruct = false;
	boolean stopall = false;
	boolean setVolume = false;
	
	float setVolumeTo = 1f;
	float Volume = 1f;
	
	Map<SoundKey,Sound> music = new HashMap<SoundKey,Sound>();
	Map<SoundKey,Sound> sounds = new HashMap<SoundKey,Sound>();
	
	LinkedList<SoundKey> incToPlay = new LinkedList<SoundKey>();
	LinkedList<SoundKey> incToStop = new LinkedList<SoundKey>();
	LinkedList<SoundKey> incToPlayMusic = new LinkedList<SoundKey>();
	LinkedList<SoundKey> incToStopMusic = new LinkedList<SoundKey>();
	

	LinkedList<SoundKey> toPlay = new LinkedList<SoundKey>();
	LinkedList<SoundKey> toStop = new LinkedList<SoundKey>();
	LinkedList<SoundKey> toPlayMusic = new LinkedList<SoundKey>();
	LinkedList<SoundKey> toStopMusic = new LinkedList<SoundKey>();
	
	public SoundThread(){
		loadSound();
		this.start();
	}
	
	public Sound getMusic(SoundKey key) {
		return music.get(key);
	}
	
	public Sound getSound(SoundKey key) {
		return sounds.get(key);
	}
	
	public void playMusic(SoundKey key){
		if (music.get(key) != null){
			incToPlayMusic.add(key);
		}
	}
	
	public void stopMusic(SoundKey key){
		if (music.get(key) != null){
			incToStopMusic.add(key);
		}
	}
	
	public void sound(SoundKey key){
		if (sounds.get(key) != null){
			incToPlay.add(key);
		}
	}
	
	public void stop(SoundKey key){
		if (sounds.get(key) != null){
			incToStop.add(key);
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
				for (Entry<SoundKey, Sound> e:sounds.entrySet()) e.getValue().stop();
				for (Entry<SoundKey, Sound> e:music.entrySet()) e.getValue().stop();
			}else{
				toStop.clear();
				toPlay.clear();
				toStopMusic.clear();
				toPlayMusic.clear();
				toStop.addAll(incToStop);
				toPlay.addAll(incToPlay);
				toStopMusic.addAll(incToStopMusic);
				toPlayMusic.addAll(incToPlayMusic);
				for (SoundKey i:toStop){
					sounds.get(i).stop();
					incToStop.remove(i);
				}
				for (SoundKey i:toPlay){
					sounds.get(i).play();
					incToPlay.remove(i);
				}
				for (SoundKey i:toStopMusic){
					music.get(i).stop();
					incToStopMusic.remove(i);
				}
				for (SoundKey i:toPlayMusic){
					music.get(i).play();
					incToPlayMusic.remove(i);
				}
			}
			if (setVolume && Volume != setVolumeTo){
				Volume = setVolumeTo;
				setVolume = false;
				for (Entry<SoundKey, Sound> e:sounds.entrySet()) e.getValue().setVolume(Volume);
				for (Entry<SoundKey, Sound> c:music.entrySet()) c.getValue().setVolume(Volume);
			}
			
			try {
				Thread.sleep(100);//10 times each second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void loadSound() {
		BufferedReader bfr = null;
		try {
			FileReader fstream = new FileReader("sound/conf.txt");
			bfr = new BufferedReader(fstream);
			String line;
			while ( (line = bfr.readLine()) != null){
				if(line.contains(":")) {
					String[] s = line.trim().split(":");
					if(s.length == 3) {
						if("SOUND".equals(s[0])) {
							sounds.put(SoundKey.valueOf(s[1]), new Sound(s[2]));
						} else if("MUSIC".equals(s[0])) {
							music.put(SoundKey.valueOf(s[1]), new Sound(s[2]));
						}
					}
				}
			}
		} catch(Exception e){ 
			e.printStackTrace();
		} finally{
			try{ 
				bfr.close();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}
