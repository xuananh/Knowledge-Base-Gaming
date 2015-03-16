package de.kbgame.game.level;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;

import de.kbgame.game.Game;
import de.kbgame.map.MapLoader;

public class GraebenSegment extends LevelSegment {

	public GraebenSegment(Game g, String[] args) throws FileNotFoundException {
		super(g);
		
		goal = new Point();
		playerStart = new Point();

		String[] params = new String[1]; 
		params[0] = "-c difficulty=" + args[1];
		
		map = MapLoader.loadFromClingo(g, new File(args[0]), playerStart, goal, params);
		width = map.length;
		height = map[0].length;
	}

}
