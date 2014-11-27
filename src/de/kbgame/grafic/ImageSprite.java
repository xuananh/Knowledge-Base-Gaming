package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageSprite {

	private final List<BufferedImage> sprites = new ArrayList<BufferedImage>();
	private final String imagePath;
	private final int rows;
	private final int cols;
	private int index = 0;

	public ImageSprite(String imagePath, int rows, int cols) {
		this.imagePath = imagePath;
		this.rows = rows;
		this.cols = cols;
		setImagePathToSprite();
	}

	private List<BufferedImage> setImagePathToSprite() {
		try {
			final BufferedImage image = ImageIO.read(new File(imagePath));
			final int width = (image.getWidth() / cols);
			final int height = (image.getHeight() / rows);

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					sprites.add(image.getSubimage(j * width, i * height, width,
							height));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sprites;
	}

	public BufferedImage getSprite() {
		return sprites.get(index);
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
		checkIndex();
	}
	
	public void setIndexInkrement(){
		this.index ++;
		checkIndex();
	}
	
	private void checkIndex() {
		if (this.index >= sprites.size()) {
			this.index = 0;
		}
	}
}
