package de.kbgame.game;

import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;
import de.kbgame.util.Status;

public class Player extends Entity {

	private Platform parent = null;
	private int parentOffsetX, parentOffsetY;
	public float rot = 0;

	private Status status = Status.STANDING;
	private int sprite = 0;

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
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
		int nx = 0, ny = 0;

		PhysicResult result = Physic.doPhysic(g, this);

		if (parent != null) {
			nx = parent.x + parentOffsetX;
			ny = parent.y + parentOffsetY;

			vy = parent.vy;
			return;
		}

		if (parent != null && result.y < ny) {
			result.apply(this);
			setParent(null);
		} else if (parent != null) {
			x = nx;
			y = ny;
		} else {
			result.apply(this);
		}

		setSprites(result);
	}

	@Override
	public void draw(Game g) {
		g.graphic.drawImage(sprite, x, y, this.width, this.height, rot);
	}

	private void setSprites(PhysicResult result) {
		setPlayerStatus(result);
		if (sprite > 10) {
			sprite = 0;
		}
		switch (status) {
		case MOVE_LEFT:
			if (sprite == 5) {
				sprite = 3;
			}
			break;
		case MOVE_RIGHT:
			if (sprite == 8) {
				sprite = 6;
			}
			break;
		case JUMP:
			sprite = 4;
			break;
		default:
			sprite = 0;
			break;
		}
		sprite++;
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
}
