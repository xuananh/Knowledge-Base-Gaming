package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.kbgame.game.Game;
import de.kbgame.util.GameState;

public class GoalMenu extends Menu{

	private static final int X_START = 300;
	private static final int Y_START = 300;
	private boolean isSavedScore = false;
	
	private final ScoreMenu scoreMenu = new ScoreMenu(this);

	@Override
	protected void updateMenu(Game g) {
		switch (menu) {
		case HAUPT_MENU:
			updateGoalMenu(g);
			break;
		case HIGHT_SCORE:
			scoreMenu.update(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void draw(Game g) {
		menuBackground(g);
		switch (menu) {
		case HAUPT_MENU:
			drawGoalMenu(g);
			break;
		case HIGHT_SCORE:
			scoreMenu.draw(g);
			break;
		default:
			break;
		}
	}

	private void drawGoalMenu(Game g) {
		final int score = g.player.getPoint(); 
		if(!isSavedScore ) {
			saveHightScore(score);
			isSavedScore = true;
		}
		gameLogo(10, 50, g, 0.5f);
		g.graphic.drawText("Level Complete!", X_START - 80, Y_START - 150, Color.orange, false, new Font("Comic Sans MS", Font.BOLD, 50));
		
		g.graphic.drawText("Score: " + g.player.getPoint(), X_START - 20, Y_START - 100, Color.orange, false, new Font("Comic Sans MS", Font.BOLD, 30));
		
		g.graphic.drawText("Next Level", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("Restart Level", X_START, Y_START+50, (menuPunkt == 2) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
		g.graphic.drawText("Hight Score", X_START, Y_START + 100, (menuPunkt == 3) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 3) ? 35 : 30));
		g.graphic.drawText("Exit game", X_START, Y_START + 150, (menuPunkt == 4) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 4) ? 35 : 30));

		int x = X_START - 30;
		int y = Y_START - 10;
		switch (menuPunkt) {
		case 2:
			y = y + 50;
			break;
		case 3:
			y = y + 100;
			break;
		case 4:
			y = y + 150;
			break;
		default:
			break;
		}
		g.graphic.drawImage(sprite.getCurrent(), x, y, 30, 30, 0, false);
	}
	
	private void updateGoalMenu(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP)){
			menuPunkt = (menuPunkt == 1) ? 4 : menuPunkt -1;
		}
		
		if (g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 4) ? 1 : menuPunkt +1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			isSavedScore = false;
			switch (menuPunkt) {
			case 1:
				g.state = GameState.GAME;
				g.nextLevel();
				break;
			case 2:
				g.state = GameState.GAME;
				g.newGame();
				break;
			case 3:
				menu = HIGHT_SCORE;
				break;
			case 4:
				g.shouldApplicationExit = true;
				break;
			default:
				break;
			}
		}
	}
}
