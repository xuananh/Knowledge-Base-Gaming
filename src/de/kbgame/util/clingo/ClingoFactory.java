package de.kbgame.util.clingo;

public class ClingoFactory extends Clingo{
	
private static ClingoFactory instance = null;
	
	public static ClingoFactory getInstance() {
		if(instance == null) {
			instance = new ClingoFactory();
		}
		return instance;
	}
   
	public AnswerASP getAnswerASP(String[] params) {
		final String res = callClingo(params);
		if(("").equals(res)) {
			return getAnswerASP("");
		}
		final String[] line = res.split("\n\r|\r");
		String newRes = "";
		for(String s : line) {
			if(s.trim().matches("[a-zA-Z]+\\(([0-9]+,?)+.*\\)\\.?"))
				newRes = s;
		}
		return getAnswerASP(newRes);
	}
	
	private static PredicateASP getPredicate(String pre, ParameterType type) {
		final PredicateASP predicateASP = new PredicateASP();
		if(pre != null && "".equals(pre)) {
			return predicateASP;
		}
		pre = pre.trim();
		predicateASP.setPre(pre.substring(0,pre.indexOf("(")));
		final String[] pars = pre.substring(pre.indexOf("(")+1,pre.indexOf(")")).trim().split(",");
		
		switch (type) {
		case INTEGER:
			for(String par : pars) {
				if(par.matches("[0-9]+"))
					predicateASP.addParameter(Integer.parseInt(par.trim()));
	 		}
			break;
		case STRING:
			for(String par : pars) {
				predicateASP.addParameter(par.trim());
			}
			break;
		default:
			break;
		}
		return predicateASP;
	}
	
	private static AnswerASP getAnswerASP(String answer) {
		final AnswerASP answerASP = new AnswerASP();
		if(answer != null && !("").equals(answer)) {
			String regel = " ";
			if(answer.contains(".")) {
				regel = ". ";
			}
			final String[] pres = answer.split(regel);
			for(String pre : pres) {
				answerASP.addPredicate(getPredicate(pre, ParameterType.INTEGER));
			}
		}
		return answerASP;
	}
}
