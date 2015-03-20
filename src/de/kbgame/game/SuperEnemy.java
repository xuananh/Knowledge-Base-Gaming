package de.kbgame.game;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import de.kbgame.geometry.ImageKey;
import de.kbgame.grafic.ImageLoader;
import de.kbgame.grafic.ImageSprite;
import de.kbgame.map.Level;
import de.kbgame.util.Physic;
import de.kbgame.util.PhysicResult;
import de.kbgame.util.Shot;
import de.kbgame.util.ShotCollection;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;
import de.kbgame.util.sound.SoundKey;

public class SuperEnemy extends Enemy {

	public int hearts = 5;
	private final BufferedImage heart;
	
	private ShotCollection shots;
	public boolean activated = true;
	private final ArrayList<PredicateASP> pres;

	public SuperEnemy(int x, int y, int width, int height, Player player, Game g) {
		super(x, y, width, height,new ImageSprite("Images/endboss.jpg", 1, 1));

		heart = ImageLoader.getInstance().getImageByKey(ImageKey.HUD_HEART);
		shots = new ShotCollection(20, 5, new Point(x, y), player);

		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/encoding/schuesse-v0.1.txt";
		params[2] = "1";

		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
		pres = (ArrayList<PredicateASP>) a
				.getPreListFromString("schuss");

		for (PredicateASP p : pres) {
			shots.add(new Shot((Integer) p.getParameterOfIndex(1), (Integer) p.getParameterOfIndex(0), shots));
		}
	}

	public void update(Game g) {
		if (activated) {
			if (g.player.x>shots.origin.x-8*Level.BLOCK_WIDTH) {
				if(shots.size() == 0){
					for (PredicateASP p : pres) {
						shots.add(new Shot((Integer) p.getParameterOfIndex(1), (Integer) p.getParameterOfIndex(0), shots));
					}
				}
			}
			
			vx += (facing) ? 1 : -1;
			if (onground) {
				vy = -3;
			}
			
			PhysicResult pr = Physic.doPhysic(g, this);
			pr.apply(this);
			setSprites(pr);

			if (!facing && pr.left) {
				facing = true;
			} else if (facing && pr.right) {
				facing = false;
			}
			shots.origin.x = x;
			shots.origin.y = y;
			shots.update(g);
		}
	}

	public void draw(Game g) {
		if (activated) {
			super.draw(g);
			shots.draw(g);
			drawHeart(g);
		}
	}
	
	private void drawHeart(Game g) {
		int x = g.graphic.Width - Level.BLOCK_WIDTH + 10;
		
		for (int i = 0; i < hearts; i++) {
			g.graphic.drawImage(heart, x, Level.BLOCK_HEIGHT-10, Level.BLOCK_WIDTH, Level.BLOCK_HEIGHT, 0, false);
			x -= Level.BLOCK_WIDTH;
		}
	}
	
	public void kill(Game g) {
		g.sounds.sound(SoundKey.ENEMY_HIT);
		if (--hearts <= 0) {
			super.kill(g);
			g.endboss = false;
		}
	}
}