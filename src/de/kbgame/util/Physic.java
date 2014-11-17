package de.kbgame.util;

import de.kbgame.game.Entity;
import de.kbgame.game.Game;
import de.kbgame.map.Level;

public final class Physic {

	public static final double JUMP_VELOCITY = 14;
	public static final double GRAVITY = 0.11;
	public static final double FRICTION = 0.8;

	private Physic() {
		throw new AssertionError();
	}

	public static PhysicResult doPhysic(Game g, Entity e) {
		// TODO: Different Physic Types
		return fallPhysic(g, e);
	}

	public static PhysicResult fallPhysic(Game g, Entity e) {
		PhysicResult result = new PhysicResult(e.x, e.y, e.vx, e.vy, e.onground, false, false, false, e.lx, e.rx, e.uy, e.dy, e.wi, e.hi);

		int blockheight = Level.BLOCK_HEIGHT;
		int blockwidth = Level.BLOCK_WIDTH;

		int newBlockIndexY, newBlockIndexX;

		double ovx = result.vx, nvx = 0;
		double ovy = result.vy, nvy = 0;
		
		while (ovx != 0 || ovy != 0) {
			double vx = ovx, vy = ovy;
			double hlp;

			if (ovx > blockwidth || ovy > blockheight) { // important if Velocity > 1 Block
				if (Math.abs(ovx) > Math.abs(ovy)) { // Partitionate in Vectors of the same direction with each vx, vy less/equal than block
					vx = Math.signum(ovx) * blockwidth;
					vy = vx / ovx * ovy;
				} else {
					vy = Math.signum(ovy) * blockwidth;
					vx = vy / ovy * ovx;
				}
			}
			ovx -= vx; // Remove the currently calculated move from the
						// "movepool"
			ovy -= vy;

			// if (Math.abs(vx) > blockwidth) vx = blockwidth*Math.signum(vx);
			// // Max Speed = 1 Block per Cycle
			// if (Math.abs(vy) > blockheight) vy = blockheight*Math.signum(vy);
			// // Max Speed = 1 Block per Cycle

			// current position
			int currentBlockIndexX = (int) (result.x / blockwidth);
			int currentBlockIndexY = (int) (result.y / blockheight);

			if (vx >= 0) {
				hlp = result.rx + vx;
			} else {
				hlp = result.lx + vx;
			}
			newBlockIndexX = (int) (hlp / blockwidth);
			if (hlp < 0) {
				newBlockIndexX -= 1;// Sonst gibt es zwischen 0 und -1 einen
									// Rundungsfehler... ( (int)1.6 == 1;
									// (int)0.6 == 0; (int)-0.6 == 0 statt -1).
									// Daher -1
			}

			// LEFT <- -> RIGHT PHYSIC
			for (int loopy = result.uy; loopy < result.dy; loopy += blockheight) {
				int my = loopy / blockheight;

				if (loopy < 0) {
					my -= 1;
				}

				if (newBlockIndexX != currentBlockIndexX && g.level.getMap(newBlockIndexX, my) != 0) {
					// if your size would be > 1 Block, this check is more
					// complex...
					// Then you would adjust vx instead of result.x directly...
					if (vx > 0) {
						result.x += blockwidth - 1 - (result.rx % blockwidth);
						result.right = true;
					} else {
						result.x -= (result.lx) % blockwidth;
						result.left = true;
					}
					vx = 0;
					newBlockIndexX = currentBlockIndexX;
					break;
				}
			}
			if (vx != 0) {
				int my = result.dy / blockheight;

				if (result.dy < 0) {
					my -= 1;
				}

				if (newBlockIndexX != currentBlockIndexX && g.level.getMap(newBlockIndexX, my) != 0) {
					// if your size would be > 1 Block, this check is more
					// complex...
					// Then you would adjust vx instead of result.x directly...
					if (vx > 0) {
						result.x += blockwidth - 1 - ((result.rx) % blockwidth);
						result.right = true;
					} else {
						result.x -= (result.lx) % blockwidth;
						result.left = true;
					}
					vx = 0;
					newBlockIndexX = currentBlockIndexX;
				}
				result.x += vx;
			}
			result.updatePos();

			// UP <- -> DOWN PHYSIC
			//
			// player size might be bigger than 1 block
			// check all blocks from left (lx) to right (rx)
			if (vy >= 0) {
				hlp = result.dy + vy;
			} else {
				hlp = result.uy + vy;
			}
			newBlockIndexY = (int) (hlp / blockheight);
			if (hlp < 0) {
				newBlockIndexY -= 1;
			}

			for (int loopX = result.lx; loopX < result.rx; loopX += blockwidth) {
				int blockIndex = loopX / blockwidth;

				if (loopX < 0) {
					blockIndex -= 1;
				}

				if (newBlockIndexY != currentBlockIndexY && g.level.getMap(blockIndex, newBlockIndexY) != 0) {
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vy > 0) {
						result.y += blockheight - 1 - (result.dy % blockheight);
						result.onground = true;
					} else {
						result.y -= (result.uy) % blockheight;
						result.top = true;
					}
					vy = 0;
					break;
				}
			}
			if (vy != 0) {
				int mx = result.rx / blockwidth;

				if (result.rx < 0) {
					mx -= 1;
				}
				if (newBlockIndexY != currentBlockIndexY && g.level.getMap(mx, newBlockIndexY) != 0) {
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vy > 0) {
						result.y += blockheight - 1 - (result.dy % blockheight);
						result.onground = true;
					} else {
						result.y -= (result.uy) % blockheight;
						result.top = true;
					}
					vy = 0;
				}
				result.y += vy;
			}
			result.updatePos();

			if (vy != 0) {
				result.onground = false;
			}

			nvx += vx;
			nvy += vy;
		}

		result.vx = nvx;
		result.vy = nvy;

		if (Math.abs(result.vx) > 0.3) {
			result.vx *= FRICTION;
		} else {
			result.vx = 0;
		}
		// if (!result.onground){
		result.vy += GRAVITY;
		// }

		return result;
	}
}