package de.kbgame.util;

import java.awt.Point;
import java.util.ArrayList;

import de.kbgame.game.Game;
import de.kbgame.game.Player;

@SuppressWarnings("serial")
public class CaveEnemyShotCollection extends ShotCollection {
	
	private int maxTimeOffset = 0;
	private int maxTimeCounter = 0;
	private ArrayList<Shot> shotBackup = new ArrayList<Shot>();

	public CaveEnemyShotCollection(int updateInterval, int velocity, Point origin) {
		super(updateInterval, velocity, origin);
	}

	public CaveEnemyShotCollection(int updateInterval, int velocity, Point origin, Player player) {
		super(updateInterval, velocity, origin, player);
	}
	
	@Override
	public boolean add(Shot s) {
		if (s.getTimeOffset() > maxTimeOffset) {
			maxTimeOffset = s.getTimeOffset();
			maxTimeCounter = maxTimeOffset;
		}
		
		shotBackup.add(new Shot(s));
		
		return super.add(s);
	}
	
	@Override
	public void update(Game g) {
		long currentTime = System.currentTimeMillis();
		
		if (currentTime > nextUpdate) {
			maxTimeCounter--;
			
			if (maxTimeCounter <= 0) {
				maxTimeCounter = maxTimeOffset;
				insertShots();
			}
		}
		
		super.update(g);
	}
	
	private void insertShots() {
		for (Shot shot : shotBackup) {
			super.add(new Shot(shot));
		}
	}

}
