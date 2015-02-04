package de.kbgame.game;

import de.kbgame.geometry.MyPoint;
import de.kbgame.grafic.ImageSprite;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;
import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;
import de.kbgame.util.Status;
import de.kbgame.util.hud.HUD;
import de.kbgame.util.hud.LifeHearts;

public class Player extends Entity {

	private final ImageSprite sprite;
	private final static int MAX_COLLISION_STEPS = 10;

	public Platform parent = null;
	private int parentOffsetX, parentOffsetY;
	public float rot = 0;

	public int hitdelay = 0;

	private Status status = Status.STANDING;
	private LifeHearts lifes = new LifeHearts(2);

	public Player(int x, int y, int width, int height, HUD hud) {
		super(x, y, width, height);
		sprite = new ImageSprite("Images/sprite.png", 4, 3);
		
		hud.add(lifes);
	}

	public void setParent(Platform platform) {
		parent = platform;

		if (platform != null) {
			parentOffsetX = x - platform.x;
			parentOffsetY = y - platform.y;

			vx = 0;
			vy = 0;
		}
	}

	@Override
	public void update(Game g) {
		if (hitdelay > 0) {
			hitdelay--;
		}

		if (event == null) {
			int nx = 0, ny = 0;
			int lx = 0, rx = 0;

			MyPoint before = new MyPoint(x, y), after;
			PhysicResult result = Physic.doPhysic(g, this);

			if (parent != null) {
				parentOffsetX = (int) result.x - parentOffsetX;

				nx = (int) result.x;
				ny = parent.y + parentOffsetY;

				vy = parent.vy;
				vx = result.vx;
				// vx = parent.vx;

				lx = x - width / 2;
				rx = lx + width - 1; // != x+wi/2
			}

			if (parent != null && (result.y < ny || rx < parent.lx || lx > parent.rx)) {
				result.apply(this);
				setParent(null);
			} else if (parent != null) {
				x = nx;
				y = ny;
			} else {
				result.apply(this);
			}

			after = new MyPoint(x, y);

			// detect enemy collisions
			for (Entity e : g.list) {
				if (e instanceof Enemy && hitdelay <= 0) {
					// first indication via rectangle overlapping
					if (e.getSurroundingRectangle().intersects(getSurroundingRectangle())) {
						enemyCollision((Enemy) e, before, after, g);
					}
				}
			}

			setSprites(result);
		} else {
			event.update();
		}
	}
	
	private void enemyCollision(Enemy e, MyPoint before, MyPoint after, Game g) {
		de.kbgame.geometry.Rectangle enemyRect = e.getSurroundingRectangle();
		boolean xIn, yIn;
		MyPoint intermediatePoint = new MyPoint(before);
		int maxRuns = MAX_COLLISION_STEPS;

		xIn = intermediatePoint.x > enemyRect.x && intermediatePoint.x < (enemyRect.x + enemyRect.width);
		yIn = intermediatePoint.y > enemyRect.y && intermediatePoint.y < (enemyRect.y + enemyRect.height);
		if (xIn && yIn) {
			maxRuns = -1;
			getHit((Enemy) e, g);
		}

		while (--maxRuns > 0) {
			if (xIn && yIn) {
				after.copy(intermediatePoint);
			} else {
				before.copy(intermediatePoint);
			}
			intermediatePoint.add(before, after);
			intermediatePoint.multiply(.5);

			xIn = intermediatePoint.x > enemyRect.x && intermediatePoint.x < (enemyRect.x + enemyRect.width);
			yIn = intermediatePoint.y > enemyRect.y && intermediatePoint.y < (enemyRect.y + enemyRect.height);

			if (!xIn && yIn) {
				// hit from left or right
				getHit((Enemy) e, g);
				maxRuns = -1;
			} else if (xIn && !yIn) {
				// TODO no kill from below
				vy = -Physic.JUMP_VELOCITY;
				((Enemy) e).kill(g);
				maxRuns = -1;
			}
		}

		// lucky kill
		if (maxRuns == 0) {
			vy = -Physic.JUMP_VELOCITY;
			((Enemy) e).kill(g);
		}
	}

	@Override
	public void draw(Game g) {
		if (parent == null) {
			g.graphic.drawImage(sprite.getSprite(), x, y, this.width, this.height, rot, true);
		} else {
			int _y = parent.y - height;
			g.graphic.drawImage(sprite.getSprite(), x, _y, this.width, this.height, rot, true);
		}
	}

	@Override
	public void jump(Game g) {
		int blockX = x / Level.BLOCK_WIDTH;
		int blockY = y / Level.BLOCK_HEIGHT;

		if (g.level.getMap(blockX, blockY + 1) == Blocks.JUMP) {
			// player's center must be above the jump block
			if (event == null) {
				this.event = new JumpEvent(this, -0.2, -10, 100, new MyPoint(5 * 50, 36 * 50));
			}
		} else {
			super.jump(g);
		}
	}

	private void setSprites(PhysicResult result) {
		setPlayerStatus(result);
		switch (status) {
			case MOVE_LEFT:
				if (sprite.getIndex() == 5) {
					sprite.setIndex(3);
				}
				break;
			case MOVE_RIGHT:
				if (sprite.getIndex() == 8) {
					sprite.setIndex(6);
				}
				break;
			case JUMP:
				sprite.setIndex(4);
				break;
			default:
				sprite.setIndex(0);
				break;
		}
		sprite.setIndexInkrement();
	}

	private void setPlayerStatus(PhysicResult result) {
		if (result.vx > 0) {
			status = Status.MOVE_RIGHT;
		} else if (result.vx < 0) {
			status = Status.MOVE_LEFT;
		} else {
			status = Status.STANDING;
		}
	}

	public void getHit(Enemy e, Game g) {
		vx = (x > e.x) ? 10 : -10;
		vy = -3;
		hitdelay = 50;
		
		if (--lifes.hearts <= 0) {
			// TODO
			g.shouldApplicationExit = true;
		}
	}
}
