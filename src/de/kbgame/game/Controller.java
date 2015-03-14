package de.kbgame.game;

import java.awt.event.KeyEvent;

import de.kbgame.util.GameState;
import de.kbgame.util.Physic;

public class Controller {
	
	public int viewX, viewY;
	
	public Entity controlledOne = null;
	
	public void control(Entity r){
		if (controlledOne != null) controlledOne.con = null;
		controlledOne = r;
		viewX = controlledOne.x;
		viewY = controlledOne.y;
		r.con = this;
	}
	
	public void update(Game g){
		if (controlledOne != null){
			viewX = controlledOne.x;
			int a = controlledOne.y - viewY;
			
			if(a > g.graphic.Height/4) {
				viewY += a-g.graphic.Height/4;
			}
			
			if(a < -g.graphic.Height/4) {
				viewY -= Math.abs(a+g.graphic.Height/4);
			}
			
			if(viewX < g.graphic.Width / 2) {
				viewX = g.graphic.Width/2;
			}
			
			if (g.input.getKey(KeyEvent.VK_UP)){
				if (controlledOne.onground) {
					controlledOne.jump(g);
				} else if (controlledOne instanceof Player && ((Player) controlledOne).parent != null) {
					((Player) controlledOne).setParent(null);
					controlledOne.jump(g);
				}
			} /*else if (g.input.getKey(KeyEvent.VK_DOWN)){
				controlledOne.vy += 2;
			}*/

			if (g.input.getKey(KeyEvent.VK_LEFT)) {
				controlledOne.vx -= Physic.WALK_VELOCITY;
			} else if (g.input.getKey(KeyEvent.VK_RIGHT)) {
				controlledOne.vx += Physic.WALK_VELOCITY;
			}
		}
		
		if (g.input.getKey(KeyEvent.VK_ESCAPE)) {
			g.state = GameState.PAUSE;
			g.input.resetKeys();
		}
	}

}
