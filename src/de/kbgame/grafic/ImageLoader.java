package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.kbgame.geometry.ImageKey;

public class ImageLoader {

	private static final String BACKGROUND_IMAGE = "Images/background.png";
	private static final String PERSON_IMAGE = "Images/person.png";
	private static final String BLOCK_IMAGE = "Images/block.png";
	private static final String BODEN_IMAGE = "Images/grass.png";
	private static final String QUESTIONBLOCK_IMAGE = "Images/questionblock.png";
	private static final String ENEMY_IMAGE = "Images/enemy.png";
	private final HashMap<ImageKey, BufferedImage> images = new HashMap<ImageKey, BufferedImage>();
	
	public ImageLoader() {
		loadImages();
	}
	
	public void loadImages(){
		try {
			images.put(ImageKey.BACKGROUND,ImageIO.read(new File(BACKGROUND_IMAGE)));
			images.put(ImageKey.PERSON,ImageIO.read(new File(PERSON_IMAGE)));
			images.put(ImageKey.BLOCK, ImageIO.read(new File(BLOCK_IMAGE)));
			images.put(ImageKey.BODEN, ImageIO.read(new File(BODEN_IMAGE)));
			images.put(ImageKey.QUESTION_BLOCK, ImageIO.read(new File(QUESTIONBLOCK_IMAGE)));
			images.put(ImageKey.ENEMY, ImageIO.read(new File(ENEMY_IMAGE)));
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImageByKey(ImageKey key) {
		return images.get(key);
	}
}
