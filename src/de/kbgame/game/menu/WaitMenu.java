package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;

import de.kbgame.game.Game;

public class WaitMenu extends Menu{
	
	private static final int X_START = 200;
	private static final int Y_START = 200;
	private static final int X_END = 600;
	private int x = X_START;
	
	/*
	 * (non-Javadoc)
	 * @see de.kbgame.game.menu.Menu#updateMenu(de.kbgame.game.Game)
	 */
	@Override
	protected void updateMenu(Game g) {
		x = (x > X_END) ? X_START : x + 5;
	}

	/*
	 * (non-Javadoc)
	 * @see de.kbgame.game.menu.Menu#draw(de.kbgame.game.Game)
	 */
	@Override
	public void draw(Game g) {
		menuBackground(g);
		gameLogo(10, 50, g, 0.5f);
		drawWating(x, g);
		drawWating(x + 50, g);
		drawWating(x + 100, g);
		drawWating(x + 150, g);
		drawWating(x + 200, g);
		g.graphic.drawText("Wating", X_START + 150, Y_START + 100, Color.green, false, new Font("Arial", Font.BOLD, 35));
	}

	/**
	 * Zeichen laufende Avatar
	 * @param x x-koordination von startenden Punkt zu zeichnen. 
	 * @param g Game Instance
	 */
	private void drawWating(int x, Game g) {
		if(x < X_END) {
			g.graphic.drawImage(sprite.getCurrent(), x, Y_START, 30, 30, 0, false);
		}
	}
}
