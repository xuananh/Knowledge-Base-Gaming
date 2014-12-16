package de.kbgame.util.clingo;

import java.awt.Color;
import java.util.List;

public class Main {

	public static void main(String[] args) {

		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/graeben.txt";
		params[2] = "1";

		String res = Clingo.callClingo(params);

		AnswerASP a = AnswerASP.getAnswerASPfromRes(res);
		List<PredicateASP> pres = a.getPreListFromString("block");
		for (PredicateASP p : pres) {
			System.out.println(p.toString());
		}
		
		new ClingoThread();

//		ClingoGraphic graphic = new ClingoGraphic();
//		int w = graphic.Width / 30;
//		int h = graphic.Height / 10;
//		int x = 0, y = 0;
//		int r1 = 50, g1 = 100, b1 = 150;
//		int r9 = 255, g9 = 0, b9 = 0;
//		int r0 = 150, g0 = 150, b0 = 150;
//
//		graphic.startDrawNewFrame();
//
//		graphic.newBackground();
//		for (PredicateASP p : pres) {
//			x = (int) p.getParameterOfIndex(0)*w;
//			y = (int) p.getParameterOfIndex(1)*h;
//			if ((int) p.getParameterOfIndex(2) == 0) {
//				graphic.drawRectangle(x, y, w, h, new Color(r0, g0, b0));
//			}
//			if ((int) p.getParameterOfIndex(2) == 1) {
//				graphic.drawRectangle(x, y, w, h, new Color(r1, g1, b1));
//			}
//			if ((int) p.getParameterOfIndex(2) == 9) {
//				graphic.drawRectangle(x, y, w, h, new Color(r9, g9, b9));
//			}
//		}
//
//		graphic.endDrawNewFrame();
	}
}
