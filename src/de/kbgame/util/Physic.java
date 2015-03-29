package de.kbgame.util;

import java.awt.Point;

import de.kbgame.game.Entity;
import de.kbgame.game.Game;
import de.kbgame.game.Platform;
import de.kbgame.game.Player;
import de.kbgame.geometry.Rectangle;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;
import de.kbgame.util.hud.Points;
import de.kbgame.util.sound.SoundKey;

public final class Physic {

	public static final double JUMP_VELOCITY = 10.0;
	public static final double WALK_VELOCITY = 1.0;
	public static final double GRAVITY = 0.33;
	public static final double FRICTION = 0.8;
	public static final double TRESHOLD = 0.2;
	public static final double WALL_DISTANCE = 0.001;

	private Physic() {
		throw new AssertionError();
	}

	public static PhysicResult doPhysic(Game g, Entity e) {
		// 1. step
		// gravity and deadly blocks
		PhysicResult result = fromAbovePhysic(g, e);

		int newBlockIndexX = (int) result.x / Level.BLOCK_WIDTH;
		int newBlockIndexY = (int) result.y / Level.BLOCK_HEIGHT;
		if (isKilling(g, newBlockIndexX, newBlockIndexY)) {
			if(e instanceof Player) {
				g.kill(g.level.getMap(newBlockIndexX, newBlockIndexY));
			}else {
				//e.kill(g);
			}
		} 

		// 2. step
		// coins and platforms
		if (e instanceof Player) {
			if (g.level.getMap(newBlockIndexX, newBlockIndexY) == Blocks.COIN) {
				g.level.setMap(newBlockIndexX, newBlockIndexY, Blocks.Empty);
				g.player.addPoints(Points.COIN_SCORE);
				g.sounds.sound(SoundKey.COIN);
			}
			
			result = platformPhysics(g, (Player) e, result);
		}

		return result;
	}

	public static PhysicResult basicMove(Game g, Entity e) {
		PhysicResult result = new PhysicResult(e.x, e.y, e.vx, e.vy, false, false, false, false, e.lx, e.rx, e.uy, e.dy, e.width, e.height);

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
			ovx -= vx; // Remove the currently calculated move from the "movepool"
			ovy -= vy;

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
				newBlockIndexX -= 1;// Sonst gibt es zwischen 0 und -1 einen Rundungsfehler... ( (int)1.6 == 1;
									// (int)0.6 == 0; (int)-0.6 == 0 statt -1). Daher -1
			}

			// LEFT <- -> RIGHT PHYSIC
			for (double loopY = result.uy; loopY < result.dy; loopY += blockheight) {
				int my = (int) (loopY / blockheight);

				if (loopY < 0) {
					my -= 1;
				}

				if (newBlockIndexX != currentBlockIndexX && isBlocking(g, newBlockIndexX, my)) {
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vx > 0) {
						result.x += blockwidth - WALL_DISTANCE - (result.rx % blockwidth);
						result.right = true;
					} else {
						result.x -= (result.lx) % blockwidth;
						result.left = true;
					}
					vx = 0;
					break;
				}
			}
			if (vx != 0) {
				double loopY = result.dy;
				int my = (int) (loopY / blockheight);

				if (loopY < 0) {
					my -= 1;
				}

				if (newBlockIndexX != currentBlockIndexX && isBlocking(g, newBlockIndexX, my)) {
					// if your size would be > 1 Block, this check is more
					// complex...
					// Then you would adjust vx instead of result.x directly...
					if (vx > 0) {
						result.x += blockwidth - WALL_DISTANCE - (result.rx % blockwidth);
						result.right = true;
					} else {
						result.x -= result.lx % blockwidth;
						result.left = true;
					}
					vx = 0;
				}
				result.x += vx;
			}
			result.updatePos();
			newBlockIndexX = (int) (result.x / blockwidth);
			currentBlockIndexX = (int) (result.x / blockwidth); // Aktuelle Position
			currentBlockIndexY = (int) (result.y / blockheight);

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

			for (double loopX = result.lx; loopX < result.rx; loopX += blockwidth) {
				int blockIndex = (int) (loopX / blockwidth);

				if (loopX < 0) {
					blockIndex -= 1;
				}

				if (newBlockIndexY != currentBlockIndexY && isBlocking(g, blockIndex, newBlockIndexY)) {
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vy > 0) {
						result.y += blockheight - WALL_DISTANCE - (result.dy % blockheight);
						result.onground = true;
					} else {
						result.y -= result.uy % blockheight;
						result.top = true;
						if (g.level.getMap(blockIndex, newBlockIndexY) == Blocks.QuestionBlock) {
							g.level.setMap(blockIndex, newBlockIndexY, Blocks.QUESTION_BLOCK_BOUNCED);
							if (g.level.getMap(blockIndex, newBlockIndexY - 1) == Blocks.Empty) {
								g.level.setMap(blockIndex, newBlockIndexY - 1, Blocks.COIN);
							}
						}
					}
					vy = 0;
					break;
				}
			}
			if (vy != 0) {
				double loopX = result.rx;
				int mx = (int) (loopX / blockwidth);

				if (loopX < 0) {
					mx -= 1;
				}
				if (newBlockIndexY != currentBlockIndexY && isBlocking(g, mx, newBlockIndexY)) {
					// if your size would be > 1 Block, this check is more complex...
					// Then you would adjust vx instead of result.x directly...
					if (vy > 0) {
						result.y += blockheight - WALL_DISTANCE - (result.dy % blockheight);
						result.onground = true;
					} else {
						result.y -= result.uy % blockheight;
						result.top = true;
						if (g.level.getMap(mx, newBlockIndexY) == Blocks.QuestionBlock) {
							g.level.setMap(mx, newBlockIndexY, Blocks.QUESTION_BLOCK_BOUNCED);
						}
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
			newBlockIndexY = (int) (result.y / blockheight);
			currentBlockIndexX = (int) (result.x / blockwidth); // Aktuelle Position
			currentBlockIndexY = (int) (result.y / blockheight);
		}

		result.vx = Math.min(nvx, Level.BLOCK_WIDTH - 1);
		result.vy = Math.min(nvy, Level.BLOCK_HEIGHT - 1);

		return result;
	}

	public static PhysicResult fromAbovePhysic(Game g, Entity e) {
		PhysicResult result = basicMove(g, e);
		if (Math.abs(result.vx) > TRESHOLD) {
			result.vx *= FRICTION;
		} else {
			result.vx = 0;
		}

		result.vy += GRAVITY;

		return result;
	}

	/**
	 * Berechnet mögliche Kollisionen von Plattformen mit dem Spieler. Eine Kollision wird erst ganz allgemein mit 
	 * einer Überlagerung der umschließenden Rechtecke ermittelt. Sollte eine Kollision stattgefunden haben, wird
	 * mittels der vorherigen und der neuen Positionen von Spieler und entsprechender Plattform die Richtung der
	 * Kollision bestimmt. Wenn der Spieler von oben auf die Plattform trifft, wird diese ihm zugeordnet.
	 * 
	 * @param g
	 * @param player
	 * @param result die aktuelle Bewegung des Spielers, die evtl. innerhalb der Methode verändert werden müssen
	 * @return ggf. eine überarbeitete Bewegung des Spielers
	 */
	public static PhysicResult platformPhysics(Game g, Player player, PhysicResult result) {
		Rectangle predictedRect = new Rectangle(result.lx, result.uy, result.wi, result.hi);
		Rectangle rect;

		for (Platform platform : g.platforms) {
			rect = platform.getSurroundingRectangle();

			if (platform != player.parent && predictedRect.intersects(rect)) {	
				Point oldPlatformLocation = platform.getOldPoint();

				int oldPlatformLx = oldPlatformLocation.x - platform.width / 2;
				int oldPlatformRx = oldPlatformLx + platform.width - 1;
				int oldPlatformUy = oldPlatformLocation.y - platform.height / 2;
				int oldPlatformDy = oldPlatformUy + platform.height - 1;
				
				// compare old positions with the new ones
				boolean collidedFromLeft = player.rx <= oldPlatformLx && result.rx >= platform.lx;
				boolean collidedFromRight = player.lx >= oldPlatformRx && result.lx < platform.rx;
				boolean collidedFromTop = player.dy < oldPlatformUy && result.dy >= platform.uy;
				boolean collidedFromBottom = player.uy >= oldPlatformDy && result.uy < platform.dy;
				
				if (collidedFromBottom) {
					result.vy = platform.vy;
					result.y = player.y;
				} else if (collidedFromTop) {
					// jump onto a platform => bind to that platform
					player.setParent(platform);
				} else if (collidedFromLeft || collidedFromRight) {
					result.vx = 0;
					if (platform.verticalMove) {
						// on vertical move just nullify horizontal velocity
						result.x = player.x;
					} else if (collidedFromLeft) {
						// push player to left
						result.x = platform.lx - player.width / 2 - 1;
					} else {
						// push player to right
						result.x = platform.rx + player.width / 2;
					}
				}
			}
		}

		return result;
	}

	/**
	 * Ermittelt ob sich 2 Rechtecke überschneiden. 
	 * 
	 * @param r1 Rechteck 1
	 * @param r2 Rechteck 2
	 * @return gibt an ob eine Überschneidung existiert.
	 */
	public static boolean rectangleOverlap(Rectangle r1, Rectangle r2) {
		return r1.intersects(r2);
	}

	public static void log(String str) {
		if (Game.DEBUG) {
			System.out.println(str);
		}
	}

	/**
	 * Ermittelt anhand der in de.kbgame.map.Blocks definierten Bitmask, ob ein Block undurchlässig ist.
	 * 
	 * @param g
	 * @param x der x Index des Blocks
	 * @param y der y Index des Blocks
	 * @return gibt true zurück, sollte der gegebene Block undurchlässig sein
	 */
	public final static boolean isBlocking(Game g, int x, int y) {
		return ((int) Math.pow(2, g.level.getMap(x, y)) & Blocks.NON_BLOCKING_BITMASK) == 0;
	}


	/**
	 * Ermittelt anhand der in de.kbgame.map.Blocks definierten Bitmask, ob ein Block tötlich ist.
	 * 
	 * @param g
	 * @param x der x Index des Blocks
	 * @param y der y Index des Blocks
	 * @return gibt true zurück, wenn der gegebene Block tötlich ist
	 */
	public final static boolean isKilling(Game g, int x, int y) {
		return ((int) Math.pow(2, g.level.getMap(x, y)) & Blocks.KILLING_BITMASK) != 0;
	}
}
