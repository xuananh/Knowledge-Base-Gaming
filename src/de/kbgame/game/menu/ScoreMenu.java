package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import de.kbgame.game.Game;

public class ScoreMenu extends Menu{

	private static final int X_START = 200;
	private static final int Y_START = 400;
	private final Menu hauptMenu;
	
	private boolean isloadedScore = false;
	private List<Integer> score = new ArrayList<Integer>();
	
	public ScoreMenu(Menu hauptMenu) {
		this.hauptMenu = hauptMenu;
	}

	@Override
	protected void updateMenu(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP) || g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 1) ? 2 : 1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			isloadedScore = false;
			switch (menuPunkt) {
			case 1:
				hauptMenu.menu = HAUPT_MENU;
				break;
			case 2:
				g.shouldApplicationExit = true;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void draw(Game g) {
		if(!isloadedScore) {
			score = getHightScore();
			isloadedScore = true;
		}
		gameLogo(50, 50, g, 0.5f);
		g.graphic.drawText("Hight Score: ", 200, 150, Color.yellow, false, new Font("Arial", Font.PLAIN, 40));
		g.graphic.drawText("1st: 	" + score.get(0), 250, 200, Color.yellow, false, new Font("Arial", Font.PLAIN, 30));
		g.graphic.drawText("2nd: 	" + score.get(1), 250, 250, Color.yellow, false, new Font("Arial", Font.PLAIN, 30));
		g.graphic.drawText("3rd: 	" + score.get(2), 250, 300, Color.yellow, false, new Font("Arial", Font.PLAIN, 30));
		
		g.graphic.drawText("back", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("Exit game", X_START, Y_START + 50, (menuPunkt == 2) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
		int x = X_START - 30;
		int y = Y_START - 10;
		
		if(menuPunkt == 2) {
			y = y + 50;
		}
		g.graphic.drawImage(sprite.getCurrent(), x, y, 30, 30, 0, false);
	}

}
