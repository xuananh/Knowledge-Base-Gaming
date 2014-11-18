package de.kbgame.game;

import java.awt.event.KeyEvent;

import de.kbgame.map.Level;
import de.kbgame.util.Physic;

public class Controller {
	
	public int viewX, viewY;
	
	public Entity controlledOne = null;
	public boolean testing = false;
	
	public void control(Entity r){
		if (controlledOne != null) controlledOne.con = null;
		controlledOne = r;
		viewX = controlledOne.x;
		viewY = controlledOne.y;
		r.con = this;
	}
	
	public void update(Game g){
		if (controlledOne != null){
			viewX = controlledOne.x;
			viewY = controlledOne.y;
			if (g.input.getKey(KeyEvent.VK_UP)){
				if (controlledOne.onground) controlledOne.vy -= Physic.JUMP_VELOCITY;
			} else if (g.input.getKey(KeyEvent.VK_DOWN)){
				controlledOne.vy += 2;
			}

			if (g.input.getKey(KeyEvent.VK_LEFT)) {
				controlledOne.vx -= Physic.WALK_VELOCITY;
			} else if (g.input.getKey(KeyEvent.VK_RIGHT)) {
				controlledOne.vx += Physic.WALK_VELOCITY;
			}
			
			//TEST
			if (testing){
				if (g.input.getKey(KeyEvent.VK_A)){ // 1 px move through blocks
					g.input.dontAlertTillKeyUp(KeyEvent.VK_A);
					controlledOne.x -= 1;
				}
				if (g.input.getKey(KeyEvent.VK_D)){ // 1 px move through blocks
					g.input.dontAlertTillKeyUp(KeyEvent.VK_D);
					controlledOne.x += 1;
				}
				if (g.input.getKey(KeyEvent.VK_X)){ // Toggle Objekt Boxes
					g.input.dontAlertTillKeyUp(KeyEvent.VK_X);
					System.out.println(controlledOne.x % Level.BLOCK_WIDTH);
				}
			}
		}

		if (g.input.getKey(KeyEvent.VK_ESCAPE)) 
			g.shouldApplicationExit = true;
		
		//TEST
		if (testing){
			
			if (g.input.getKey(KeyEvent.VK_SPACE)){ // Restarts and plays the Sound
				g.input.dontAlertTillKeyUp(KeyEvent.VK_SPACE);
				g.sounds.playMusic(0);
			}
			
			if (g.input.getKey(KeyEvent.VK_M)){ // Stops the Sound
				g.input.dontAlertTillKeyUp(KeyEvent.VK_M);
				g.sounds.stopMusic(0);
			}
			
			if (g.input.getKey(KeyEvent.VK_B)){ // Half Volume
				g.input.dontAlertTillKeyUp(KeyEvent.VK_B);
				g.sounds.setAllVolume(0.5f);
			}
			
			if (g.input.getKey(KeyEvent.VK_N)){ // Full Volume
				g.input.dontAlertTillKeyUp(KeyEvent.VK_N);
				g.sounds.setAllVolume(1f);
			}
		}
		
	}

}
