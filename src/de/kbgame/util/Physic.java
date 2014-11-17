package de.kbgame.util;

import de.kbgame.game.Entity;
import de.kbgame.game.Game;

public final class Physic {
	
	public static double jumpvelocity = 14;
	public static double gravity = 0.11;
	public static double friction = 0.8;

	private Physic() {
        throw new AssertionError();
    }

	public static PhysicResult doPhysic(Game g, Entity e){
		//TODO: Different Physic Types
		return fallPhysic(g, e);
	}
	
	public static PhysicResult fallPhysic(Game g, Entity e){
		PhysicResult result = new PhysicResult(e.x,e.y,e.vx,e.vy,e.onground,false,false,false,e.lx,e.rx,e.uy,e.dy,e.wi,e.hi);
		
		int blockheight = g.level.blockheight;
		int blockwidth = g.level.blockwidth;

		double ovx = result.vx, nvx = 0;
		double ovy = result.vy, nvy = 0;
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
			
			int x = (int) (result.x / blockwidth); // Aktuelle Position
			int y = (int) (result.y / blockheight);
			
			double hlp;
			if (vx >= 0){
				hlp = result.rx + vx;
			}else{
				hlp = result.lx + vx;
			}
			int nx = (int) (hlp/ blockwidth); // Aktuelle Position
			if (hlp < 0) nx -= 1;// Sonst gibt es zwischen 0 und -1  einen Rundungsfehler... ( (int)1.6 == 1; (int)0.6 == 0; (int)-0.6 == 0 statt -1). Daher -1
			
			if (vy >= 0){
				hlp = result.dy + vy;
			}else{
				hlp = result.uy + vy;
			}
			int ny = (int) (hlp / blockheight);
			if (hlp < 0) ny -= 1;
			//System.out.println("ny "+ny+", "+hlp);
			
			// LEFT <- -> RIGHT PHYSIC
			for (int loopy=result.uy; loopy<result.dy; loopy += blockheight){
				int my = loopy / blockheight;
				if (loopy < 0) my -= 1;
				if (nx != x && g.level.getMap(nx,my) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vx > 0){
						result.x += blockwidth-1-((result.rx) % blockwidth);
						result.right = true;
					}else{
						result.x -= (result.lx) % blockwidth;
						result.left = true;
					}
					vx = 0;
					nx = x;
					break;
				}
			}
			if (vx != 0){
				int loopy = result.dy;
				int my = loopy / blockheight;
				if (loopy < 0) my -= 1;
				if (nx != x && g.level.getMap(nx,my) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vx > 0){ 
						result.x += blockwidth-1-((result.rx) % blockwidth);
						result.right = true;
					}else{
						result.x -= (result.lx) % blockwidth;
						result.left = true;
					}
					vx = 0;
					nx = x;
				}
				result.x += vx;
			}
			result.updatePos();
			

			// UP <- -> DOWN PHYSIC
			for (int loopx=result.lx; loopx<result.rx; loopx += blockwidth){
				int mx = loopx / blockwidth;
				if (loopx < 0) mx -= 1;
				if (ny != y && g.level.getMap(mx,ny) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vy > 0){
						result.y += blockheight-1-((result.dy) % blockheight);
						result.onground = true;
					}else{
						result.y -= (result.uy) % blockheight;
						result.top = true;
					}
					vy = 0;
					break;
				}
			}
			if (vy != 0){
				int loopx=result.rx;int mx = loopx / blockwidth;
				if (loopx < 0) mx -= 1;
				if (ny != y && g.level.getMap(mx,ny) != 0){
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vy > 0){
						result.y += blockheight-1-((result.dy) % blockheight);
						result.onground = true;
					}else{
						result.y -= (result.uy) % blockheight;
						result.top = true;
					}
					vy = 0;
				}
				result.y += vy;
			}
			result.updatePos();
			
			if (vy != 0) result.onground = false;
			
			nvx += vx;
			nvy += vy;
		}
		result.vx = nvx;
		result.vy = nvy;
		if (Math.abs(result.vx) > 0.3){
			result.vx *= friction;
		} else result.vx = 0;
		//if (!result.onground){
		result.vy += gravity;
		//}
		
		return result;
	}
	
}
