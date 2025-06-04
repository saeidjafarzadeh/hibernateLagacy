package org.hibernate.hibernatlegacy.criteria;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

public class Disjunction {
	private final CriteriaBuilder criteriaBuilder;
	public Disjunction(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	private final List<Predicate> predicates = new ArrayList<>();

	public Disjunction add(Predicate predicate) {
		predicates.add(predicate);
		return this;
	}

	public Predicate toPredicate() {
		return predicates.isEmpty() ? criteriaBuilder.disjunction() : criteriaBuilder.or(predicates.toArray(new Predicate[0]));
	}
}
