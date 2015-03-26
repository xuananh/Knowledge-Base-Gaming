package de.kbgame.util.hud;

import java.util.Vector;

import de.kbgame.game.Game;

@SuppressWarnings("serial")
public class HUD extends Vector<HUDElement> {

	/**
	 * zeichnen alle Element mit Hilfe von Objekt Graphic der Game
	 * @param g Game Instance
	 * @see HUDElement#draw(Game)
	 */
	public void draw(Game g) {
		for (HUDElement elem : this) {
			elem.draw(g);
		}
	}

}
