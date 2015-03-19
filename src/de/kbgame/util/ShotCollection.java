package de.kbgame.util;

import java.awt.Point;
import java.util.Vector;

import de.kbgame.game.Game;
import de.kbgame.game.Player;

@SuppressWarnings("serial")
public class ShotCollection extends Vector<Shot> {

	private int updateInterveral;
	private long nextUpdate = 0;
	int velocity;
	de.kbgame.geometry.Rectangle playerHitBox;
	public Player player;
	private Vector<Shot> removeList = new Vector<Shot>();
	public Point origin;

	public ShotCollection(int updateInterval, int velocity, Point origin) {
		this.updateInterveral = updateInterval;
		this.velocity = velocity;
		this.origin = origin;
	}

	public ShotCollection(int updateInterval, int velocity, Point origin, Player player) {
		this(updateInterval, velocity, origin);
		this.player = player;
	}

	public void update(Game g) {
		long currentTime = System.currentTimeMillis();

		if (!removeList.isEmpty()) {
			for (Shot shot : removeList) {
				remove(shot);
			}
			removeList.clear();
		}

		if (currentTime > nextUpdate) {
			playerHitBox = g.player.getSurroundingRectangle();

			for (Shot shot : this) {
				shot.update(g);
			}

			nextUpdate = System.currentTimeMillis() + updateInterveral;
		}
	}

	public void draw(Game g) {
		for (Shot shot : this) {
			shot.draw(g);
		}
	}

	public void addToRemoveList(Shot shot) {
		removeList.add(shot);
	}

	public void autofill() {
		for (int i = 0; i < 100; i++) {
			this.add(new Shot(-1, i * 130, this));
		}
	}

}
