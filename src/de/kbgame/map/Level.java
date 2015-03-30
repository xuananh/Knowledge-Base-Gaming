package de.kbgame.map;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.game.JumpBlock;
import de.kbgame.game.JumpEvent;
import de.kbgame.game.Platform;
import de.kbgame.game.level.LevelSegment;
import de.kbgame.geometry.ImageKey;
import de.kbgame.geometry.MyPoint;
import de.kbgame.util.FallingItem;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.XValueObserver;

public class Level {

	public int width, height;
	private final byte[][] map;
	private final ArrayList<LevelSegment> levelParts = new ArrayList<LevelSegment>();

	private int left, right, top, bottom;
	private Point goal = null;

	public final static int BLOCK_WIDTH = 50, BLOCK_HEIGHT = 50;
	public final static Point DEFAULT_PLAYER_START = new Point(1, 40);
	public final static String CONFIG_REG_EX = "(\\w+)\\((.*)\\)";
	public final static String LEVEL_SEGMENT_PACKAGE = "de.kbgame.game.level";

	public Level(int wi, int hi) {
		width = Math.max(10, wi);
		height = Math.max(10, hi);
		map = new byte[width][height];
	}

	public void update(Game g) {
		int centerX = g.graphic.viewX;
		int centerY = g.graphic.viewY;

		left = (int) (centerX - g.graphic.Width / 2) / BLOCK_WIDTH - 1;
		right = (int) (centerX + g.graphic.Width / 2) / BLOCK_WIDTH + 1;
		top = (int) (centerY - g.graphic.Height / 2 - 0.9) / BLOCK_HEIGHT - 1;
		bottom = (int) (centerY + g.graphic.Height / 2 + 0.9) / BLOCK_HEIGHT + 1;
	}

	/**
	 * Zeichnet Blöcke anhand ihrer jeweiliger Block-ID. Einige Blocke werden auch anhand von Spriteanimationen gezeichnet. Diese sind 
	 * in Game statisch definiert.
	 * 
	 * @param g
	 */
	public void draw(Game g) {
		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				byte val = getMap(x, y);
				switch (val) {
					case Blocks.Solid: {
						g.graphic.drawImage(ImageKey.BLOCK, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						break;
					}
					case Blocks.Floor: {
						g.graphic.drawImage(ImageKey.BODEN, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						break;
					}
					case Blocks.QuestionBlock: {
						g.graphic.drawImage(ImageKey.QUESTION_BLOCK, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						// Test: System.out.println("QuestionBlock: " + (x * BLOCK_WIDTH + BLOCK_WIDTH / 2) + " " + (y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2));
						break;
					}
					case Blocks.FireBlock: {
						g.graphic.drawImage(Game.fire.getCurrent(), x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_WIDTH, BLOCK_HEIGHT, 0f, true);
						break;
					}
					case Blocks.JUMP: {
						g.graphic.drawImage(ImageKey.JUMP_BLOCK, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						break;
					}
					case Blocks.QUESTION_BLOCK_BOUNCED:
						g.graphic.drawImage(ImageKey.QUESTIONBLOCK_BOUNCED_IMAGE, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);

						break;
					case Blocks.GOAL:
						g.graphic.drawImage(ImageKey.GOAL, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						break;
					case Blocks.CannonBlock:
						g.graphic.drawImage(ImageKey.CANNON, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						break;
					case Blocks.COIN:
						g.graphic.drawImage(Game.coins.getCurrent(), x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_WIDTH, BLOCK_HEIGHT, 0f, true);
						break;
					case Blocks.CAVE:
						g.graphic.drawRectangle(x * BLOCK_WIDTH, y * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT, Color.BLACK);
						break;
				}
			}
		}
	}

	public byte getMap(int x, int y) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			return map[x][y];
		} else {
			return 1;
		}
	}

	public void setMap(int x, int y, byte v) {
		if (x >= 0 && y >= 0 && x < width && y < height) {
			map[x][y] = v;
		}
	}
	
	/**
	 * Überschreibt einen bestimmten Block innerhalb eines Levelsegments. Wirkt sich ebenfalls
	 * auf die allgmeine Map des gesamten Levels aus.
	 * 
	 * @param segment das Segment, dessen Map angepasst werden soll
	 * @param x der x Block-Index
	 * @param y der y Block-Index 
	 * @param block die neue Block-ID für den gewählten Block
	 */
	public void setSegmentMap(LevelSegment segment, int x, int y, byte block) {
		int xOffset = getOffsetByLevel(segment);
		int yOffset = height - segment.getHeight();
		
		setMap(xOffset + x, yOffset + y, block);
	}

	/**
	 * Erstellt eine große Map, die die Maps der einzelnen Segmente – an den richtigen Positionen – enthält.
	 * Level mit geringerer Höhe werden oben mit soliden Blöcken aufgefüllt.
	 */
	public void createMap() {
		byte[][] map;
		int offsetTop, offsetLeft = 0;
		int levelWidth;

		for (LevelSegment level : levelParts) {
			// for levels of smaller height some extra space is needed above
			offsetTop = height - level.getHeight();
			map = level.getMap();
			levelWidth = level.getWidth();

			for (int col = offsetLeft; col < levelWidth + offsetLeft; col++) {
				for (int row = 0; row < height; row++) {
					if (row < offsetTop) {
						// fill extra space above with solid blocks
						this.map[col][row] = Blocks.Solid;
					} else {
						// copy map of level segment
						this.map[col][row] = map[col - offsetLeft][row - offsetTop];
					}
				}
			}
			
			// look for a goal in the last segment
			if (level.equals(levelParts.get(levelParts.size() - 1))) {
				Point goal = levelParts.get(levelParts.size() - 1).getGoal();
				
				if (goal != null) {
					this.goal = new Point(goal.x + offsetLeft, goal.y + offsetTop);
				}
			}

			offsetLeft += levelWidth;
		}
	}

	public void add(LevelSegment level) {
		levelParts.add(level);
	}

	/**
	 * Ermittelt den Offset eines LevelSegments in der Liste.
	 * 
	 * @param level das Levelsegment, dessen Offset berechnet werden soll
	 * @return der gewünschte Offset
	 */
	public int getOffsetByLevel(LevelSegment level) {
		int offset = 0;

		for (LevelSegment lvl : levelParts) {
			if (lvl != level) {
				offset += lvl.getWidth();
			} else {
				return offset;
			}
		}

		throw new ArrayIndexOutOfBoundsException();
	}

	/**
	 * Liest die in den einzelnen Segmenten gepufferten Enemies und fügt sie an der richtigen Stelle 
	 * im Spiel ein.
	 * 
	 * @param g
	 */
	private void insertEnemies(Game g) {
		int offsetX = 0, offsetY;

		for (LevelSegment segment : levelParts) {
			offsetY = (height - segment.getHeight()) * Level.BLOCK_HEIGHT;

			for (Enemy e : segment.getEnemies()) {
				e.x += offsetX;
				e.y += offsetY;
				g.list.add(e);
			}

			offsetX += segment.getWidth() * Level.BLOCK_WIDTH;
		}
	}

	/**
	 * Liest die in den einzelnen Segmenten gepufferten FallingItems und fügt sie an der richtigen Stelle 
	 * im Spiel ein.
	 * 
	 * @param g
	 */
	private void insertFallingItems(Game g) {
		int offsetX = 0, offsetY;

		for (LevelSegment segment : levelParts) {
			offsetY = (height - segment.getHeight()) * Level.BLOCK_HEIGHT;

			for (FallingItem f : segment.getFallingItems()) {
				f.x += offsetX;
				f.y += offsetY;
				g.fallingItemList.add(f);
			}

			offsetX += segment.getWidth() * Level.BLOCK_WIDTH;
		}
		
	}
	
	/**
	 * Liest die in den einzelnen Segmenten gepufferten ShotCollections und fügt sie an der richtigen Stelle 
	 * im Spiel ein.
	 */
	private void insertShotCollections(Game g) {
		int offsetX = 0, offsetY=0;
		
		for (LevelSegment segment : levelParts) {
			offsetY = (height - segment.getHeight()) * Level.BLOCK_HEIGHT;
			
			for (ShotCollection sc : segment.getShotCollections()) {
				sc.origin.x += offsetX;
				sc.origin.y += offsetY;
				sc.autofill();
				g.shots.add(sc);
			}
			
			offsetX += segment.getWidth() * Level.BLOCK_WIDTH;
		}
	}
	
	/**
	 * Liest die in den einzelnen Segmenten gepufferten Plattformen und fügt sie an der richtigen Stelle 
	 * im Spiel ein.
	 * 
	 * @param g
	 */
	private void insertPlatforms(Game g) {
		int offsetX = 0, offsetY;

		for (LevelSegment segment : levelParts) {
			offsetY = (height - segment.getHeight()) * Level.BLOCK_HEIGHT;

			for (Platform p : segment.getPlatform()) {
				p.x += offsetX;
				p.y += offsetY;
				p.fromXY += p.verticalMove ? offsetY : offsetX;
				p.toXY += p.verticalMove ? offsetY : offsetX;
				g.platforms.add(p);
			}

			offsetX += segment.getWidth() * Level.BLOCK_WIDTH;
		}
	}
	
	/**
	 * Liest die in den einzelnen Segmenten gepufferten JumpBlocks und fügt sie an der richtigen Stelle 
	 * im Spiel ein.
	 * 
	 * @param g
	 */
	private void insertJumpBlocks(Game g) {
		int offsetX = 0, offsetY;
		JumpEvent e;
		MyPoint p;

		for (LevelSegment segment : levelParts) {
			offsetY = (height - segment.getHeight()) * Level.BLOCK_HEIGHT;

			for (JumpBlock j : segment.getJumpBlock()) {
				j.x += offsetX / Level.BLOCK_WIDTH;
				j.y += offsetY / Level.BLOCK_HEIGHT;
				j.setMap(this);
				
				e = j.getJumpEvent();
				p = e.getPoint();
				p.x += offsetX;
				p.y += offsetY;
				
				g.jumpBlocks.put(new Point(j.x, j.y), j);
			}

			offsetX += segment.getWidth() * Level.BLOCK_WIDTH;
		}
	}
	
	/**
	 * Liest die in den einzelnen Segmenten gepufferten zu überwachenden x Werte aus und meldet diese
	 * beim Game an. Der Observer ist dabei immer das jeweilige Segment selbst.
	 * 
	 * @param g
	 */
	private void subscribeObservers(Game g) {
		int offsetX = 0;

		for (LevelSegment segment : levelParts) {			
			int[] observedXValues = segment.getObservedValues();
			
			if (observedXValues != null && segment instanceof XValueObserver) {
				for (int i = 0; i < observedXValues.length; i++) {
					g.subscribeXPass((XValueObserver) segment, observedXValues[i] + offsetX);
				}
			}

			offsetX += segment.getWidth() * Level.BLOCK_WIDTH;
		}
	}

	/**
	 * Erstellt aus einzelnen Segmenten ein neues Level.
	 * 
	 * @param levelParts die einzelnen Segmente, die zusammengesetzt werden sollen
	 * @param g
	 * @param playerStart die Methode überschreibt diesen Punkt mit den Koordinaten, an welchen der Spieler starten soll
	 * @param levelSettings definiert Eigenschaften, die für das gesamte Level gelten
	 * @return gibt das Level zurück, welches aus den Levelsegmenten zusammengesetzt wurde
	 */
	public static Level createLevel(List<LevelSegment> levelParts, Game g, Point playerStart, String[] levelSettings) {
		int width = 0;
		int height = 0;

		for (LevelSegment level : levelParts) {
			width += level.getWidth();
			height = Math.max(height, level.getHeight());
		}

		Level newLevel = new Level(width, height);

		for (LevelSegment level : levelParts) {
			newLevel.add(level);
		}

		newLevel.createMap();
		
		// insert all buffered level elements at their translated location
		newLevel.insertEnemies(g);
		newLevel.insertFallingItems(g);
		newLevel.insertShotCollections(g);
		newLevel.insertPlatforms(g);
		newLevel.insertJumpBlocks(g);
		
		newLevel.subscribeObservers(g);

		if (levelParts.get(0).getPlayerStart() != null) {
			// levelPart's playerStart is relative to its own dimensions.
			// So it must be converted to the global dimension which means that there might be an upper offset.
			Point relativeStart = levelParts.get(0).getPlayerStart();
			playerStart.x = relativeStart.x;
			playerStart.y = relativeStart.y + height - levelParts.get(0).getHeight();
		} else {
			playerStart.setLocation(DEFAULT_PLAYER_START);
		}

		// use final sgment's goal as level goal
		LevelSegment finalSegment = newLevel.levelParts.get(levelParts.size() - 1);
		Point goal = finalSegment.getGoal();
		if (goal != null && goal.x > -1) {
			newLevel.setSegmentMap(finalSegment, goal.x, goal.y, Blocks.GOAL);
		}

		return newLevel;
	}
	
	public Point getGoal() {
		return goal;
	}

	public void setCoin(int x, int y) {
		byte val = getMap(x, y - 1);
		if (val == Blocks.Empty) {
			setMap(x, y, Blocks.COIN);
		}
	}
}
