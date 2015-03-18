package de.kbgame.game.menu;

import java.util.HashMap;
import java.util.Map;

import de.kbgame.game.Game;
import de.kbgame.util.GameState;

public class MenuManage {
	
	private final Map<GameState, Menu> menuMap = new HashMap<GameState, Menu>();
	
	public MenuManage() {
		menuMap.put(GameState.MENU, new HauptMenu());
		menuMap.put(GameState.GOAL, new GoalMenu());
		menuMap.put(GameState.DEAD, new DeadMenu());
		menuMap.put(GameState.WAIT, new WaitMenu());
	}

	public void update(Game g) {
		menuMap.get(g.state).update(g);
	}
	
	public void draw(Game g) {
		menuMap.get(g.state).draw(g);
	}
}
