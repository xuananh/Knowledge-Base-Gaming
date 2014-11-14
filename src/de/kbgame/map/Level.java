package de.kbgame.map;

import java.awt.Color;

import de.kbgame.game.Game;

public class Level {
	
	public int width, height;
	public int blockwidth = 25, blockheight = 25;
	public byte[][] map;
	
	public Level(int wi, int hi){
		if (wi<10) wi=10;
		if (wi<10) wi=10;
		width = wi;
		height = hi;
		map = new byte[width][height];
	}
	
	public void testLevel(int x, int y){
		if (x<10) x=10;
		if (y<10) y=10;
		width = x;
		height = y;
		map = new byte[width][height];
		for (int i=0;i<width;i++) map[i][height-1] = 1; // solid ground
		map[(int)(width/2)][height-2] = 1;
		map[(int)(width/2)+2][height-2] = 1;
		map[(int)(width/2)+5][height-2] = 1;
		map[(int)(width/2)-2][height-3] = 1;
		map[(int)(width/2)-4][height-2] = 1;
		map[(int)(width/2)-3][height-3] = 1;
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
				String image = "";
				byte val = getMap(x, y);
				switch(val){
				case Blocks.Solid:{image="block";break;}
				}
				if (image != null) g.graphic.drawImage(image, x*blockwidth+blockwidth/2, y*blockheight+blockheight/2, blockheight, blockheight, 0f);
			}
			if (g.drawEntityBorders) g.graphic.drawLine(x*blockwidth,0,x*blockwidth,height*blockheight, Color.red);
		}

		if (g.drawEntityBorders) for (int y=0; y<height; y++){
			g.graphic.drawLine(0,y*blockheight,width*blockwidth,y*blockheight, Color.red);
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
	
}
