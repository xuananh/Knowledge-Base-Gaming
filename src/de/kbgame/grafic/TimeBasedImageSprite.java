package de.kbgame.grafic;


public class TimeBasedImageSprite extends ImageSprite {
	
	private int updateInterval;
	private long nextUpdate = 0;

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
