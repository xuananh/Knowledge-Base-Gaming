package de.kbgame.game;

import de.kbgame.map.Level;
import de.kbgame.util.ImageKey;

public class Platform extends Entity {

	private boolean verticalMove = true;
	private boolean downOrRight = true;
	private int fromXY;
	private int toXY;

	public int speed = 1;

	public Platform(Game g, int x, int y, int width, int height, int fromBlockIndex, int toBlockIndex, boolean verticalMove) {
		super(x, y, width, height);

		fromXY = fromBlockIndex * Level.BLOCK_HEIGHT;
		toXY = toBlockIndex * Level.BLOCK_HEIGHT;
		this.verticalMove = verticalMove;

		vy = verticalMove ? 1 : 0;
		vx = verticalMove ? 0 : 1;
	}

	@Override
	public void update(Game g) {
		int xy;

		if (verticalMove) {
			if (downOrRight) {
				y = Math.min(toXY, y + speed);
			} else {
				y = Math.max(fromXY, y - speed);
			}

			xy = y;
		} else {
			if (downOrRight) {
				x = Math.min(toXY, x + speed);
			} else {
				x = Math.max(fromXY, x - speed);
			}

			xy = x;
		}

		if (xy == toXY || xy == fromXY) {
			downOrRight = !downOrRight;
			vy *= -1;
			vx *= -1;
		}

		updatePos();
	}

	@Override
	public void draw(Game g) {
		g.graphic.drawImage(ImageKey.BLOCK, x, y, width, height, 0);
	}
}