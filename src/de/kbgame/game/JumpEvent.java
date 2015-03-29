package de.kbgame.game;

import de.kbgame.geometry.MyPoint;

public class JumpEvent extends Event {

	double dvy, vy, y, lastY, dx;
	int steps;
	MyPoint point;

	/**
	 * Ein Jump Event bewegt eine Entity ihrer aktuellen Position auf einer vorberechneten Bahn zu einem definierten Zielpunkt.
	 * 
	 * @param e die Entity auf die das Event angewendet werden soll
	 * @param dvy dieser Betrag wird in jedem Schritt von der akutellen y-Geschwindigkeit abgezogen
	 * @param vy die Geschwindigkeit in y Richtung
	 * @param steps die Anzahl an Schritte, in welcher das Event ausgeführt werden soll
	 * @param point der Zielpunkt, zu welchem die Entity gelangen soll
	 */
	public JumpEvent(Entity e, double dvy, double vy, int steps, MyPoint point) {
		super(e);

		this.dvy = dvy;
		this.vy = vy;
		this.steps = steps;
		this.point = point;
		this.lastY = e.y;
		this.y = e.y;
		this.dx = (point.x - e.x) / steps;
	}

	/**
	 * 
	 * @param dvy dieser Betrag wird in jedem Schritt von der akutellen y-Geschwindigkeit abgezogen
	 * @param vy die Geschwindigkeit in y Richtung
	 * @param steps die Anzahl an Schritte, in welcher das Event ausgeführt werden soll
	 * @param point der Zielpunkt, zu welchem die Entity gelangen soll
	 */
	public JumpEvent(double dvy, double vy, int steps, MyPoint point) {
		this.dvy = dvy;
		this.vy = vy;
		this.steps = steps;
		this.point = point;
	}

	@Override
	public void setOwner(Entity e) {
		owner = e;
		this.lastY = e.y;
		this.y = e.y;
		this.dx = (point.x - e.x) / steps;
	}

	/**
	 * Die Methode bewegt die Entity (owner) schrittweise auf einer vorberechneten Flugbahn zum gewählten Zielpunkt.
	 * Die eigentliche „Kurve“ wird durch die Schrittanzahl, die anfägnliche Geschwindigkeit in y-Richtung (vy) und die
	 * spezielle Gravitation (dvy) bestimmt.
	 */
	void update() {
		if (owner != null) {
			if (steps <= 0) {
				owner.y = (int) point.y;
				owner.x = (int) point.x;
				System.out.println(owner.x + " " + owner.y + " - " + ((int) point.x) + " " + ((int) point.y));
				owner.event = null;
				owner.vx = dx;
				owner.vy = vy;
			} else {
				double delta = (lastY + (point.y - lastY) / (double) steps);
				y += vy - lastY + delta;
				owner.y = (int) y;
				owner.x += (point.x - owner.x) / (steps + 1);//dx;
				lastY = delta;
				vy -= dvy;
			}
			steps--;
		}
	}
}
