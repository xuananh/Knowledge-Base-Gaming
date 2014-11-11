package de.kbgame.game;

import java.awt.Color;

public class Entity {

	public Controller con = null;
	public int x,y,wi,hi;
	public int lx,rx,uy,dy;
	public double vx,vy;
	public boolean onground = false;
	
	public Entity(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		wi = width;
		hi = height;
		
		lx = x-wi/2;
		rx = lx+wi-1; // != x+wi/2
		uy = y-hi/2;
		dy = uy+hi-1;
		
		vx=0;vy=0;
	}
	
	public void updatePos(){
		lx = x-wi/2;
		rx = lx+wi-1; // != x+wi/2
		uy = y-hi/2;
		dy = uy+hi-1;
	}
	
	public void update(Game g){
	}
	
	public void draw(Game g){
		g.graphic.drawOval(x-wi/2, y-hi/2, wi, hi, new Color(200,100,100));
	}
	
	public void drawBox(Game g){
		g.graphic.drawRectangleBorder(x-wi/2, y-hi/2, wi, hi, Color.cyan);
	}
}
