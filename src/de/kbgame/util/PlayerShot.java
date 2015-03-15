package de.kbgame.util;

import java.awt.Color;
import java.awt.Point;

import de.kbgame.game.Enemy;
import de.kbgame.game.Entity;
import de.kbgame.game.Game;
import de.kbgame.map.Level;

public class PlayerShot {

	public static final int LIFE_TIME = 50;
	
	private int x, y;
	private int factor;
	private int lifeTime;
	public static int velocity = 8;

	public PlayerShot(Point start, int factor, int lifeTime) {
		x = start.x;
		y = start.y;
		
		this.factor = factor;
		this.lifeTime = lifeTime;
	}

	public void update(Game g) {
		x += factor * velocity * g.gaFactor;
		
		int blockIndexX = (int) x / Level.BLOCK_WIDTH;
		int blockIndexY = (int) y / Level.BLOCK_HEIGHT;
		
		// block collision
		if (Physic.isBlocking(g, blockIndexX, blockIndexY) || x == 0 || x == g.level.width || --lifeTime <= 0) {
			g.player.removeShots.add(this);
		}
		
		// enemy collision
		Enemy e;
		for (Entity entity : g.list) {
			if (entity instanceof Enemy) {
				e = (Enemy) entity;
				if (e.getSurroundingRectangle().intersects(x, y, Shot.RADIUS, Shot.RADIUS)) {
					e.kill(g);
					g.player.removeShots.add(this);
				}
			}
		}
	}

	public void draw(Game g) {
		g.graphic.drawOval(x, y, Shot.RADIUS, Shot.RADIUS, Color.BLACK);
	}

}
