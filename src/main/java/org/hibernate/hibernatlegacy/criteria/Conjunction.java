package org.hibernate.hibernatlegacy.criteria;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;

public class Conjunction {
	private final CriteriaBuilder criteriaBuilder;

	public Conjunction(CriteriaBuilder criteriaBuilder) {
		this.criteriaBuilder = criteriaBuilder;
	}

	private final List<Predicate> predicates = new ArrayList<>();

	public Conjunction add(Predicate predicate) {
		predicates.add(predicate);
		return this;
	}

	public Predicate toPredicate() {
		return predicates.isEmpty() ? criteriaBuilder.conjunction() : criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	}
}
