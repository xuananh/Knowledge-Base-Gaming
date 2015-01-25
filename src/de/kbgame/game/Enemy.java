package de.kbgame.game;

import de.kbgame.grafic.ImageSprite;
import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;
import de.kbgame.util.Status;

public class Enemy extends Entity {

	public float rot = 0;
	public boolean facing = true;
	private final ImageSprite sprite;
	private Status status = Status.STANDING;

	public Enemy(int x, int y, int width, int height) {
		super(x, y, width, height);
		sprite = new ImageSprite("Images/enemy.png", 1, 1);
		sprite.setIndex(0);
	}

	@Override
	public void update(Game g) {
		vx += (facing) ? 0.6 : -0.6;
		if (onground) {
			vy = -3;
		}

		PhysicResult pr = Physic.doPhysic(g, this);
		pr.apply(this);

		if (!facing && pr.left) {
			facing = true;
		} else if (facing && pr.right) {
			facing = false;
		}

		setSprites(pr);
	}

	@Override
	public void draw(Game g) {
		g.graphic.drawImage(sprite.getSprite(), x, y, width, height, rot, true);
	}

	private void setSprites(PhysicResult result) {
		setPlayerStatus(result);
		switch (status) {
		/*case MOVE_LEFT:
			if (sprite.getIndex() == 4) {
				sprite.setIndex(3);
			} else {
				sprite.setIndex(4);
			}
			break;
		case MOVE_RIGHT:
			if (sprite.getIndex() == 9) {
				sprite.setIndex(10);
			} else {
				sprite.setIndex(9);
			}
			break;*/
			default:
				sprite.setIndex(0);
				break;
		}
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

	public void kill(Game g) {
		g.removeFromList.add(this);
	}
}
