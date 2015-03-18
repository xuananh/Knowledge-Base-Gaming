package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.kbgame.game.Game;
import de.kbgame.util.GameState;

public class HauptMenu extends Menu{
	
	private static final int X_START = 250;
	private static final int Y_START = 250;
	private final AboutAndHelpMenu aboutAndHelp = new AboutAndHelpMenu(this);
	private final ScoreMenu scoreMenu = new ScoreMenu(this);
	
	private boolean isPauseMenu = false;

	@Override
	protected void updateMenu(Game g) {
		switch (menu) {
		case HAUPT_MENU:
			if(isPauseMenu) {
				updatePauseMenu(g);
			} else {
				updateNormalMenu(g);
			}
			break;
		case HIGHT_SCORE:
			scoreMenu.update(g);
			break;
		case ABOUT_MENU:
			aboutAndHelp.update(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void draw(Game g) {
		if(!isPauseMenu) {
			menuBackground(g);
		}
		switch (menu) {
		case HAUPT_MENU:
			if(isPauseMenu) {
				pauseMenu(g);
			} else {
				normalMenu(g);
			}
			break;
		case HIGHT_SCORE:
			scoreMenu.draw(g);
			break;
		case ABOUT_MENU:
			aboutAndHelp.draw(g);
			break;
		default:
			break;
		}
	}
	
	private void normalMenu(Game g) {
		gameLogo(X_START - 100, Y_START - 100, g, 1.2f);
		
		g.graphic.drawText("Start new game", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("Hight Score", X_START, Y_START + 50, (menuPunkt == 2) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
		g.graphic.drawText("About & Help", X_START, Y_START + 100, (menuPunkt == 3) ? Color.green : new Color(	255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 3) ? 35 : 30));
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
	
	private void updateNormalMenu(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP)){
			menuPunkt = (menuPunkt == 1) ? 4 : menuPunkt -1;
		}
		
		if (g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 4) ? 1 : menuPunkt +1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			switch (menuPunkt) {
			case 1:
				g.state = GameState.GAME;
				g.newGame();
				isPauseMenu = true;
				break;
			case 2:
				menu = HIGHT_SCORE;
				break;
			case 3:
				menu = ABOUT_MENU;
				break;
			case 4:
				g.shouldApplicationExit = true;
				break;
			default:
				break;
			}
		}
	}
	
	private void pauseMenu(Game g) {
		gameLogo(X_START - 100, Y_START - 100, g, 1.2f);
		
		g.graphic.drawText("Resume game", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("Start new game", X_START, Y_START + 50, (menuPunkt == 2) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
		g.graphic.drawText("Hight Score", X_START, Y_START + 100, (menuPunkt == 3) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 3) ? 35 : 30));
		g.graphic.drawText("About & Help", X_START, Y_START + 150, (menuPunkt == 4) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 4) ? 35 : 30));
		g.graphic.drawText("Exit game", X_START, Y_START + 200, (menuPunkt == 5) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 5) ? 35 : 30));

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
		case 5:
			y = y + 200;
			break;
		default:
			break;
		}
		g.graphic.drawImage(sprite.getCurrent(), x, y, 30, 30, 0, false);
	}
	
	private void updatePauseMenu(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP)){
			menuPunkt = (menuPunkt == 1) ? 5 : menuPunkt -1;
		}
		
		if (g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 5) ? 1 : menuPunkt +1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			switch (menuPunkt) {
			case 1:
				g.state = GameState.GAME;
				isPauseMenu = true;
				break;
			case 2:
				g.state = GameState.GAME;
				g.newGame();
				isPauseMenu = true;
				break;
			case 3:
				menu = HIGHT_SCORE;
				break;
			case 4:
				menu = ABOUT_MENU;
				break;
			case 5:
				g.shouldApplicationExit = true;
				break;
			default:
				break;
			}
		}
	}
}
