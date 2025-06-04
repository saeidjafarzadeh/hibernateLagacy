package org.hibernate.hibernatlegacy.criteria;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Selection;

public class ProjectionList {

	private final List<Selection<?>> selections = new ArrayList<>();

	public ProjectionList add(Selection<?> selection) {
		selections.add(selection);
		return this;
	}

	public List<Selection<?>> getSelections() {
		return selections;
	}
}
