package com.github.saeidjafarzadeh.hibernatlegacy.criteria;

public enum MatchMode {
	EXACT, START, END, ANYWHERE;

	public String toMatchString(String value) {
		switch (this){
			case EXACT :return value;

			case START :return value + "%";

			case END :return "%" + value;

			case ANYWHERE  :return  "%" + value + "%";
			default:
				return value;
		}
	}
}
