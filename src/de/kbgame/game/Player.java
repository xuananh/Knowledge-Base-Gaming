package de.kbgame.game;

import de.kbgame.geometry.MyPoint;
import de.kbgame.grafic.ImageSprite;
import de.kbgame.map.Blocks;
import de.kbgame.map.Level;
import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;
import de.kbgame.util.Status;

public class Player extends Entity {

	private final ImageSprite sprite;

	public Platform parent = null;
	private int parentOffsetX, parentOffsetY;
	public float rot = 0;

	public int hitdelay = 0;

	private Status status = Status.STANDING;

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = new ImageSprite("Images/sprite.png", 4, 3);
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

			setSprites(result);
		} else {
			event.update();
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

	public void getHit(Enemy e) {
		vx = (x > e.x) ? 10 : -10;
		vy = -3;
		// TODO: Life & Damage?
		hitdelay = 50;
	}
}
