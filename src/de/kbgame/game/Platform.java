package de.kbgame.game;

import java.awt.Point;

import de.kbgame.geometry.ImageKey;
import de.kbgame.map.Level;

public class Platform extends Entity {

	public boolean verticalMove = true;
	private boolean downOrRight = true;
	public int fromXY;
	public int toXY;
	private Point oldPoint = new Point(-1, -1);

	public int speed = 1;

	/**
	 * Erstellt eine Plattform an einer bestimmten Pixelposition mit eine bestimmten Breite und Höhe. Die Plattform
	 * bewegt sich von einem Block zu einem anderen. Dabei kann sich eine Plattform entweder vertikal oder horizontal
	 * bewegen.
	 * 
	 * @param g
	 * @param x der x Wert der Startposition
	 * @param y der y Wert der Startposition
	 * @param width die Breite der Plattform
	 * @param height die Höhe der Plattform
	 * @param fromBlockIndex die Plattform bewegt sich abwechselnd zum und vom Block mit diesem Index
	 * @param toBlockIndex die Plattform bewegt sich abwechselnd zum und vom Block mit diesem Index
	 * @param verticalMove die Plattform kann sich entweder vertikal oder horizontal bewegen
	 */
	public Platform(Game g, int x, int y, int width, int height, int fromBlockIndex, int toBlockIndex, boolean verticalMove) {
		super(x, y, width, height);

		fromXY = fromBlockIndex * Level.BLOCK_HEIGHT + width / 2;
		toXY = toBlockIndex * Level.BLOCK_HEIGHT + height / 2;
		this.verticalMove = verticalMove;

		vy = verticalMove ? 1 : 0;
		vx = verticalMove ? 0 : 1;
	}

	@Override
	public void update(Game g) {
		int xy;
		
		oldPoint.x = x;
		oldPoint.y = y;

		if (verticalMove) {
			if (downOrRight) {
				y = (int) Math.min(toXY, y + speed * g.gaFactor);
			} else {
				y = (int) Math.max(fromXY, y - speed * g.gaFactor);
			}

			xy = y;
		} else {
			if (downOrRight) {
				x = (int) Math.min(toXY, x + speed * g.gaFactor);
			} else {
				x = (int) Math.max(fromXY, x - speed * g.gaFactor);
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
	
	/**
	 * @return gibt den Punkt zurück an welchem sich die Plattform im vorherigen Frame befand
	 */
	public Point getOldPoint() {
		return oldPoint;
	}
}