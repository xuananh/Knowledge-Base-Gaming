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
		Level level = null;
		BufferedImage bi;
		try {
			bi = ImageIO.read(new File(filename));
        } catch (IOException e) {
        	System.err.println("Error: Cannot Read Image '"+filename+"'");
        	return null;
        }
		if (bi == null) return null;
		level = new Level(bi.getWidth(), bi.getHeight());
		for (int x=0;x<bi.getWidth();x++){
			for (int y=0;y<bi.getHeight();y++){
				int pixelColor = bi.getRGB(x, y);
				Byte block = Blocks.Empty;
				switch(pixelColor){
					case ColorValues.r0g0b0: { 
						block = Blocks.Solid;
						break;
					} 
				}
				level.setMap(x,y,block);
			}
		}
		return level;
	}
	
}
