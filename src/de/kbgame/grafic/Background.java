package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.kbgame.game.Game;
import de.kbgame.geometry.ImageKey;

public class Background {

	public float xFactor = 1;
	public float yFactor = 0;
	public BufferedImage background = null;
	private int width;
	private int height;
	private float targetWidth;
	
	public Background(ImageKey img, float xFactor, float yFactor, Game g) {
		ImageLoader imageLoader = new ImageLoader();
		
		background = imageLoader.getImageByKey(img);
		
		float scale = background.getHeight() / (float) g.graphic.currentWindowSize.height;
		
		width = (int) (background.getWidth() / scale);
		height = g.graphic.currentWindowSize.height;
		
		targetWidth = width / xFactor;
			
		this.xFactor = xFactor;
		this.yFactor = yFactor;
	}
	
	public Background(ImageKey img) {
		try {
			background = ImageIO.read(new File("Images/bg-1.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Game g) {
		if (background != null) {			
			int start = (int) (g.graphic.viewX / targetWidth);
			// TODO
//			int end = (int) ((g.graphic.viewX + g.graphic.currentWindowSize.width + xFactor * this.width) / this.targetWidth);
			
			for (int i = start; i <= start + 1; i++) {
				g.graphic.currentGrafic.drawImage(background, (int) (i * this.width + g.graphic.viewX * -xFactor), 0, width, height, null);
			}
		}
	}
	
}
