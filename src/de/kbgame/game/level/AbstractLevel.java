package de.kbgame.game.level;

import de.kbgame.map.Level;

public abstract class AbstractLevel {
	
	protected Level level;
	protected int offsetX;
	protected int width, height;
	
	public AbstractLevel(Level level, int offsetX) {
		this.level = level;
		this.offsetX = offsetX;
	}

	abstract public int getWidth();
	abstract public int getHeight();
	abstract public byte[][] getMap();

}
