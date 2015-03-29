package de.kbgame.util.hud;

import de.kbgame.game.Game;

public interface HUDElement {

	/**
	 * HUDElemente müssen dieses Interface implementieren und somit eine öffentliche draw Methode bereitstellen.
	 * 
	 * @param g
	 */
	public void draw(Game g);

}
