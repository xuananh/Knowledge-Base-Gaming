package de.kbgame.util;

import java.awt.Color;
import java.awt.Point;

import de.kbgame.game.Enemy;
import de.kbgame.game.Entity;
import de.kbgame.game.Game;
import de.kbgame.map.Level;

public class PlayerShot {

	public static final int LIFE_TIME = 50;
	public static final int SHOT_KILL_SCORE = 50;
	
	private int x, y;
	private int factor;
	private int lifeTime;
	public static int velocity = 8;

	/**
	 * Der Spieler hat die Möglichkeit Schüsse abzufeuern.
	 * 
	 * @param start definiert den Ausgangspunkt für den abgefeuerten Schuss
	 * @param factor sollte den Wert 1 oder -1 bekommen, um die Richtung des Schusses anzugeben
	 * @param lifeTime die Anzahl an Frames, die der Schuss vorhanden ist
	 */
	public PlayerShot(Point start, int factor, int lifeTime) {
		x = start.x;
		y = start.y;
		
		this.factor = factor;
		this.lifeTime = lifeTime;
	}

	/**
	 * Die Methode berechnet die nächste x Position anhand des vorherigen Wertes, der Richtung (factor), der Geschwindgkeit und ggf.
	 * des gaFactors (künstliche Beschleunigung bei geringerer FPS Anzahl). Sie berechnet weiterhin Kollisionen mit undurchdringlichen
	 * Blöcken und Feinden. Normale Feinde werden bei einem Treffen sofort getötet.
	 * 
	 * @param g
	 */
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
					g.player.addPoints(SHOT_KILL_SCORE);
				}
			}
		}
	}

	public void draw(Game g) {
		g.graphic.drawOval(x, y, Shot.RADIUS, Shot.RADIUS, Color.BLACK);
	}

}
