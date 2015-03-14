package de.kbgame.game.menu;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.kbgame.game.Game;
import de.kbgame.util.GameState;

public class HauptMenu extends Menu{
	
	private static final int X_START = 250;
	private static final int Y_START = 250;
	private final AboutAndHelpMenu aboutAndHelp = new AboutAndHelpMenu(this);
	
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
		case SUB_MENU:
			aboutAndHelp.update(g);
			break;
		default:
			break;
		}
	}

	@Override
	public void draw(Game g) {
		if(!isPauseMenu) {
			try {
				BufferedImage image = ImageIO.read(new File("Images/menu_background.png"));
				g.graphic.currentGrafic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
				g.graphic.currentGrafic.drawImage(image, 0, 0, g.graphic.Width, g.graphic.Height, null);
				g.graphic.currentGrafic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		switch (menu) {
		case HAUPT_MENU:
			if(isPauseMenu) {
				pauseMenu(g);
			} else {
				normalMenu(g);
			}
			break;
		case SUB_MENU:
			aboutAndHelp.draw(g);
			break;
		default:
			break;
		}
	}
	
	private void normalMenu(Game g) {
		gameLogo(X_START - 100, Y_START - 100, g, 1.2f);
		
		g.graphic.drawText("Start new game", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("About & Help", X_START, Y_START + 50, (menuPunkt == 2) ? Color.green : new Color(	255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
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
		g.graphic.drawImage(sprite.getSprite(), x, y, 30, 30, 0, false);
	}
	
	private void updateNormalMenu(Game g) {
		if (g.input.getKey(KeyEvent.VK_UP)){
			menuPunkt = (menuPunkt == 1) ? 3 : menuPunkt -1;
		}
		
		if (g.input.getKey(KeyEvent.VK_DOWN)){
			menuPunkt = (menuPunkt == 3) ? 1 : menuPunkt +1;
		}
		
		if (g.input.getKey(KeyEvent.VK_ENTER)){
			switch (menuPunkt) {
			case 1:
				g.state = GameState.GAME;
				isPauseMenu = true;
				break;
			case 2:
				menu = SUB_MENU;
				break;
			case 3:
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
		g.graphic.drawImage(sprite.getSprite(), x, y, 30, 30, 0, false);
	}
	
	private void updatePauseMenu(Game g) {
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
				isPauseMenu = true;
				break;
			case 2:
				g.state = GameState.GAME;
				isPauseMenu = true;
				break;
			case 3:
				menu = SUB_MENU;
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
