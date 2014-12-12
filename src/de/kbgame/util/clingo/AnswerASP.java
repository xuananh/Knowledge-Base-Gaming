package de.kbgame.util.clingo;

import java.util.ArrayList;
import java.util.List;

public class AnswerASP {

	private final List<PredicateASP> preList = new ArrayList<PredicateASP>();
	
	public List<PredicateASP> getPreList() {
		return preList;
	}
	
	public void addPredicate(PredicateASP pre) {
		preList.add(pre);
	}
	
	public List<PredicateASP> getPreListFromString(String pre){
		final List<PredicateASP> preLs = new ArrayList<PredicateASP>();
		for(PredicateASP p : preList) {
			if(p.getPre().equals(pre)) {
				preLs.add(p);
			}
		}
		return preLs;
	}
	
	private static AnswerASP getAnswerASP(String answer) {
		final AnswerASP answerASP = new AnswerASP();
		final String[] pres = answer.split(" ");
		for(String pre : pres) {
			answerASP.addPredicate(PredicateASP.getPredicate(pre, ParameterType.INTEGER));
		}
		return answerASP;
	}
	
	public static AnswerASP getAnswerASPfromRes(String res) {
		final String[] q = res.substring(res.indexOf("Answer"), res.indexOf("SATISFIABLE")).split("\n\r|\r");
		return AnswerASP.getAnswerASP(q[1].trim());
	}
}
