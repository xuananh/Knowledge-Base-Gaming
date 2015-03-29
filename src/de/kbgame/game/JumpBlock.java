package de.kbgame.game;

import de.kbgame.map.Blocks;
import de.kbgame.map.Level;

public class JumpBlock {
	
	public int x, y;
	private JumpEvent event;
	
	/**
	 * instanziiert einen neuen Jump-Block mit einem dazugehörigen Jump-Event und aktualisiert das entsprechende Level
	 * 
	 * @param x der x Block-Index
	 * @param y der y Block-Index
	 * @param e das dazugehörige Jump-Event
	 * @param lvl das Level zu welchem diese Jump-Block Instanz gehört
	 */
	public JumpBlock(int x, int y, JumpEvent e, Level lvl) {
		this(x, y, e);
		
		setMap(lvl);
	}
	
	/**
	 * instanziiert ein neuen Jump-Block mit einem dazugehörigen Jump-Event
	 * 
	 * @param x der x Block-Index
	 * @param y der y Block-Index
	 * @param e das dazugehörige Jump-Event
	 */
	public JumpBlock(int x, int y, JumpEvent e) {
		this.x = x;
		this.y = y;
		this.event = e;
	}
	
	/**
	 * fügt diesen Jump-Block zu einem bestimmten Level hinzu
	 * 
	 * @param level das Level zu welchem diese Jump-Block Instanz gehört
	 */
	public void setMap(Level level) {
		level.setMap(x, y, Blocks.JUMP);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = x;
	}
	
	/**
	 * verschiebt den Jump-Block zu einer anderen Position
	 * 
	 * @param x der neue x Block-Index
	 * @param y der neue y Block-Index
	 * @param level das Level zu welchem der Jump-Block gehören soll
	 */
	public void setLocation(int x, int y, Level level) {
		// reset old location
		level.setMap(this.x, this.y, Blocks.Empty);
		
		this.x = x;
		this.y = x;

		// set old location
		setMap(level);	
	}
	
	/**
	 * @return gibt das gespeicherte Jump-Event zurück
	 */
	public JumpEvent getJumpEvent() {
		return event;
	}

}
