package de.kbgame.util.hud;

import java.awt.image.BufferedImage;

import de.kbgame.game.Game;
import de.kbgame.game.Player;
import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.ImageLoader;
import de.kbgame.map.Level;

public class LifeHearts implements HUDElement {

	public int hearts = 2;

	public final static int OFFSET_LEFT = Level.BLOCK_WIDTH / 5;
	public final static int OFFSET_TOP = Level.BLOCK_HEIGHT / 5;

	private BufferedImage img;

	public LifeHearts(int hearts) {
		this.hearts = hearts;
		img = ImageLoader.getInstance().getImageByKey(ImageKey.HUD_HEART);
	}

	public void add(int s, Player player) {
		hearts = hearts+1;
		
	}

	public void draw(Game g) {
		int offsetLeft;

		for (int i = 0; i < hearts; i++) {
			offsetLeft = ((i + 1) * OFFSET_LEFT) + i * Level.BLOCK_WIDTH;

			g.graphic.currentGrafic.drawImage(img, offsetLeft, OFFSET_TOP, Level.BLOCK_HEIGHT, Level.BLOCK_HEIGHT, null);
		}
	}

}
