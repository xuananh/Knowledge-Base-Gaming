package de.kbgame.map;

import de.kbgame.game.Game;
import de.kbgame.util.ImageKey;

public class Level {
	
	public int width, height;
	private final int[][] map;
	
	public final int blockwidth = 25, blockheight = 25;
	
	public Level(int wi, int hi){
		if (wi<10) wi=10;
		if (hi<10) hi=10;
		width = wi;
		height = hi;
		map = new int[width][height];
	}
	
	public void update(Game g){
	}
	
	public void draw(Game g){
		int centerX = g.graphic.viewX;
		int centerY = g.graphic.viewY;
		int left = (int)(centerX - g.graphic.Width/2) / blockwidth -1;
		int right = (int)(centerX + g.graphic.Width/2) / blockwidth +1;
		int top = (int)(centerY - g.graphic.Height/2 -0.9) / blockheight -1;
		int bottom = (int)(centerY + g.graphic.Height/2 +0.9) / blockheight +1;
		
		for (int x=left; x<right; x++){
			for (int y=top; y<bottom; y++){
				int val = getMap(x, y);
				switch(val){
					case Blocks.Solid:{
						g.graphic.drawImage(ImageKey.BLOCK, x*blockwidth+blockwidth/2, y*blockheight+blockheight/2, blockheight, blockheight, 0f);
						break;
					}
				}
			}
		}
	}
	
	public int getMap(int x, int y){
		if (x >= 0 && y >= 0 && x < width && y < height){
			return map[x][y];
		}else return 2;
	}
	
	public void setMap(int x, int y, int v){
		if (x >= 0 && y >= 0 && x < width && y < height){
			map[x][y] = v;
		}
	}
	
}
