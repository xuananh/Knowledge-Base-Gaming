package de.kbgame.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.game.level.LevelSegment;
import de.kbgame.geometry.ImageKey;

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
						break;
					}
					case Blocks.FireBlock: {
						g.graphic.drawImage(ImageKey.FIRE_BLOCK, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
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

	public boolean inViewport(int x, int y) {
		return x >= left && x <= right && y >= top && y <= bottom;
	}

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
		newLevel.insertEnemies(g);

		if (levelParts.get(0).getPlayerStart() != null) {
			// levelPart's playerStart is relative to its own dimensions.
			// So it must be converted to the global dimension which means that there might be an upper offset.
			Point relativeStart = levelParts.get(0).getPlayerStart();
			playerStart.x = relativeStart.x;
			playerStart.y = relativeStart.y + height - levelParts.get(0).getHeight();
		} else {
			playerStart.setLocation(DEFAULT_PLAYER_START);
		}

		return newLevel;
	}
	
	public Point getGoal() {
		return goal;
	}
}
