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

	/**
	 * 
	 * @param yOffset der y Offset vom Ursprungspunkt aus, welcher in der ShotCollection definiert ist
	 * @param timeOffset ein timeOffset größer 0 bewirkt, dass ein Schuss erst zu einem späteren Zeitpunkt abgefeuert wird
	 * @param shots die ShotCollection, die verwandte Schüsse gruppiert und zusätzliche Informationen zur Verfügung stellt
	 * 
	 * @see de.kbgame.util.ShotCollection
	 */
	public Shot(int yOffset, int timeOffset, ShotCollection shots) {
		this.timeOffset = timeOffset;
		this.shots = shots;
		this.yOffset = yOffset;
	}
	
	/**
	 * Kopierkonstruktor.
	 * 
	 * @param s eine andere Shot-Instanz
	 */
	public Shot(Shot s) {
		this(s.yOffset, s.timeOffset, s.shots);
	}

	/**
	 * Ist ein timeOffset größer 0 angegeben, wird dieser zuerst in jedem Aufruf um 1 reduziert. 
	 * Die Geschwindigkeit der Schüsse wird in der gemeinsamen ShotCollection definiert. Ebenso der
	 * Ausgangspunkt. 
	 * Die Methode prüft auch Kollisionen mit dem Spieler und mit undurchdringlichen Blöcken.
	 * 
	 * @param g
	 */
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
