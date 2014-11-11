package de.kbgame.game;

import java.awt.Color;

public class Rectangle extends Entity {
	public Controller con = null;
	
	public Rectangle(int x, int y, int w, int h){
		super(x, y, w, w);
	}
	
	public void update(Game g){
		if (con == null){
			x = (x+1) % g.graphic.Width; // Modulo: 0 < x < g.graphic.Width
			y = (y+1) % g.graphic.Height; 
		}
	}
	
	public void draw(Game g){
		g.graphic.drawRectangle(x-wi/2, y-hi/2, wi, hi, new Color(100,100,100));
	}
	
}
