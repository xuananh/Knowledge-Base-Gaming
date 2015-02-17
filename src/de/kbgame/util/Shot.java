package de.kbgame.util;

import java.awt.Color;
import java.awt.Point;

import de.kbgame.game.Game;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;

public class Shot {
	
	public static final int RADIUS = 10;
	
	private int timeOffset;
	private int x, y;
	private ShotCollection shots;
	private int yBlock;
	
	public Shot(Point origin, int yOffset, int timeOffset, ShotCollection shots) {
		this.timeOffset = timeOffset;
		this.shots = shots;
		
		x = origin.x;
		y = origin.y - Level.BLOCK_HEIGHT / 4 * yOffset;
		
		yBlock = (int) y / Level.BLOCK_HEIGHT;
	}
	
	public void update(Game g) {
		if (timeOffset <= 0) {
			x -= shots.velocity;
			
			// player collision
			if (shots.playerHitBox.intersects(x, y, RADIUS, RADIUS)) {
				shots.player.getHit(null, g);
				shots.addToRemoveList(this);
			}
			
			int xBlock = (int) x / Level.BLOCK_WIDTH;
			if (g.level.getMap(xBlock, yBlock) != Blocks.Empty || x < 0 - RADIUS) {
				shots.addToRemoveList(this);
			} 
		} else {
			--timeOffset;
		}
	}

	public void draw(Game g) {
		if (timeOffset <= 0) {
			g.graphic.drawOval(x, y, RADIUS, RADIUS, Color.BLACK);
		}
	}
}