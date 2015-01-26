package de.kbgame.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import de.kbgame.game.Enemy;
import de.kbgame.game.Game;
import de.kbgame.util.ColorValues;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.Clingo;
import de.kbgame.util.clingo.PredicateASP;

public final class MapLoader {

	private MapLoader() {
		throw new AssertionError();
	}

	public static Level LoadMapFromClingo(Game g, String filename) {
		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = filename;
		params[2] = "1";

		String res = Clingo.callClingo(params);

		AnswerASP answer = AnswerASP.getAnswerASPfromRes(res);
		final List<PredicateASP> pres = answer.getPreListFromString("block");
		final int width = (int) answer.getPreListFromString("width").get(0).getParameterOfIndex(0);
		final int height = (int) answer.getPreListFromString("height").get(0).getParameterOfIndex(0);
		
		final Level level = new Level(width,height);
		for(PredicateASP pre : pres) {
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
		switch(pixelColor){
			case ColorValues.r0g0b0: { 
				level.setMap(x,y,Blocks.Solid);
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
				Enemy e = new Enemy((x+1)*Level.BLOCK_WIDTH-50-1, (y+1)*Level.BLOCK_HEIGHT-50-1, 50, 50);
				g.list.add(e);
				break;
			}
			default :
				level.setMap(x,y,Blocks.Empty);
				break;
		}
	}
	
	private static void setMapFromPredicate(Game g, Level level, PredicateASP pre) {
		final int blockType = (int) pre.getParameterOfIndex(2);
		final int x = (int) pre.getParameterOfIndex(0);
		final int y = (int) pre.getParameterOfIndex(1);
		int color = ColorValues.r255g255b255;
		if(blockType == 1) {
			color = ColorValues.r128g128b128;
		}
		setPixel(g, level, color, x, y);
	}
}
