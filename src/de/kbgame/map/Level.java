package de.kbgame.map;

import de.kbgame.game.Game;
import de.kbgame.util.ImageKey;

public class Level {

	public int width, height;
	public byte[][] map;

	private int left, right, top, bottom;

	public final int blockwidth = 25, blockheight = 25;

	public Level(int wi, int hi) {
		width = Math.max(10, wi);
		height = Math.max(10, hi);
		map = new byte[width][height];
	}

	public void update(Game g) {
		int centerX = g.graphic.viewX;
		int centerY = g.graphic.viewY;

		left = (int) (centerX - g.graphic.Width / 2) / blockwidth - 1;
		right = (int) (centerX + g.graphic.Width / 2) / blockwidth + 1;
		top = (int) (centerY - g.graphic.Height / 2 - 0.9) / blockheight - 1;
		bottom = (int) (centerY + g.graphic.Height / 2 + 0.9) / blockheight + 1;
	}

	public void draw(Game g) {
		for (int x = left; x < right; x++) {
			for (int y = top; y < bottom; y++) {
				byte val = getMap(x, y);
				switch (val) {
				case Blocks.Solid: {
					g.graphic.drawImage(ImageKey.BLOCK, x * blockwidth
							+ blockwidth / 2,
							y * blockheight + blockheight / 2, blockheight,
							blockheight, 0f);
					break;
				}
				}
			}
		}
	}
	
	public byte getMap(int x, int y){
		if (x >= 0 && y >= 0 && x < width && y < height){
			return map[x][y];
		}else return 1;
	}
	
	public void setMap(int x, int y, byte v){
		if (x >= 0 && y >= 0 && x < width && y < height){
			map[x][y] = v;
		}
	}

	public boolean inViewport(int x, int y) {
		return x >= left && x <= right && y >= top && y <= bottom;
	}
}
