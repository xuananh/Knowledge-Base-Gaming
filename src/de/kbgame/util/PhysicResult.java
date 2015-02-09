package de.kbgame.util;

import de.kbgame.game.Entity;

public class PhysicResult {
	public double x, y, vx, vy;
	public double lx, rx, uy, dy;
	public double wi, hi;
	public boolean onground = false, top = false, left = false, right = false;
	public boolean turn = false;

	public PhysicResult(double nx, double ny, double nvx, double nvy, boolean gr, boolean to, boolean le, boolean ri, double nlx, double nrx, double nuy, double ndy, int nwi, int nhi) {
		x = nx;
		y = ny;
		vx = nvx;
		vy = nvy;
		onground = gr;
		top = to;
		left = le;
		right = ri;

		lx = nlx;
		rx = nrx;
		uy = nuy;
		dy = ndy;
		wi = nwi;
		hi = nhi;
	}
	
	public void updatePos(){
		lx =  (x-wi/2);
		rx =  (lx+wi-1); // != x+wi/2
		uy =  (y-hi/2);
		dy =  (uy+hi-1);
	}

	public void apply(Entity e) {
		e.x = (int) x;
		e.y = (int) y;
		e.vx = vx;
		e.vy = vy;
		e.lx = (int) lx;
		e.rx = (int) rx;
		e.uy = (int) uy;
		e.dy = (int) dy;
		e.onground = onground;
		e.updatePos();
	}
	
	public void setPhysicResult(Entity e) {
		x = e.x;
		y = e.y;
		vx = e.vx;
		vy = e.vy;
		onground = e.onground;
		
		lx = e.lx;
		rx = e.rx;
		uy = e.uy;
		dy = e.dy;
		wi = e.width;
		hi = e.height;
	}
}
