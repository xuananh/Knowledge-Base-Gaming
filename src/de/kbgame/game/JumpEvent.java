package de.kbgame.game;

import de.kbgame.geometry.MyPoint;

public class JumpEvent extends Event {
	
	double dvy, vy, y, lastY, dx; 
	int steps;
	MyPoint point;

	JumpEvent(Entity e, double dvy, double vy, int steps, MyPoint point) {
		super(e);
		
		this.dvy = dvy;
		this.vy = vy;
		this.steps = steps;
		this.point = point;
		this.lastY = e.y;
		this.y = e.y;
		this.dx = (point.x - e.x) / steps;
	}
	
	void update() {
		
		
		//System.out.println(owner.x + " " + owner.y + " " + lastY + " " + vy + " " + steps);
		
		if (steps <= 0) {
			owner.y = (int) point.y;
			owner.x = (int) point.x;
			System.out.println(owner.x + " " + owner.y + " - " + ((int) point.x) + " " + ((int) point.y));
			owner.event = null;
			owner.vx = dx;
			owner.vy = vy;
		}else{
			double delta = (lastY + (point.y - lastY) / (double)steps);
			y += vy - lastY + delta;
			owner.y = (int) y;
			owner.x += (point.x - owner.x)/(steps+1);//dx;
			lastY = delta;
			vy -= dvy;
		}
		steps--;
	}
	
}