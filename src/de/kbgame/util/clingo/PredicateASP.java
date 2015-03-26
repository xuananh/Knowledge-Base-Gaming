package de.kbgame.util.clingo;

import java.util.ArrayList;
import java.util.List;

public class PredicateASP {

	/** Praedikate name */
	private String pre;
	
	/** List alle Paramete von Praedikate */
	private final List<Object> parameter = new ArrayList<Object>();

	/**
	 * get name von Praedikate
	 * @return name von Praedikate im String
	 */
	public String getPre() {
		return pre;
	}

	/**
	 * set name fuer Preadikate
	 * @param pre neue Name für Praedikate
	 */
	public void setPre(String pre) {
		this.pre = pre;
	}

	/**
	 * get List der Paramete von Praedikate
	 * @return List Parameter
	 */
	public List<Object> getParameter() {
		return parameter;
	}
	
	/**
	 * get eine Paramete von List durch index
	 * @param index von Paramete
	 * @return eine Paramete von List
	 */
	public Object getParameterOfIndex(int index) {
		if(index > parameter.size()) {
			return null;
		}
		return parameter.get(index);
	}
	
	/**
	 * fuege eine neue Paramete hinzu
	 * @param par neue Paramete von Praedikate
	 */
	public void addParameter(Object par) {
		parameter.add(par);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
