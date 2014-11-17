package de.kbgame.util;

import de.kbgame.game.Entity;
import de.kbgame.game.Game;



public class Physic {

	public static void doPhysic(Game g, Entity e){
		//TODO: Different Physic Types
		FallPhysic(g, e);
	}
	
	public static void FallPhysic(Game g, Entity e){
		
		int blockheight = g.level.blockheight;
		int blockwidth = g.level.blockwidth;

		double ovx = e.vx, nvx = 0;
		double ovy = e.vy, nvy = 0;
		while (ovx != 0 || ovy != 0){
			double vx = ovx, vy = ovy;
			if (ovx > blockwidth || ovy > blockheight){ // important if Velocity > 1 Block
				if (Math.abs(ovx) > Math.abs(ovy)){ // Partitionate in Vectors of the same direction with each vx,vy less/equal than block
					vx = Math.signum(ovx)*blockwidth;
					vy = vx/ovx*ovy;
				}else{
					vy = Math.signum(ovy)*blockwidth;
					vx = vy/ovy*ovx;
				}
			}
			ovx -= vx; // Remove the currently calculated move from the "movepool"
			ovy -= vy;
			
			//if (Math.abs(vx) > blockwidth) vx = blockwidth*Math.signum(vx); // Max Speed = 1 Block per Cycle
			//if (Math.abs(vy) > blockheight) vy = blockheight*Math.signum(vy); // Max Speed = 1 Block per Cycle
			
			int x = e.x / blockwidth; // Aktuelle Position
			int y = e.y / blockheight;
			
			double hlp;
			if (vx >= 0){
				hlp = e.rx + vx;
			}else{
				hlp = e.lx + vx;
			}
			int nx = (int) (hlp/ blockwidth); // Aktuelle Position
			if (hlp < 0) nx -= 1;// Sonst gibt es zwischen 0 und -1  einen Rundungsfehler... ( (int)1.6 == 1; (int)0.6 == 0; (int)-0.6 == 0 statt -1). Daher -1
//			System.out.println("nx "+nx+", "+hlp);
			
			if (vy >= 0){
				hlp = e.dy + vy;
			}else{
				hlp = e.uy + vy;
			}
			int ny = (int) (hlp / blockheight);
			if (hlp < 0) ny -= 1;
//			System.out.println("ny "+ny+", "+hlp);
			
			// LEFT <- -> RIGHT PHYSIC
			for (int loopy=e.uy; loopy<e.dy; loopy += blockheight){
				int my = loopy / blockheight;
				if (loopy < 0) my -= 1;
				if (nx != x && g.level.getMap(nx,my) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of e.x directly...
					if (vx > 0) e.x += blockwidth-1-((e.rx) % blockwidth);
					else e.x -= (e.lx) % blockwidth;
					vx = 0;
					nx = x;
					break;
				}
			}
			if (vx != 0){
				int loopy = e.dy;
				int my = loopy / blockheight;
				if (loopy < 0) my -= 1;
				if (nx != x && g.level.getMap(nx,my) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of e.x directly...
					if (vx > 0) e.x += blockwidth-1-((e.rx) % blockwidth);
					else e.x -= (e.lx) % blockwidth;
					vx = 0;
					nx = x;
				}
				e.x += vx;
			}
			e.updatePos();
			

			// UP <- -> DOWN PHYSIC
			for (int loopx=e.lx; loopx<e.rx; loopx += blockwidth){
				int mx = loopx / blockwidth;
				if (loopx < 0) mx -= 1;
//				System.out.println("loopx "+loopx+", mx "+mx+", ny "+ny+", y "+y);
				if (ny != y && g.level.getMap(mx,ny) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of e.x directly...
					if (vy > 0){
						e.y += blockheight-1-((e.dy) % blockheight);
						e.onground = true;
					}else e.y -= (e.uy) % blockheight;
					vy = 0;
					break;
				}
			}
			if (vy != 0){
				int loopx=e.rx;int mx = loopx / blockwidth;
				if (loopx < 0) mx -= 1;
//				System.out.println("loopx "+loopx+", mx "+mx+", ny "+ny+", y "+y);
				if (ny != y && g.level.getMap(mx,ny) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of e.x directly...
					if (vy > 0){
						e.y += blockheight-1-((e.dy) % blockheight);
						e.onground = true;
					}else e.y -= (e.uy) % blockheight;
					vy = 0;
				}
				e.y += vy;
			}
			e.updatePos();
			
			if (vy != 0) e.onground = false;
			
			nvx += vx;
			nvy += vy;
		}
		e.vx = nvx;
		e.vy = nvy;
		if (Math.abs(e.vx) > 0.3){
			e.vx *= 0.8;
		} else e.vx = 0;
		//if (!e.onground){
		e.vy += 0.1;
		//}
	}
	
}
