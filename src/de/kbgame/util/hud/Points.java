package de.kbgame.util.hud;


import java.awt.Color;
import java.awt.image.BufferedImage;

import de.kbgame.game.Game;
import de.kbgame.game.Player;
import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.ImageLoader;
import de.kbgame.map.Level;

public class Points implements HUDElement {

	public int points = 0;

	public final static int OFFSET_LEFT = Level.BLOCK_WIDTH / 5;
	public final static int OFFSET_TOP = Level.BLOCK_HEIGHT / 5;

	private BufferedImage img;

	public Points(int points) {
		this.points = points;
		//img = ImageLoader.getInstance().getImageByKey(ImageKey.HUD_HEART);
	}
	
	public void add(int plus, Player player) {
		points+=plus;
		// TODO: Punktezahl festlegen
		while (points>=200) {
			points = points-200;
			player.lifes.add(1,player);
		}
	}
	

	public void draw(Game g) {
		int offsetLeft;
	//	g.graphic.currentGrafic.drawText("hello World", 10, 10, Color.RED);
		//g.graphic.currentGrafic.drawImage(img, offsetLeft, OFFSET_TOP, Level.BLOCK_HEIGHT, Level.BLOCK_HEIGHT, null);
		g.graphic.currentGrafic.drawString("POINTS: " + points, 3, 12);
	}

}
