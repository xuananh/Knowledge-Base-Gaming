package de.kbgame.map;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.util.ColorValues;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;

public final class MapLoader {

	private MapLoader() {
		throw new AssertionError();
	}

	public static Level LoadMapFromClingo(Game g, String filename, Point playerStart, Point goalPoint) {
		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = filename;
		params[2] = "1";

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
		
		final Level level = new Level(width, height - 1);
		for (PredicateASP pre : pres) {
			setMapFromPredicate(g, level, pre);
		}
		return level;
	}

	public static Level LoadMapOutOfBitmap(Game g, String filename) {
		try {
			final BufferedImage bi = ImageIO.read(new File(filename));
			final Level level = new Level(bi.getWidth(), bi.getHeight());
			int pixelColor;

			for (int x = 0; x < bi.getWidth(); x++) {
				for (int y = 0; y < bi.getHeight(); y++) {
					pixelColor = bi.getRGB(x, y);
					setPixel(g, level, pixelColor, x, y);
				}
			}

			return level;
		} catch (IOException e) {
			System.err.println("Error: Cannot Read Image '" + filename + "'");
			return null;
		}
	}

	private static void setPixel(Game g, Level level, int pixelColor, int x, int y) {
		switch (pixelColor) {
			case ColorValues.r0g0b0: {
				level.setMap(x, y, Blocks.Solid);
				break;
			}
			case ColorValues.r128g128b128: {
				level.setMap(x, y, Blocks.Boden);
				break;
			}
			case ColorValues.r0g0b255: {
				level.setMap(x, y, Blocks.QuestionBlock);
				break;
			}
			case ColorValues.r255g0b0: {
				Enemy e = new Enemy((x + 1) * Level.BLOCK_WIDTH - 50 - 1, (y + 1) * Level.BLOCK_HEIGHT - 50 - 1, 50, 50);
				g.list.add(e);
				break;
			}
			default:
				level.setMap(x, y, Blocks.Empty);
				break;
		}
	}

	private static void setMapFromPredicate(Game g, Level level, PredicateASP pre) {
		final int blockType = (Integer) pre.getParameterOfIndex(2);
		final int x = (Integer) pre.getParameterOfIndex(0);
		final int y = (Integer) pre.getParameterOfIndex(1);
		switch (blockType) {
			case 1:
				level.setMap(x, y, Blocks.Boden);
				break;
			case 2:
				level.setMap(x, y, Blocks.QuestionBlock);
				break;
			case 10:

				break;
			case 11:

				break;
			default:
				level.setMap(x, y, Blocks.Empty);
				break;
		}
	}
}
