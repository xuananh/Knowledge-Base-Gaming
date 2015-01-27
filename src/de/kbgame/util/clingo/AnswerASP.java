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
