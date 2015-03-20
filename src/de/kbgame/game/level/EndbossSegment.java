package de.kbgame.game.level;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;

import de.kbgame.game.Game;
import de.kbgame.game.SuperEnemy;
import de.kbgame.map.Level;
import de.kbgame.map.MapLoader;
import de.kbgame.util.sound.SoundKey;

public class EndbossSegment extends LevelSegment {

	public EndbossSegment(Game g, String[] args) throws FileNotFoundException {
		super(g);
		
		goal = new Point();
		playerStart = new Point();
		
		map = MapLoader.loadFromClingo(g, this, new File(args[0]), playerStart, goal);
		width = map.length;
		height = map[0].length;
		
		SuperEnemy superEnemy = new SuperEnemy(goal.x*Level.BLOCK_WIDTH,goal.y*Level.BLOCK_HEIGHT, Level.BLOCK_WIDTH*2, Level.BLOCK_HEIGHT*2, g.player, g);
		g.list.add(superEnemy);
		g.endboss = true;
		g.sounds.stopall();
		g.sounds.getMusic(SoundKey.BACKGROUND_ENDBOSS).playRepeated();
	}

}
