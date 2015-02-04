package de.kbgame.util.hud;

import java.util.Vector;

import de.kbgame.game.Game;

@SuppressWarnings("serial")
public class HUD extends Vector<HUDElement> {

	public void draw(Game g) {
		for (HUDElement elem : this) {
			elem.draw(g);
		}
	}

}
