package de.kbgame.util;

import java.awt.geom.Line2D;

import de.kbgame.game.Entity;
import de.kbgame.game.Game;
import de.kbgame.game.Platform;
import de.kbgame.game.Player;
import de.kbgame.geometry.MyPoint;
import de.kbgame.geometry.Rectangle;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;

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
		PhysicResult result = fromAbovePhysic(g, e);

		int newBlockIndexX = (int) result.x / Level.BLOCK_WIDTH;
		int newBlockIndexY = (int) result.y / Level.BLOCK_HEIGHT;
		if (isKilling(g, newBlockIndexX, newBlockIndexY)) {
			g.kill(g.level.getMap(newBlockIndexX, newBlockIndexY));
		}

		// 2. step
		if (e instanceof Player) {
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

			// if (Math.abs(vx) > blockwidth) vx = blockwidth*Math.signum(vx); // Max Speed = 1 Block per Cycle
			// if (Math.abs(vy) > blockheight) vy = blockheight*Math.signum(vy); // Max Speed = 1 Block per Cycle

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

	public static PhysicResult platformPhysics(Game g, Player player, PhysicResult result) {
		Rectangle predictedRect = new Rectangle(result.lx, result.uy, result.wi, result.hi);
		MyPoint moveVec = new MyPoint(result.x - player.x, result.y - player.y);
		Line2D.Double entityMovement = new Line2D.Double(player.x, player.y, moveVec.x, moveVec.y);

		MyPoint src = new MyPoint();
		MyPoint dst = new MyPoint();

		Rectangle rect;

		for (Platform platform : g.platforms) {
			rect = platform.getSurroundingRectangle();

			if (predictedRect.intersects(rect)) {
				// collision detected
				result.setPhysicResult(player);

				Line2D.Double[] rectLines = rect.getLines();
				for (int line = 0; line < rectLines.length; line++) {
					boolean collision = false;

					src.setLocation(player.lx, player.uy);
					dst.add(src, moveVec);
					entityMovement.setLine(src, dst);
					if (entityMovement.intersectsLine(rectLines[line])) {
						log("top/left: " + line);
						collision = true;
					}

					src.setLocation(player.rx, player.uy);
					dst.add(src, moveVec);
					entityMovement.setLine(src, dst);
					if (entityMovement.intersectsLine(rectLines[line])) {
						log("top/right: " + line);
						collision = true;
					}

					src.setLocation(player.rx, player.dy);
					dst.add(src, moveVec);
					entityMovement.setLine(src, dst);
					if (entityMovement.intersectsLine(rectLines[line])) {
						log("bottom/right: " + line);
						collision = true;
					}

					src.setLocation(player.lx, player.dy);
					dst.add(src, moveVec);
					entityMovement.setLine(src, dst);
					if (entityMovement.intersectsLine(rectLines[line])) {
						log("bottom/left: " + line);
						collision = true;
					}

					if (collision) {
						if (line == de.kbgame.geometry.Rectangle.BOTTOM) {
							log("bottom");
							result.vy = Math.max(0, platform.vy);
						} else if (line == de.kbgame.geometry.Rectangle.LEFT || line == de.kbgame.geometry.Rectangle.RIGHT) {
							log("side");
							result.vx = platform.vx;
						} else {	 // TOP
							player.setParent(platform);
							log(player.x + " " + player.y);
							return result;
						}
					}
				}

				break;
			}
		}

		return result;
	}

	public static boolean rectangleOverlap(Rectangle r1, Rectangle r2) {
		return r1.intersects(r2);
	}

	public static void log(String str) {
		if (Game.DEBUG) {
			System.out.println(str);
		}
	}

	private final static boolean isBlocking(Game g, int x, int y) {
		return ((int) Math.pow(2, g.level.getMap(x, y)) & Blocks.NON_BLOCKING_BITMAP) == 0;
	}

	private final static boolean isKilling(Game g, int x, int y) {
		return ((int) Math.pow(2, g.level.getMap(x, y)) & Blocks.KILLING_BITMAP) != 0;
	}
}
