package de.kbgame.util.clingo;




public class Main {

	public static void main(String[] args) {

		String[] params = new String[3];
		params[0] = "clingo";
		params[1] = "clingo/graeben.txt";
		params[2] = "1";

		String res = Clingo.callClingo(params);
		
		AnswerASP a = AnswerASP.getAnswerASPfromRes(res);
		for(PredicateASP p : a.getPreList()) {
			System.out.println(p.toString());
		}
	}
}
