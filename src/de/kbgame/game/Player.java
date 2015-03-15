package de.kbgame.game;

import de.kbgame.geometry.MyPoint;
import de.kbgame.grafic.TimeBasedImageSprite;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;
import de.kbgame.util.GameState;
import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;
import de.kbgame.util.Status;
import de.kbgame.util.hud.HUD;
import de.kbgame.util.hud.LifeHearts;
import de.kbgame.util.hud.Points;

public class Player extends Entity {

	private final TimeBasedImageSprite sprite;
	private final static int MAX_COLLISION_STEPS = 10;

	public Platform parent = null;
	private int parentOffsetX, parentOffsetY;
	public float rot = 0;

	public int hitdelay = 0;

	private Status status = Status.STANDING;
	public LifeHearts lifes = new LifeHearts(2);
	private Points points = new Points(0);

	public Player(int x, int y, int width, int height, HUD hud) {
		super(x, y, width, height);
		sprite = new TimeBasedImageSprite("Images/sprite.png", 4, 3, 100);
		
		hud.add(lifes);
		hud.add(points);
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
				points.add(100,this);
			}
		}

		// lucky kill
		if (maxRuns == 0) {
			vy = -Physic.JUMP_VELOCITY;
			((Enemy) e).kill(g);
			points.add(100,this);
		}
	}

	@Override
	public void draw(Game g) {
		if (parent == null) {
			g.graphic.drawImage(sprite.getCurrent(), x, y, this.width, this.height, rot, true);
		} else {
			int _y = parent.y - height;
			g.graphic.drawImage(sprite.getCurrent(), x, _y, this.width, this.height, rot, true);
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
		
		sprite.noUpdate = false;
		
		switch (status) {
			case MOVE_LEFT:
				sprite.setCurrentRow(1);
				break;
			case MOVE_RIGHT:
				sprite.setCurrentRow(2);
				break;
			default:
				sprite.noUpdate = true;
				sprite.setIndex(1);
				break;
		}

		sprite.update();
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
		if (hitdelay > 0) {
			return;
		}
		
		if (e != null) {
			vx = (x > e.x) ? 10 : -10;
			vy = -3;
		}
		hitdelay = 50;
		
		if (--lifes.hearts <= 0) {
			kill(g);
		}
	}
	
	public void kill(Game g) {
		lifes.hearts = 0;
		g.state = GameState.DEAD;
	}
}
