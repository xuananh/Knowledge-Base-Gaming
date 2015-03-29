package de.kbgame.util;

import de.kbgame.game.Game;

public interface XValueObserver {
	
	/**
	 * Klassen, die über das Überschreiten des Spielers eines gewissen x Wertes informiert werden wollen,
	 * müssen dieses Interface implementieren und werden mittels dieser Methode benachrichtigt.
	 * 
	 * @param g
	 */
	public void notifyObserver(Game g);

}
