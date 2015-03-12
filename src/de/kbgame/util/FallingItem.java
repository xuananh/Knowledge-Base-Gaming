package de.kbgame.util;

import java.awt.Color;
import java.awt.Point;

import de.kbgame.game.Game;
import de.kbgame.game.Player;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;

public class FallingItem {
	
	public static final int RADIUS = 20;
	
	private int time =100000;
	private int x=200, y=1000;
	private Player player;
	private int velocity=5;
	private int spaceX = -10;
	private boolean activated = false;
	
	

	
	public FallingItem(int x, int y, int spaceX, int velocity, Player player) {
		this.x = x;
		this.y = y;
		this.spaceX = spaceX;
		if (velocity!=0) { 
			this.velocity = velocity;
		}
		this.player = player;
	}
	
	// Test
	public FallingItem(Player player) {
		this. player=player;
	}
	
	
	public void update(Game g) {
		
		if (g.player.x>x+spaceX) {
			activated=true;
		}
		
		if (activated) {
			
			// max. Y-Wert eines Levels
			if (y<3000) {
				y += velocity;
				
				if (g.player.getSurroundingRectangle().intersects(x, y, RADIUS, RADIUS)) {
					g.player.getHit(null, g);
					y=3000;
				}
			}
	
		}
	}	
	
	
	public void draw(Game g) {
		if (time > 0) {
			g.graphic.drawOval(x, y, RADIUS, RADIUS, Color.ORANGE);
		}
	}
	
	
	
}
