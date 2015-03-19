package de.kbgame.game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.kbgame.map.Level;
import de.kbgame.util.Shot;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;

public class SuperEnemy extends Enemy {

	private ShotCollection shots;
	public boolean activated = true;
	private final ArrayList<PredicateASP> pres;
	private BufferedImage endboss;

	public SuperEnemy(int x, int y, int width, int height, Player player) {
		super(x, y, width, height);

		shots = new ShotCollection(20, 5, new Point(x, y), player);

		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/encoding/schuesse-v0.1.txt";
		params[2] = "1";

		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
		pres = (ArrayList<PredicateASP>) a
				.getPreListFromString("schuss");
		for (PredicateASP p : pres) {
			System.out.println(p.toString());
		}

		for (PredicateASP p : pres) {
			shots.add(new Shot((Integer) p.getParameterOfIndex(1), (Integer) p.getParameterOfIndex(0), shots));
		}
		
		try {
			endboss = ImageIO.read(new File("Images/endboss.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// fixed dummy shots
//		 shots.add(new Shot(1, 10, shots));
//		 shots.add(new Shot(2, 10, shots));
//		 shots.add(new Shot(4, 10, shots));
//		 shots.add(new Shot(5, 100, shots));
//		 shots.add(new Shot(9, 10, shots));
//		 shots.add(new Shot(10, 80, shots));
	}

	public void update(Game g) {
		if (activated) {
//			if (g.player.x>shots.origin.x-8*Level.BLOCK_WIDTH) {
				if(shots.size() == 0){
					for (PredicateASP p : pres) {
						shots.add(new Shot((Integer) p.getParameterOfIndex(1), (Integer) p.getParameterOfIndex(0), shots));
					}
				}
//			}
			super.update(g);
			shots.origin.x = x;
			shots.origin.y = y;
			shots.update(g);
		}
	}

	public void draw(Game g) {
		if (activated) {
			g.graphic.drawImage(endboss, x, y, width, height, rot, true);
			shots.draw(g);
		}
	}
}