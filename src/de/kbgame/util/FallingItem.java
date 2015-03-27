package de.kbgame.util;

import java.awt.Color;
import java.awt.Point;

import de.kbgame.game.Game;
import de.kbgame.game.Player;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;


// Herunterfallendes (toedliches) Objekt
public class FallingItem {
	
	public static final int RADIUS = 20;
	
	private int time =100000;
	public int x=200;

	public int y=1000;
	private Player player;
	private int velocity=5;
	private int spaceX = -10;
	private boolean activated = false;
	
	

	/**
	 * Constructor
	 *
	 * @param x 		X-Wert der Ausgangskoordinate
	 * @param y 		Y-Wert der Ausgangskoordinate
	 * @param spaceX 	Abstand zum Objekt
	 * @param velocity 	Geschwindigkeit des Objekts
	 * @param player 	Spielerdaten
	 */	
	public FallingItem(int x, int y, int spaceX, int velocity, Player player) {
		this.x = x;
		this.y = y;
		this.spaceX = spaceX;
		if (velocity!=0) { 
			this.velocity = velocity;
		}
		this.player = player;
	}
	
	/**
	 * Constructor 
	 *
	 * @param x 	X-Wert der Ausgangskoordinate
	 * @param y 	Y-Wert der Ausgangskoordinate
	 */	
	public FallingItem(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Constructor
	 *
	 * @param player 	Spielerdaten
	 * @return 
	 */
	public FallingItem(Player player) {
		this. player=player;
	}
	
	/**
	 * Update-Methode
	 *
	 * @param g 	Game-Datenobjekt
	 */	
	
	public void update(Game g) {
		
		// Faellt herunter, wenn sich der Spieler unterhalb des Objekts befindet 
		// und sich in der unmittelbaren Umgebung befindet
		int spaceX = 30;
		if ((g.player.x>x-spaceX) & (g.player.x<x+spaceX)) {
			if (g.player.y>y) {
				activated=true;
			}
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
	
	/**
	 * Draw-Methode
	 *
	 * @param g 	Game-Datenobjekt
	 */	
	public void draw(Game g) {
		if (time > 0) {
			g.graphic.drawOval(x, y, RADIUS, RADIUS, Color.BLACK);
		}
	}
	
	
	
}
