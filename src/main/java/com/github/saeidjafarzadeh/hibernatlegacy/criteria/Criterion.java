package com.github.saeidjafarzadeh.hibernatlegacy.criteria;

import java.util.Arrays;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@FunctionalInterface
public interface Criterion<T> {
	Predicate toPredicate(Root<T> root , CriteriaBuilder cb);

	static <T> Criterion<T> and(Criterion<T>...criteria){
		return (root, cb) -> cb.and(Arrays.stream(criteria).map(tCriterion -> tCriterion.toPredicate(root,cb)).toArray(Predicate[]::new));
	}
	static <T> Criterion<T> or(Criterion<T>...criteria){
		return (root, cb) -> cb.or(Arrays.stream(criteria).map(tCriterion -> tCriterion.toPredicate(root,cb)).toArray(Predicate[]::new));

	}
	static <T> Criterion<T> not(Criterion<T> criteria){
		return (root, cb) -> cb.not(criteria.toPredicate(root,cb));

	}
}
