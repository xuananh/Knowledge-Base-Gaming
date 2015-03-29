package de.kbgame.util.hud;

import java.util.Vector;

import de.kbgame.game.Game;

@SuppressWarnings("serial")
public class HUD extends Vector<HUDElement> {

	/**
	 * Speichert und löst das Zeichnen von HUD Elemente aus
	 * 
	 * @param g Game Instanz
	 * @see HUDElement#draw(Game)
	 */
	public void draw(Game g) {
		for (HUDElement elem : this) {
			elem.draw(g);
		}
	}

}
