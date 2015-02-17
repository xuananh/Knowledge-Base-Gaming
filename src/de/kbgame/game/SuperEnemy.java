package de.kbgame.game;

import java.awt.Point;
import java.util.ArrayList;

import de.kbgame.util.Shot;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;

public class SuperEnemy extends Enemy {

	private ShotCollection shots;
	public boolean activated = false;

	public SuperEnemy(int x, int y, int width, int height, Player player) {
		super(x, y, width, height);

		shots = new ShotCollection(20, 5, player, new Point(x, y));

		String[] params = new String[3];
		params[0] = "/opt/local/bin/clingo";
		params[1] = "clingo/encoding/schuesse-v0.1.txt";
		params[2] = "1";

		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
		ArrayList<PredicateASP> pres = (ArrayList<PredicateASP>) a
				.getPreListFromString("schuss");
		for (PredicateASP p : pres) {
			System.out.println(p.toString());
		}

		for (PredicateASP p : pres) {
			shots.add(new Shot((int) p.getParameterOfIndex(1), (int) p.getParameterOfIndex(0), shots));
		}

		// fixed dummy shots
		// shots.add(new Shot(1, 190, shots));
		// shots.add(new Shot(2, 30, shots));
		// shots.add(new Shot(4, 10, shots));
		// shots.add(new Shot(5, 100, shots));
		// shots.add(new Shot(9, 10, shots));
		// shots.add(new Shot(10, 80, shots));
	}

	public void update(Game g) {
		if (activated) {
			super.update(g);
			shots.origin.x = x;
			shots.origin.y = y;
			shots.update(g);
		}
	}

	public void draw(Game g) {
		// if (activated) {
		super.draw(g);
		shots.draw(g);
		// }
	}
}