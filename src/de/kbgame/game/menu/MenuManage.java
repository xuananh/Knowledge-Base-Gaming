package de.kbgame.game.menu;

import de.kbgame.game.Game;

public class MenuManage {

	private final HauptMenu hauptMenu = new HauptMenu();
	private final GoalMenu goalMenu = new GoalMenu();
	private final DeadMenu deadMenu = new DeadMenu();
	
	public void update(Game g) {
		switch (g.state) {
		case MENU:
			hauptMenu.update(g);
			break;
		case PAUSE:
			hauptMenu.update(g);
			break;
		case GOAL:
			goalMenu.update(g);
			break;
		case DEAD:
			deadMenu.update(g);
			break;
		default:
			break;
		}
	}
	
	public void draw(Game g) {
		switch (g.state) {
		case MENU:
			hauptMenu.draw(g);
			break;
		case PAUSE:
			hauptMenu.draw(g);
			break;
		case GOAL:
			goalMenu.draw(g);
			break;
		case DEAD:
			deadMenu.draw(g);
			break;
		default:
			break;
		}
	}
}
