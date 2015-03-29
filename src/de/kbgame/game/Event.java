package de.kbgame.game;

public class Event {
	
	protected Entity owner;
	
	Event() {
		
	}
	
	Event(Entity e) {
		owner = e;
	}
	
	/**
	 * Ein Event wird auf eine festgelegte Enity angewendet.
	 * 
	 * @param e die Entity, auf welche das Event angewendet werden soll.
	 */
	public void setOwner(Entity e) {
		owner = e;
	}
	
	void update() {
		
	}

}
