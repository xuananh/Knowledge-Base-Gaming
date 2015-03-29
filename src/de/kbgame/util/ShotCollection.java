package de.kbgame.util;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Vector;

import de.kbgame.game.Game;
import de.kbgame.game.Player;

@SuppressWarnings("serial")
public class ShotCollection extends LinkedList<Shot> {

	private int updateInterveral;
	protected long nextUpdate = 0;
	int velocity;
	de.kbgame.geometry.Rectangle playerHitBox;
	public Player player;
	private Vector<Shot> removeList = new Vector<Shot>();
	public Point origin;

	/**
	 * Gruppiert Schüsse in einer Collection. Definiert zusätzliche Eigenschaften, die für alle enthaltenen Schüsse identisch sind.
	 * 
	 * @param updateInterval das Update-Intervall in Millisekunden. Die Aktualisierung findet zeitbasiert statt
	 * @param velocity die Geschwindigkeit der einzelnen Schüsse
	 * @param origin der Ausgangspunkt, von welchem die Schüsse abgefeuert werden
	 * @param player der Spieler, welcher von den einzelnen Schüssen zur Kollisionserkennung herangezogen wird
	 */
	public ShotCollection(int updateInterval, int velocity, Point origin, Player player) {
		this(updateInterval, velocity, origin);
		this.player = player;
	}
	
	public ShotCollection(int updateInterval, int velocity, Point origin) {
		this.updateInterveral = updateInterval;
		this.velocity = velocity;
		this.origin = origin;
	}

	/**
	 * (1) Entfernt alte Schüsse aus der Liste.
	 * (2) Aktualisiert alle Schüsse in der Collection, wenn ein definierte Zeitintervall überschritten wurde. 
	 * 
	 * @param g
	 */
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

	/**
	 * Schüsse, die den Spieler getroffen haben oder auf einen undurchdringlichen Block getroffen sind, können sich dieser
	 * Liste hinzufügen und werden aus dieser Collection gelöscht und nicht mehr aktualisiert.
	 * 
	 * @param shot der Schuss, welcher aus der Liste entfernt werden soll
	 */
	public void addToRemoveList(Shot shot) {
		removeList.add(shot);
	}

	public void autofill() {
		for (int i = 0; i < 100; i++) {
			this.add(new Shot(-1, i * 130, this));
		}
	}

}
