package de.kbgame.game.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;

import de.kbgame.game.Game;
import de.kbgame.grafic.ImageSprite;

public abstract class Menu {
	
	protected static final int HAUPT_MENU = 0;
	protected static final int SUB_MENU = 1;
	
	protected final ImageSprite sprite = new ImageSprite("Images/sprite.png", 4, 3);
	protected int menuPunkt = 1;
	
	protected int menu;
	
	protected abstract void updateMenu(Game g);
	public abstract void draw(Game g);
	
	public void update(Game g) {
		updateMenu(g);
		if (g.input.getKey(KeyEvent.VK_ESCAPE)) 
			g.shouldApplicationExit = true;
		
		sprite.setCurrentRow(2);
		sprite.update();
		g.input.resetKeys();
	}
	
	protected void gameLogo(int xStart, int yStart, Game g, float scale) {
		g.graphic.drawText("JUMP", xStart, yStart, Color.yellow, false, new Font("Comic Sans MS", Font.BOLD, (int) (50*scale)));
		g.graphic.drawText("&", xStart + (int)(150*scale), yStart + (int)(10*scale), new Color(0,191,255), false, new Font("Arial", Font.PLAIN, (int) (80*scale)));
		g.graphic.drawText("RUN", xStart + (int)(220*scale), yStart, Color.orange, false, new Font("Comic Sans MS", Font.BOLD, (int) (50*scale)));
		g.graphic.drawText("GAME", xStart +(int)(350*scale), yStart, new Color(121,205,205), false, new Font("Arial", Font.PLAIN, (int) (40*scale)));
	}
}
