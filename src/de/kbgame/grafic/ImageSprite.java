package de.kbgame.grafic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageSprite {
	private final List<BufferedImage> sprites = new ArrayList<BufferedImage>();
	private final int rows;
	private final int cols;
	private int index = 0;
	private int currentRow = 0;
	
	public boolean noUpdate = false;

	public ImageSprite(String imagePath, int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		setImagePathToSprite(imagePath);
	}

	private void setImagePathToSprite(String imagePath) {
		try {
			final BufferedImage image = ImageIO.read(new File(imagePath));
			final int width = image.getWidth() / cols;
			final int height = image.getHeight() / rows;

			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					sprites.add(image.getSubimage(j * width, i * height, width, height));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;

		if (this.index >= sprites.size()) {
			this.index = 0;
		}
	}

	public BufferedImage getCurrent() {
		return sprites.get(index);
	}

	public void setCurrentRow(int row) {
		if (row >= 0 && row < rows && row != currentRow) {
			currentRow = row;
			index = currentRow * cols;
		}
	}

	public void next() {
		index = currentRow * cols + (index + 1) % cols;
	}
	
	public void update() {
		if (!noUpdate) {
			next();
		}
	}
}
