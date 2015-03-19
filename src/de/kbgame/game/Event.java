package de.kbgame.game;

public class Event {
	
	protected Entity owner;
	
	Event() {
		
	}
	
	Event(Entity e) {
		owner = e;
	}
	
	public void setOwner(Entity e) {
		owner = e;
	}
	
	void update() {
		
	}

}
