package de.kbgame.util.clingo;

import java.util.ArrayList;
import java.util.List;

public class AnswerASP {

	/**
	 * List alle Prädikate von ASP
	 */
	private final List<PredicateASP> preList = new ArrayList<PredicateASP>();
	
	/**
	 * get method
	 * @return list from PredicateASP
	 */
	public List<PredicateASP> getPreList() {
		return preList;
	}
	
	/**
	 * fuege eine Prädikate im List hinzu
	 * @param pre PredicateASP Instance
	 */
	public void addPredicate(PredicateASP pre) {
		preList.add(pre);
	}
	
	/**
	 * Jede Praedikate hat eine bestimmte Name. Mit diesem name kann man
	 * alles Praedikate im List herausfinden.
	 * 
	 * @param pre name von Praedikate
	 * @return List von alle Praedikate mit bestimmte Name
	 */
	public List<PredicateASP> getPreListFromString(String pre){
		final List<PredicateASP> preLs = new ArrayList<PredicateASP>();
		if(pre != null && !("").equals(pre)) {
			for(PredicateASP p : preList) {
				if(p.getPre().equals(pre)) {
					preLs.add(p);
				}
			}
		}
		return preLs;
	}
}
