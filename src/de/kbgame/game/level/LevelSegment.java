package de.kbgame.game.level;

import java.util.ArrayList;

import de.kbgame.game.Enemy;

public abstract class LevelSegment {

	protected int width, height;
	protected byte[][] map;
	protected ArrayList<Enemy> enemies = new ArrayList<Enemy>();

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte[][] getMap() {
		return map;
	}

	public void addEnemy(Enemy e) {
		enemies.add(e);
	}
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public void setMap(int x, int y, byte v) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			map[x][y] = v;
		}
	}

}
