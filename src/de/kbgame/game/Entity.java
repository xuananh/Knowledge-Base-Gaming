package de.kbgame.game;

import java.awt.Color;

import de.kbgame.util.Physic;

public class Entity {

	public Controller con = null;
	public int x, y, width, height;
	public int lx, rx, uy, dy;
	public double vx, vy;
	public boolean onground = false;
	public Event event = null;

	public Entity(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		lx = x - this.width / 2;
		rx = lx + this.width - 1; // != x+wi/2
		uy = y - this.height / 2;
		dy = uy + this.height - 1;

		vx = 0;
		vy = 0;
	}
	
	public de.kbgame.geometry.Rectangle getSurroundingRectangle() {
		return new de.kbgame.geometry.Rectangle(lx, uy, width, height);
	}

	public void updatePos() {
		lx = x - width / 2;
		rx = lx + width - 1; // != x+wi/2
		uy = y - height / 2;
		dy = uy + height - 1;
	}
	
	public void jump(Game g) {
		vy -= Physic.JUMP_VELOCITY;
	}

	public void update(Game g) {
	}

	public void draw(Game g) {
		g.graphic.drawOval(x - width / 2, y - height / 2, width, height, new Color(200, 100, 100));
	}

	public void drawBox(Game g) {
		g.graphic.drawRectangleBorder(x - width / 2, y - height / 2, width, height, Color.cyan);
	}
	
	public void setStartPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void kill(Game g) {
		g.removeFromList.add(this);
	}
}
