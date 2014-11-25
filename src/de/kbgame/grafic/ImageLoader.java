package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;

import de.kbgame.geometry.ImageKey;

public class ImageLoader {

	private final HashMap<ImageKey, BufferedImage> images = new HashMap<ImageKey, BufferedImage>();
	private final List<BufferedImage> sprites = new ArrayList<BufferedImage>();
	
	public ImageLoader() {
		loadImages();
		spriteImage(getImageByKey(ImageKey.SPRITE), 4, 3);
	}
	
	public void loadImages(){
		try {
			images.put(ImageKey.BACKGROUND,ImageIO.read(new File("Images/background.png")));
			images.put(ImageKey.PERSON,ImageIO.read(new File("Images/person.png")));
			images.put(ImageKey.BLOCK, ImageIO.read(new File("Images/block.png")));
			images.put(ImageKey.BODEN, ImageIO.read(new File("Images/grass.png")));
			images.put(ImageKey.QUESTION_BLOCK, ImageIO.read(new File("Images/questionblock.png")));
			images.put(ImageKey.SPRITE, ImageIO.read(new File("Images/sprite.png")));
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImageByKey(ImageKey key) {
		return images.get(key);
	}
	
	public BufferedImage getSprite(int spritePosition) {
		return sprites.get(spritePosition);
	}
	
	private void spriteImage(BufferedImage image, int rows, int cols) {
		final int width = (image.getWidth() / cols);
		final int height = (image.getHeight() / rows);

		for (int i = 0; i < rows; i++)
		{
		    for (int j = 0; j < cols; j++)
		    {
		        sprites.add(image.getSubimage(j * width,i * height,width,height));
		    }
		}
	}
}
