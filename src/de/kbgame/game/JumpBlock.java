package de.kbgame.game;

import de.kbgame.map.Blocks;
import de.kbgame.map.Level;

public class JumpBlock {
	
	public int x, y;
	private JumpEvent event;
	
	public JumpBlock(int x, int y, JumpEvent e, Level lvl) {
		this(x, y, e);
		
		setMap(lvl);
	}
	
	public JumpBlock(int x, int y, JumpEvent e) {
		this.x = x;
		this.y = y;
		this.event = e;
	}
	
	public void setMap(Level lvl) {
		lvl.setMap(x, y, Blocks.JUMP);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = x;
	}
	
	public void setLocation(int x, int y, Level level) {
		// reset old location
		level.setMap(this.x, this.y, Blocks.Empty);
		
		this.x = x;
		this.y = x;

		// set old location
		setMap(level);	
	}
	
	public JumpEvent getJumpEvent() {
		return event;
	}

}
