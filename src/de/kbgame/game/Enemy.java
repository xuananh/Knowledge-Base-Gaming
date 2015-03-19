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
	
	public Enemy(int x, int y, int width, int height, ImageSprite imageSprite) {
		super(x, y, width, height);
		sprite = imageSprite;
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
		g.graphic.drawImage(sprite.getCurrent(), x, y, width, height, rot, true);
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
}
