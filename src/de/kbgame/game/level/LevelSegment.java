package de.kbgame.game.level;

import java.awt.Point;
import java.util.ArrayList;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.game.JumpBlock;
import de.kbgame.game.Platform;
import de.kbgame.map.Level;
import de.kbgame.util.FallingItem;
import de.kbgame.util.ShotCollection;

public abstract class LevelSegment {

	protected int width, height;
	protected byte[][] map;
	protected ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	protected ArrayList<Platform> platforms = new ArrayList<Platform>();
	protected ArrayList<FallingItem> fi = new ArrayList<FallingItem>();
	protected ArrayList<ShotCollection> shotCollections = new ArrayList<ShotCollection>();
	protected ArrayList<JumpBlock> jumpBlocks = new ArrayList<JumpBlock>();
	protected int[] observedValues = null;
	protected Point playerStart = null;
	protected Point goal = null; // block wise!
	protected Game game;
	
	public LevelSegment(Game game) {
		this.game = game;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public byte[][] getMap() {
		return map;
	}
	
	public void setPlayerStart(Point start) {
		this.playerStart = start;
	}
	
	public Point getPlayerStart() {
		return playerStart;
	}
	
	public Point getGoal() {
		return goal;
	}

	/**
	 * Speicherte eine neue Enemy Instanz, welche später dem Spiel hinzugefügt werden soll
	 * 
	 * @param e
	 */
	public void addEnemy(Enemy e) {
		enemies.add(e);
	}
	
	public ArrayList<Enemy> getEnemies() {
		return enemies;
	}
	
	/**
	 * Erstellt und speichert eine neue Plattform Instanz, welche später dem Spiel hinzugefügt werden soll
	 * 
	 * @param x der x Wert der Startposition der Plattform
	 * @param y der y Wert der Startposition der Plattform
	 * @param fromBlockIndex die Plattform bewegt sich vom und zu diesem Block
	 * @param toBlockIndex die Plattform bewegt sich zum und von diesem Block
	 * @param verticalMove die Plattform bewegt sich entweder vertikal oder horizontal
	 */
	public void addPlatform(int x, int y, int fromBlockIndex, int toBlockIndex, boolean verticalMove) {
		platforms.add(new Platform(game, x, y, Level.BLOCK_WIDTH, Level.BLOCK_HEIGHT, fromBlockIndex, toBlockIndex, verticalMove));
	}
	
	public ArrayList<Platform> getPlatform() {
		return platforms;
	}
	
	public ArrayList<JumpBlock> getJumpBlock() {
		return jumpBlocks;
	}

	public void setMap(int x, int y, byte v) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			map[x][y] = v;
		}
	}

	/**
	 * Speicherte eine neue FallingItem Instanz, welche später dem Spiel hinzugefügt werden soll
	 * 
	 * @param x der x Wert des FallingItems
	 * @param y der y Wert des FallingItems
	 */
	public void addFallingItem(int x, int y) {
		fi.add(new FallingItem(x,y));
	}

	public ArrayList<FallingItem> getFallingItems() {
		return fi;
	}

	/**
	 * Speicherte eine neue ShotCollection Instanz, welche später dem Spiel hinzugefügt werden soll
	 * 
	 * @param sc
	 */
	public void addShotCollection(ShotCollection sc) {
		sc.player = game.player;
		shotCollections.add(sc);
	}

	public ArrayList<ShotCollection> getShotCollections() {
		return shotCollections;
	}


	/**
	 * Definiert x Werte, bei dessen Überschreitung das LevelSegement (bzw. die abgeleitete Klasse) informiert werden möchte.
	 * 
	 * @param obs alle x Werte die überwacht werden sollen
	 */
	public void addObserverValues(int[] obs) {
		observedValues = obs;
	}
	
	public int[] getObservedValues() {
		return observedValues;
	}

}
