package de.kbgame.game.level;

import java.awt.Point;
import java.util.ArrayList;

import de.kbgame.game.Game;
import de.kbgame.game.JumpBlock;
import de.kbgame.game.JumpEvent;
import de.kbgame.game.UpdateDraw;
import de.kbgame.geometry.MyPoint;
import de.kbgame.map.Level;
import de.kbgame.map.MapLoader;
import de.kbgame.util.CaveEnemyShotCollection;
import de.kbgame.util.Shot;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.XValueObserver;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;

public class CaveEnemySegment extends LevelSegment implements UpdateDraw, XValueObserver {

	private final static Point shotOrigin = new Point(24 * Level.BLOCK_WIDTH, 29 * Level.BLOCK_HEIGHT);
	private final static int observerXValue = 6 * Level.BLOCK_WIDTH;

	private ShotCollection shots;
	private boolean active = false;

	public CaveEnemySegment(Game g, String[] args) {
		super(g);

		goal = new Point(-1, -1);

		map = MapLoader.loadFromBitmap(this, args[0], goal);
		width = map.length;
		height = map[0].length;

		JumpEvent jEvent = new JumpEvent(-0.4, -20, 100, new MyPoint(10 * 50, 28 * 50));
		JumpBlock jumper = new JumpBlock(1, height - 2, jEvent);
		jumpBlocks.add(jumper);

		if (goal.x < 0) {
			goal = null;
		}

		initShots();
		
		observedValues = new int[1];
		observedValues[0] = observerXValue;
		
		g.gameElements.add(this);
	}

	private void initShots() {
		shots = new CaveEnemyShotCollection(24, 6, shotOrigin);

		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/encoding/schuesse-v0.1-beta.txt";
		params[2] = "1";

		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
		ArrayList<PredicateASP> pres = (ArrayList<PredicateASP>) a.getPreListFromString("schuss");

		for (PredicateASP p : pres) {
			shots.add(new Shot((Integer) p.getParameterOfIndex(1), (Integer) p.getParameterOfIndex(0), shots));
		}
	}

	public void activate() {
		active = true;
	}

	@Override
	public void update(Game g) {
		if (active) {
			shots.update(g);
		}
	}

	@Override
	public void draw(Game g) {
		if (active) {
			shots.draw(g);
		}
	}

	@Override
	public void notifyObserver(Game g) {
		shots.player = g.player;
		activate();
	}

}
