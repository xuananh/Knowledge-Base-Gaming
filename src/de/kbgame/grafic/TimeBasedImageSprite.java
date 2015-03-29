package de.kbgame.grafic;


public class TimeBasedImageSprite extends ImageSprite {
	
	private int updateInterval;
	private long nextUpdate = 0;
	
	/**
	 * @param imagePath der Pfad zum Bild, welches die Spriteanimation enthält
	 * @param rows die Anzahl an Zeilen
	 * @param cols die Anzahl an Spalten
	 * @param updateInterval ein Zeitintervall in Millisekunden nach welchem das aktuelle Bild getauscht werden soll
	 */
	public TimeBasedImageSprite(String imagePath, int rows, int cols, int updateInterval) {
		super(imagePath, rows, cols);
		
		this.updateInterval = updateInterval;
	}

	@Override
	public void update() {
		if (System.currentTimeMillis() > nextUpdate) {
			super.update();
			nextUpdate = System.currentTimeMillis() + updateInterval;
		}
	}
}
