package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.kbgame.geometry.ImageKey;

public class ImageLoader {
	
	private static ImageLoader instance = new ImageLoader();

	private static final String BLOCK_IMAGE = "Images/block.png";
	private static final String BODEN_IMAGE = "Images/grass.png";
	private static final String QUESTIONBLOCK_IMAGE = "Images/questionblock.png";
	private static final String QUESTIONBLOCK_BOUNCED_IMAGE = "Images/questionblock_bounced.png";
	private static final String JUMP_BLOCK = "Images/jump_block.png";
	private static final String ENEMY_IMAGE = "Images/enemy.png";
	private static final String BACKGROUND_1 = "Images/bg-1.jpg";
	private static final String BACKGROUND_2 = "Images/bg-2.png";
	private static final String HUD_HEART = "Images/heart.png";
	private static final String GOAL = "Images/goal.png";
	private final HashMap<ImageKey, BufferedImage> images = new HashMap<ImageKey, BufferedImage>();
	
	private ImageLoader() {
		loadImages();
	}
	
	public void loadImages(){
		try {
			images.put(ImageKey.BLOCK, ImageIO.read(new File(BLOCK_IMAGE)));
			images.put(ImageKey.BODEN, ImageIO.read(new File(BODEN_IMAGE)));
			images.put(ImageKey.QUESTION_BLOCK, ImageIO.read(new File(QUESTIONBLOCK_IMAGE)));
			images.put(ImageKey.JUMP_BLOCK, ImageIO.read(new File(JUMP_BLOCK)));
			images.put(ImageKey.ENEMY, ImageIO.read(new File(ENEMY_IMAGE)));
			images.put(ImageKey.QUESTIONBLOCK_BOUNCED_IMAGE, ImageIO.read(new File(QUESTIONBLOCK_BOUNCED_IMAGE)));
			images.put(ImageKey.BACKGROUND_1, ImageIO.read(new File(BACKGROUND_1)));
			images.put(ImageKey.BACKGROUND_2, ImageIO.read(new File(BACKGROUND_2)));
			images.put(ImageKey.HUD_HEART, ImageIO.read(new File(HUD_HEART)));
			images.put(ImageKey.GOAL, ImageIO.read(new File(GOAL)));
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImageByKey(ImageKey key) {
		return images.get(key);
	}
	
	public static ImageLoader getInstance() {
		return ImageLoader.instance;
	}
}
