package de.kbgame.game;

import de.kbgame.util.ImageKey;
import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;

public class Player extends Entity {
	
	private Platform parent = null;
	private int parentOffsetX, parentOffsetY;
	public float rot = 0;

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public void setParent(Platform platform) {
		parent = platform;
		
		if (platform != null) {
			parentOffsetX = this.x - platform.x;
			parentOffsetY = this.y - platform.y;
			
			this.vx = 0;
			this.vy = 0;
		}
	}
	
	@Override
	public void update(Game g) {
		int nx = 0, ny = 0;
		
		if (parent != null) {
			nx = parent.x + parentOffsetX;
			ny = parent.y + parentOffsetY;
			
			vy = parent.vy;
		} 

		PhysicResult result = Physic.doPhysic(g, this);
		
		if (parent != null && result.y < ny) {
			result.apply(this);
			setParent(null);
		} else if (parent != null) {
			x = nx;
			y = ny;
		} else {
			result.apply(this);
		}		
	}
	
	@Override
	public void draw(Game g) {
		g.graphic.drawImage(ImageKey.PERSON, x, y, wi, hi, rot);
	}
}
