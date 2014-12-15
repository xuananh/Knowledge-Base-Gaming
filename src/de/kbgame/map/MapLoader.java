package de.kbgame.map;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.kbgame.game.Game;
import de.kbgame.util.ColorValues;

public final class MapLoader {
 
	private MapLoader() {
        throw new AssertionError();
    }

	public static Level LoadMapOutOfBitmap(Game g, String filename){
		try {
			final BufferedImage bi = ImageIO.read(new File(filename));
			final Level level = new Level(bi.getWidth(), bi.getHeight());
			int pixelColor;
			
			for (int x=0;x<bi.getWidth();x++){
				for (int y=0;y<bi.getHeight();y++){
					pixelColor = bi.getRGB(x, y);
					setPixel(g,level, pixelColor, x, y);
				}
			}
			
			return level;
        } catch (IOException e) {
        	System.err.println("Error: Cannot Read Image '"+filename+"'");
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
//				Enemy e = new Enemy((x+1)*Level.BLOCK_WIDTH-50-1, (y+1)*Level.BLOCK_HEIGHT-50-1, 50, 50);
//				g.list.add(e);
				break;
			}
			default :
				level.setMap(x,y,Blocks.Empty);
				break;
		}
	}
}
