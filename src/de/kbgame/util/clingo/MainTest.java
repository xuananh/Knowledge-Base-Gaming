package de.kbgame.util.clingo;

import de.kbgame.util.sound.SoundThread;

public class MainTest {

	public static void main(String[] args) throws Exception {

		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/encoding/labyrinth-23.lp";
		params[2] = "1";

//		AnswerASP a = ClingoFactory.getInstance().getAnswerASP(params);
//		List<PredicateASP> pres = a.getPreListFromString("block");
//		for (PredicateASP p : pres) {
//			System.out.println(p.toString());
//		}
	
		
		SoundThread st = new SoundThread();
		st.playMusic(0);
		st.playMusic(1);
		
	}
}
