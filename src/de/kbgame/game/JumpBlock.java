package de.kbgame.game;

import de.kbgame.map.Blocks;
import de.kbgame.map.Level;

public class JumpBlock {
	
	private int x, y;
	
	public JumpBlock(int x, int y, Level lvl) {
		this.x = x;
		this.y = y;
		
		lvl.setMap(x, y, Blocks.JUMP);
	}

}
