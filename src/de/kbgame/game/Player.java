package de.kbgame.game;

import de.kbgame.util.ImageKey;
import de.kbgame.util.Physic;

public class Player extends Entity{
	
	public float rot = 0;

	public Player(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void update(Game g){
		Physic.doPhysic(g,this);
	}
	
	@Override
	public void draw(Game g){
		g.graphic.drawImage(ImageKey.PERSON, x, y, wi, hi, rot);
	}

}
