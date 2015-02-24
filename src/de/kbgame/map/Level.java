package de.kbgame.map;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.game.level.LevelSegment;
import de.kbgame.geometry.ImageKey;

public class Level {

	public int width, height;
	private final byte[][] map;
	private final ArrayList<LevelSegment> levelParts = new ArrayList<LevelSegment>();

	private int left, right, top, bottom;

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
					case Blocks.JUMP: {
						g.graphic.drawImage(ImageKey.JUMP_BLOCK, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
						break;
					}
					case Blocks.QUESTION_BLOCK_BOUNCED:
						g.graphic.drawImage(ImageKey.QUESTIONBLOCK_BOUNCED_IMAGE, x * BLOCK_WIDTH + BLOCK_WIDTH / 2, y * BLOCK_HEIGHT + BLOCK_HEIGHT / 2, BLOCK_HEIGHT, BLOCK_HEIGHT, 0f);
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

	public static Level createLevel(List<LevelSegment> levelParts, Game g, Point playerStart) {
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

	/*
	 * Creates an ArrayList of Levels. One level is defined per line in the config file. Each level contains of 
	 * one or more level segments, split by a semicolon.
	 */
	public static ArrayList<Level> createByConfig(File config, Game g, Point playerStart) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(config));
		ArrayList<Level> levels = new ArrayList<Level>();
		String line;
		LevelSegment segment;
		ArrayList<LevelSegment> levelSegments = new ArrayList<LevelSegment>();

		while ((line = reader.readLine()) != null) {
			// split in single level segments
			String[] levelSegmentsString = line.trim().replaceAll("(\\s)", "").split(";");
			levelSegments.clear();

			// process each level segment
			for (int i = 0; i < levelSegmentsString.length; i++) {
				// try to create a level segment
				segment = parseSegment(levelSegmentsString[i], g);
				if (segment != null) {
					// if a level segment could be created by its string representation, it's added to the level list
					levelSegments.add(segment);
				}
			}

			if (levelSegments.size() > 0) {
				levels.add(createLevel(levelSegments, g, playerStart));
			}
		}

		return levels;
	}

	private static LevelSegment parseSegment(String segmentString, Game g) {
		Class<?> levelSegmentCl;

		Pattern pattern = Pattern.compile(CONFIG_REG_EX);
		// split the string into segment class name and arguments
		Matcher matcher = pattern.matcher(segmentString);
		matcher.find();

		// 1st group holds the segment class name
		String segmentClass = matcher.group(1);
		// 2nd group contains a comma separated argument list
		String[] constructorArgs = matcher.group(2).split(",");

		LevelSegment level = null;

		try {
			// look up the level segment class name in the associated package
			levelSegmentCl = Class.forName(LEVEL_SEGMENT_PACKAGE + "." + segmentClass);
			// search for a constructor that accepts exact 2 arguments of the types Game and String[]
			Constructor<?> constr = levelSegmentCl.getConstructor(Game.class, String[].class);

			if (levelSegmentCl.getSuperclass() == LevelSegment.class) {
				// if a such a constructor exists it's invoked with the specific parameters
				level = (LevelSegment) constr.newInstance(g, constructorArgs);
			}
		} catch (Exception e) {
			// ignore unknown classes
			// ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException,
			// IllegalAccessException, IllegalArgumentException, InvocationTargetException
			System.err.println(segmentClass + " couldn't be instantiated");
		}

		return level;
	}
}
