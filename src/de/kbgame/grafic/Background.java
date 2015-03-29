package de.kbgame.grafic;

import java.awt.image.BufferedImage;

import de.kbgame.game.Game;
import de.kbgame.geometry.ImageKey;

public class Background {

	public float xFactor = 1;
	public float yFactor = 0;
	public BufferedImage background = null;
	private int width;
	private int height;
	private float targetWidth;
	
	/**
	 * Die Klasse zeichnet Hintergrundbilder abhängig von der Spielerposition. Dabei kann die
	 * Position des Hintergrundbilders mittels Faktoren beeinflusst werden.
	 * 
	 * @param img identifiziert das zu verwendende Hintergrundbild
	 * @param xFactor beschleunigt/verzögert die Bewegung des Hintergrundbildes in x Richtung
	 * @param yFactor beschleunigt/verzögert die Bewegung des Hintergrundbildes in y Richtung
	 * @param g Referenz auf das Spiel
	 */
	public Background(ImageKey img, float xFactor, float yFactor, Game g) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		
		background = imageLoader.getImageByKey(img);
		
		float scale = background.getHeight() / (float) g.graphic.currentWindowSize.height;
		
		width = (int) (background.getWidth() / scale);
		height = g.graphic.currentWindowSize.height;
		
		targetWidth = width / xFactor;
			
		this.xFactor = xFactor;
		this.yFactor = yFactor;
	}
	
	/**
	 * Die Methode zeichnet das Hintergrundbild an die gewünschte Position. Das Bild wird mehrmals gezeichnet (Kachelung).
	 * 
	 * @param g
	 */
	public void draw(Game g) {
		if (background != null) {			
			int start = (int) (g.graphic.viewX / targetWidth);
			// TODO
//			int end = (int) ((g.graphic.viewX + g.graphic.currentWindowSize.width + xFactor * this.width) / this.targetWidth);
			
			for (int i = start; i <= start + 1; i++) {
				g.graphic.currentGrafic.drawImage(background, (int) (i * this.width + g.graphic.viewX * -xFactor), 0, width, height, null);
			}
		}
	}
	
}
