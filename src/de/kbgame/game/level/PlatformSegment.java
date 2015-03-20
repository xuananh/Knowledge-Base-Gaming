package de.kbgame.game.level;

import java.awt.Point;
import java.util.ArrayList;

import de.kbgame.game.Game;
import de.kbgame.game.Platform;
import de.kbgame.map.Level;
import de.kbgame.map.MapLoader;
import de.kbgame.util.clingo.AnswerASP;
import de.kbgame.util.clingo.ClingoFactory;
import de.kbgame.util.clingo.PredicateASP;

public class PlatformSegment extends LevelSegment {

	public PlatformSegment(Game g, String[] args) {
		super(g);
		
		goal = new Point(-1,-1);
		
		map = MapLoader.loadFromBitmap(this, args[0], goal);
		width = map.length;
		height = map[0].length;
		
		if (goal.x < 0) {
			goal = null;
		}
		
		

		String[] params = new String[7];
		params[0] = "clingo";
		//params[1] = "clingo/encoding/platforms.txt";
		//params[1] = "clingo/encoding/testlines-up.txt";
		params[1] = "clingo/encoding/platforms-light.txt";
		params[2] = "1";
		params[3] = "-c startx="+args[1]; // startx
		params[4] = "-c starty="+args[2];
		params[5] = "-c destx="+args[3];
		params[6] = "-c desty="+args[4];
		
		
		

		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
		ArrayList<PredicateASP> pres = (ArrayList<PredicateASP>) a
				.getPreListFromString("platform");
		for (PredicateASP p : pres) {
			System.out.println(p.toString());
		}

		

		boolean vertical = false;
		for (PredicateASP p : pres) {
			if ((Integer) p.getParameterOfIndex(4) == 1) {
				vertical = true;
			} else {
				vertical = false;
			}
			
			Platform pl = new Platform(g,
										(Integer) p.getParameterOfIndex(0)*Level.BLOCK_WIDTH,  //x
										(Integer) p.getParameterOfIndex(1)*Level.BLOCK_HEIGHT, //y
										(Integer) p.getParameterOfIndex(2)*Level.BLOCK_WIDTH,						//width
										1*Level.BLOCK_HEIGHT,														// height
										(Integer) p.getParameterOfIndex(1),						//Block-from
										(Integer) p.getParameterOfIndex(1)+(Integer) p.getParameterOfIndex(3),		//Block-from
										vertical
									);
											
			platforms.add(pl);
			//Platform	
		}	
	}

}