package de.kbgame.map;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.game.level.LevelSegment;
import de.kbgame.util.ColorValues;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;

public final class MapLoader {

	private MapLoader() {
		throw new AssertionError();
	}

	public static byte[][] loadFromClingo(Game g, File file, Point playerStart, Point goalPoint) throws FileNotFoundException {
		if (!file.exists()) {
			throw new FileNotFoundException();
		}

		Random r = new Random();
		int seed = r.nextInt(10000);
		
		String[] params = new String[4];
		params[0] = "clingo";
		params[1] = file.getAbsolutePath();
		params[2] = "--seed="+seed;
		params[3] = "--rand-freq=1";

		final AnswerASP answer = ClingoFactory.getInstance().getAnswerASP(params);
		final List<PredicateASP> pres = answer.getPreListFromString("block");
		final int width = (Integer) answer.getPreListFromString("width").get(0).getParameterOfIndex(0);
		final int height = (Integer) answer.getPreListFromString("height").get(0).getParameterOfIndex(0);
		final PredicateASP start = answer.getPreListFromString("startX").get(0);
		final PredicateASP goal = answer.getPreListFromString("goalX").get(0);

		playerStart.x = (Integer) start.getParameterOfIndex(0);
		playerStart.y = (Integer) start.getParameterOfIndex(1);

		goalPoint.x = (Integer) goal.getParameterOfIndex(0);
		goalPoint.y = (Integer) goal.getParameterOfIndex(1);

		byte[][] map = new byte[width][height + 1];
		for (PredicateASP pre : pres) {
			setMapFromPredicate(g, map, pre);
		}
		return map;
	}

	public static byte[][] loadFromBitmap(LevelSegment level, String filename, Point goal) {
		try {
			final BufferedImage image = ImageIO.read(new File(filename));
			final byte[][] map = new byte[image.getWidth()][image.getHeight()];
			int pixelColor;

			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					pixelColor = image.getRGB(x, y);
					setPixel(level, map, pixelColor, x, y, goal);
				}
			}

			return map;
		} catch (IOException e) {
			System.err.println("Error: Cannot Read Image '" + filename + "'");
			return null;
		}
	}

	private static void setPixel(LevelSegment level, byte[][] map, int pixelColor, int x, int y, Point goal) {
		switch (pixelColor) {
			case ColorValues.r0g0b0: {
				map[x][y] = Blocks.Solid;
				break;
			}
			case ColorValues.r128g128b128: {
				map[x][y] = Blocks.Floor;
				break;
			}
			case ColorValues.r0g0b255: {
				map[x][y] = Blocks.QuestionBlock;
				break;
			}
			case ColorValues.r255g0b0: {
				Enemy e = new Enemy(x * Level.BLOCK_WIDTH - 1, y * Level.BLOCK_HEIGHT - 1, Level.BLOCK_WIDTH, Level.BLOCK_HEIGHT);
				level.addEnemy(e);
				break;
			}
			case ColorValues.r255g255b0: {
				map[x][y] = Blocks.GOAL;
				goal.setLocation(x, y);
				break;
			}
			case ColorValues.r128g128b0: {
				map[x][y] = Blocks.COIN;
				break;
			}
			
			
			// Falling Objects - Gruen
			case ColorValues.r0g255b0: {
				level.addFallingItem(x*Level.BLOCK_WIDTH - 1, y*Level.BLOCK_HEIGHT - 1);
				//map[x][y] = Blocks.GOAL;
				//goal.setLocation(x, y);
				//break;
				break;
			}
			
			
			// Cannon - dunkeblau
			case ColorValues.r0g0b128: {
				ShotCollection sc = new ShotCollection(13, 2, new Point(x * Level.BLOCK_WIDTH - 1, y * Level.BLOCK_HEIGHT - 1));
//				System.out.println("Maploader: " + x + " " + y);
				level.addShotCollection(sc);
				map[x][y] = Blocks.CannonBlock;
			}
			
			
			// java initializes scalar vectors with 0s by default
		}
	}

	private static void setMapFromPredicate(Game g, byte[][] map, PredicateASP pre) {
		final int blockType = (Integer) pre.getParameterOfIndex(2);
		final int x = (Integer) pre.getParameterOfIndex(0);
		final int y = (Integer) pre.getParameterOfIndex(1);

		switch (blockType) {
			case 1:
				map[x][y] = Blocks.Floor;
				break;
			case 2:
				map[x][y] = Blocks.QuestionBlock;
				break;
			case 9:
				map[x][y] = Blocks.FireBlock;
				break;
		// java initializes scalar vectors with 0s by default
		}
	}
}
