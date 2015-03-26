package de.kbgame.util.hud;

import java.awt.Color;
import java.awt.Font;

import de.kbgame.game.Game;
import de.kbgame.game.Player;
import de.kbgame.map.Level;

public class Points implements HUDElement {

	public int points = 0;

	public final static int OFFSET_LEFT = Level.BLOCK_WIDTH / 5;
	public final static int OFFSET_TOP = Level.BLOCK_HEIGHT / 5;

	/**
	 * Sammeln eine COIN wird 50 Punkte mehr
	 */
	public final static int COIN_SCORE = 50;

	/**
	 * Konstruktor fuer neue Points Objekt
	 * @param points starende Punkte
	 */
	public Points(int points) {
		this.points = points;
	}

	/**
	 * fuege mehre Punkte zur bestimmte Spieler hinzu. Wenn Punkte mehre als 200 sind,
	 * wird fuer bestimmte Spieler ein Herz hinzugefuegt
	 * 
	 * @param plus Punkte, die hinzugefuegt werden
	 * @param player Punkete fuer diesem Spieler
	 */
	public void add(int plus, Player player) {
		points += plus;
		while (points >= 200) {
			points = points - 200;
			player.lifes.add();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.kbgame.util.hud.HUDElement#draw(de.kbgame.game.Game)
	 */
	public void draw(Game g) {
		g.graphic.drawText("POINTS: " + points, 10, 15, Color.blue, false, new Font("Arial", Font.BOLD, 14));
	}

}
