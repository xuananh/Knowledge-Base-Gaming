package de.kbgame.game.level;

import de.kbgame.game.Game;
import de.kbgame.map.MapLoader;

public class StandardSegment extends LevelSegment {

	public StandardSegment(Game g, String[] args) {
		map = MapLoader.loadFromBitmap(this, args[0]);
		width = map.length;
		height = map[0].length;
	}

}
