package de.kbgame.util.hud;

import de.kbgame.game.Game;

public interface HUDElement {

	/**
	 * zeichnen Element mit Hilfe von Objekt Graphic der Game
	 * @param g Game Instance
	 */
	public void draw(Game g);

}
