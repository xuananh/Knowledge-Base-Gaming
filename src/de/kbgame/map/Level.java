package de.kbgame.map;

import java.awt.Color;

import de.kbgame.game.Game;

public class Level {
	
	public int width, height;
	public int blockwidth = 50, blockheight = 50;
	public int[][] map;
	
	public Level(int x, int y){
		if (x<10) x=10;
		if (y<10) y=10;
		width = x;
		height = y;
		map = new int[width][height];
		for (int i=0;i<width;i++) map[i][height-1] = 1; // solid ground
		map[(int)(width/2)][height-2] = 1;
		map[(int)(width/2)+2][height-2] = 1;
		map[(int)(width/2)-2][height-3] = 1;
		map[(int)(width/2)-4][height-2] = 1;
		map[(int)(width/2)-3][height-3] = 1;
	}
	
	public void update(Game g){
	}
	
	public void draw(Game g){
		for (int x=0; x<width; x++){
			for (int y=0; y<height; y++){
				if (map[x][y] == 1){
					g.graphic.drawImage(2, x*blockwidth+blockwidth/2, y*blockheight+blockheight/2, blockheight, blockheight, 0f);
				}
			}
			if (g.drawEntityBorders) g.graphic.drawLine(x*blockwidth,0,x*blockwidth,height*blockheight, Color.red);
		}

		if (g.drawEntityBorders) for (int y=0; y<height; y++){
			g.graphic.drawLine(0,y*blockheight,width*blockwidth,y*blockheight, Color.red);
		}
	}
	
	public int getMap(int x, int y){
		if (x >= 0 && y >= 0 && x < width && y < height){
			return map[x][y];
		}else return 1;
	}
	
}
