package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.kbgame.game.Game;
import de.kbgame.util.GameState;

public class DeadMenu extends Menu{

	private static final int X_START = 300;
	private static final int Y_START = 300;
	
	/** variable sichert dass das Score nur 1 mal gespeichert wird */
	private boolean isSavedScore = false;
	/** Submenue */
	private final ScoreMenu scoreMenu = new ScoreMenu(this);

	/*
	 * (non-Javadoc)
	 * @see de.kbgame.game.menu.Menu#updateMenu(de.kbgame.game.Game)
	 */
	@Override
	protected void updateMenu(Game g) {
		switch (menu) {
		case HAUPT_MENU:
			deadMenuUpdate(g);
			break;
		case HIGHT_SCORE:
			scoreMenu.update(g);
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see de.kbgame.game.menu.Menu#draw(de.kbgame.game.Game)
	 */
	@Override
	public void draw(Game g) {
		switch (menu) {
		case HAUPT_MENU:
			deadMenuDraw(g);
			break;
		case HIGHT_SCORE:
			scoreMenu.draw(g);
			break;
		default:
			break;
		}
	}

	/**
	 * Hilfmethode fuer draw(Game)-Methode
	 * @param g Game Instance
	 */
	private void deadMenuDraw(Game g) {
		final int score = g.player.getPoint(); 
		if(!isSavedScore) {
			saveHightScore(score);
			isSavedScore = true;
		}
		gameLogo(10, 50, g, 0.5f);
		g.graphic.drawText("Game Over!!", X_START - 30, Y_START - 150, new Color(187,255,255), false, new Font("Comic Sans MS", Font.BOLD, 50));
		
		g.graphic.drawText("Score: " + score, X_START - 20, Y_START - 100, new Color(187,255,255), false, new Font("Comic Sans MS", Font.BOLD, 30));
		
		g.graphic.drawText("Restart Level", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("Hight Score", X_START, Y_START + 50, (menuPunkt == 2) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
		g.graphic.drawText("Exit game", X_START, Y_START + 100, (menuPunkt == 3) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 3) ? 35 : 30));

		int x = X_START - 30;
		int y = Y_START - 10;
		switch (menuPunkt) {
		case 2:
			y = y + 50;
			break;
		case 3:
			y = y + 100;
			break;
		default:
			break;
		}
		g.graphic.drawImage(sprite.getCurrent(), x, y, 30, 30, 0, false);
	}
	
	/**
	 * Hilfmethode fuer update(Game)-Methode
	 * @param g Game Instance
	 */
	private void deadMenuUpdate(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP)){
			menuPunkt = (menuPunkt == 1) ? 3 : menuPunkt -1;
		}
		
		if (g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 3) ? 1 : menuPunkt +1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			isSavedScore = false;
			switch (menuPunkt) {
			case 1:
				g.state = GameState.GAME;
				g.newGame();
				break;
			case 2:
				menu = HIGHT_SCORE;
				break;
			case 3:
				g.shouldApplicationExit = true;
				break;
			default:
				break;
			}
		}
	}
}
