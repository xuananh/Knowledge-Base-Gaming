package de.kbgame.util.hud;

import java.awt.Color;
import java.awt.Font;

import de.kbgame.game.Game;
import de.kbgame.game.Player;
import de.kbgame.map.Level;

public class Points implements HUDElement {

	public int points = 0;

	public final static int OFFSET_LEFT = Level.BLOCK_WIDTH / 5;
	public final static int OFFSET_TOP = Level.BLOCK_HEIGHT / 5;

	public final static int COIN_SCORE = 50;

	//	private BufferedImage img;

	public Points(int points) {
		this.points = points;
		//img = ImageLoader.getInstance().getImageByKey(ImageKey.HUD_HEART);
	}

	public void add(int plus, Player player) {
		points += plus;
		// TODO: Punktezahl festlegen
		while (points >= 200) {
			points = points - 200;
			player.lifes.add();
		}
	}

	public void draw(Game g) {
		g.graphic.drawText("POINTS: " + points, 10, 15, Color.blue, false, new Font("Arial", Font.BOLD, 14));
	}

}
