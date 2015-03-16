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

public class GoalMenu extends Menu{

	private static final int X_START = 300;
	private static final int Y_START = 300;

	@Override
	protected void updateMenu(Game g) {
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
				g.newGame();
				break;
			case 2:
				g.state = GameState.GAME;
				g.newGame();
				break;
			case 3:
				g.shouldApplicationExit = true;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void draw(Game g) {
		try {
			BufferedImage image = ImageIO.read(new File("Images/menu_background.png"));
			g.graphic.currentGrafic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
			g.graphic.currentGrafic.drawImage(image, 0, 0, g.graphic.Width, g.graphic.Height, null);
			g.graphic.currentGrafic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		} catch (IOException e) {
			e.printStackTrace();
		}
		gameLogo(10, 50, g, 0.5f);
		g.graphic.drawText("Victory! Congratulate!", X_START - 150, Y_START - 150, Color.magenta, false, new Font("Comic Sans MS", Font.BOLD, 50));
		
		g.graphic.drawText("Score: ", X_START - 20, Y_START - 100, Color.red, false, new Font("Comic Sans MS", Font.BOLD, 30));
		
		g.graphic.drawText("Next Level", X_START, Y_START, (menuPunkt == 1) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 1) ? 35 : 30));
		g.graphic.drawText("Restart Level", X_START, Y_START+50, (menuPunkt == 2) ? Color.green : new Color(255,69,0), false, new Font("Comic Sans MS", Font.BOLD, (menuPunkt == 2) ? 35 : 30));
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

}
