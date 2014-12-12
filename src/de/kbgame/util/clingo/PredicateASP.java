package de.kbgame.util.clingo;

import java.util.ArrayList;
import java.util.List;

public class PredicateASP {

	private String pre;
	private final List<Object> parameter = new ArrayList<Object>();

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public List<Object> getParameter() {
		return parameter;
	}
	
	public Object getParameterOfIndex(int index) {
		if(index > parameter.size()) {
			return null;
		}
		return parameter.get(index);
	}
	
	public void addParameter(Object par) {
		parameter.add(par);
	}
	
	public static PredicateASP getPredicate(String pre, ParameterType type) {
		final PredicateASP predicateASP = new PredicateASP();
		
		pre = pre.trim();
		predicateASP.setPre(pre.substring(0,pre.indexOf("(")));
		final String[] pars = pre.substring(pre.indexOf("(")+1,pre.indexOf(")")).trim().split(",");
		
		switch (type) {
		case INTEGER:
			for(String par : pars) {
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("pre=");
		sb.append(pre).append(", parameter=");
		for(Object parObject : parameter) {
			sb.append(parObject.toString()).append(" ");
		}
		return sb.toString();
	}
}
