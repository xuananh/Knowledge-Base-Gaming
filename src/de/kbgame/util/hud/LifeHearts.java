package de.kbgame.util.hud;

import java.awt.image.BufferedImage;

import de.kbgame.game.Game;
import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.ImageLoader;
import de.kbgame.map.Level;

public class LifeHearts implements HUDElement {

	public int hearts = 2;

	public final static int OFFSET_LEFT = Level.BLOCK_WIDTH / 5;
	public final static int OFFSET_TOP = Level.BLOCK_HEIGHT / 5;

	private BufferedImage img;

	/**
	 * Kontruktor legt Herzanzahl am Anfang fest 
	 * @param hearts Herzanzahl
	 */
	public LifeHearts(int hearts) {
		this.hearts = hearts;
		img = ImageLoader.getInstance().getImageByKey(ImageKey.HUD_HEART);
	}

	/**
	 * Herzanzahl wird um 1 erhoeht
	 */
	public void add() {
		hearts++;
	}

	/**
	 * Zeichnet Herzen im oberen-linken Bereich des Bildschirms. Eine Zeile besteht aus 6 Herzen.
	 * 
	 * @param g die Game Instanz
	 * @see de.kbgame.util.hud.HUDElement#draw(de.kbgame.game.Game)
	 */
	public void draw(Game g) {
		int x = Level.BLOCK_WIDTH - 10;
		int y = Level.BLOCK_HEIGHT - 10;

		for (int i = 0; i < hearts; i++) {
			g.graphic.drawImage(img, x, y, Level.BLOCK_WIDTH, Level.BLOCK_HEIGHT, 0, false);
			
			if (i > 0 && ((i + 1) % 6) == 0) {
				x = Level.BLOCK_WIDTH - 10;
				y = y + Level.BLOCK_HEIGHT;
			} else {
				x += Level.BLOCK_WIDTH;
			}
		}
	}

}
