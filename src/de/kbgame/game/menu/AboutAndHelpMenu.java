package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.kbgame.game.Game;

public class AboutAndHelpMenu extends Menu{
	
	private static final int X_START = 350;
	private static final int Y_START = 350;
	private final Menu hauptMenu;

	public AboutAndHelpMenu(Menu hauptMenu) {
		this.hauptMenu = hauptMenu;
	}

	@Override
	protected void updateMenu(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP) || g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 1) ? 2 : 1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
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
		gameLogo(50, 50, g, 0.5f);
		g.graphic.drawText("About and Help", 50, 150, Color.yellow, false, new Font("Arial", Font.PLAIN, 40));
		g.graphic.drawText("Jump and Run game v.0.0 beta developed by A.T.A", 100, 200, Color.yellow, false, new Font("Arial", Font.PLAIN, 30));
		g.graphic.drawText("Have fun! Thanks for joining us! ", 100, 250, Color.yellow, false, new Font("Arial", Font.PLAIN, 30));
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
