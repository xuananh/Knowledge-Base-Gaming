package de.kbgame.game;

import java.awt.Color;

public class Circle extends Entity{
	
	public Circle(int x, int y, int w, int h){
		super(x, y, w, h);
	}
	
	public void update(Game g){
		if (con == null){
			x = (x+1) % g.graphic.Width; // Modulo: 0 < x < g.graphic.Width
			y = (y+1) % g.graphic.Height; 
		}
	}
	
	public void draw(Game g){
		g.graphic.drawOval(x-width/2, y-height/2, width, height, new Color(200,100,100));
	}
	
}
