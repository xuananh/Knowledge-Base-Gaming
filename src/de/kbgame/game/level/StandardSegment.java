package de.kbgame.game.level;

import java.awt.Point;

import de.kbgame.game.Game;
import de.kbgame.map.MapLoader;

public class StandardSegment extends LevelSegment {

	public StandardSegment(Game g, String[] args) {
		goal = new Point(-1,-1);
		
		map = MapLoader.loadFromBitmap(this, args[0], goal);
		width = map.length;
		height = map[0].length;
		
		if (goal.x < 0) {
			goal = null;
		}
	}

}
