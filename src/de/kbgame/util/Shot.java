package de.kbgame.util;

import java.awt.Color;

import de.kbgame.game.Game;
import de.kbgame.map.Level;

public class Shot {

	public static final int RADIUS = 10;

	private int timeOffset;
	private int x, y;
	private ShotCollection shots;
	private int yBlock;
	private int yOffset;

	public Shot(int yOffset, int timeOffset, ShotCollection shots) {
		this.timeOffset = timeOffset;
		this.shots = shots;
		this.yOffset = yOffset;
	}
	
	public Shot(Shot s) {
		this(s.yOffset, s.timeOffset, s.shots);
	}

	public void update(Game g) {
		if (timeOffset == 0) {
			x = shots.origin.x;
			y = shots.origin.y - Level.BLOCK_HEIGHT / 4 * yOffset;
			yBlock = (int) y / Level.BLOCK_HEIGHT;
			timeOffset--;
		}

		if (timeOffset <= 0) {
			x -= shots.velocity;

			// player collision
			if (shots.playerHitBox.intersects(x, y, RADIUS, RADIUS)) {
				g.player.getHit(null, g);
				shots.addToRemoveList(this);
			}

			int xBlock = (int) x / Level.BLOCK_WIDTH;
			if (Physic.isBlocking(g, xBlock, yBlock) || x < 0 - RADIUS) {
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
	
	public int getTimeOffset() {
		return timeOffset;
	}
}
