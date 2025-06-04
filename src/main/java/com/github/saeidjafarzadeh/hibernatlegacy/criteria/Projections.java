package com.github.saeidjafarzadeh.hibernatlegacy.criteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;

public class Projections<T> {
	private final CriteriaBuilder cb;
	private final CriteriaQuery<T> cq;
	private final Root<T> root;
	private final EntityManager em;
	private final List<Predicate> predicates = new ArrayList<>();
	private final List<Order> orders = new ArrayList<>();
	private final Map<String, Join<Object, Object>> joins = new HashMap<>();

	public Projections(CriteriaBuilder cb, CriteriaQuery<T> cq, Root<T> root, EntityManager em) {
		this.cb = cb;
		this.cq = cq;
		this.root = root;
		this.em = em;
	}
	// ------------------ Count ------------------

	public static <T> Projection<T> count(String property) {
		return (root, cb) -> cb.count(root.get(property));
	}

	public static <T> Projection<T> countDistinct(String property) {
		return (root, cb) -> cb.countDistinct(root.get(property));
	}

	public static <T> Projection<T> rowCount() {
		return (root, cb) -> cb.count(root);
	}

	// ------------------ Aggregation ------------------

	public static <T> Projection<T> avg(String property) {
		return (root, cb) -> cb.avg(root.get(property));
	}

	public static <T> Projection<T> max(String property) {
		return (root, cb) -> cb.max(root.get(property));
	}

	public static <T> Projection<T> min(String property) {
		return (root, cb) -> cb.min(root.get(property));
	}

	public static <T> Projection<T> sum(String property) {
		return (root, cb) -> cb.sum(root.get(property));
	}

	public static <T> Projection<T> sumAsLong(String property) {
		return (root, cb) -> cb.sum(root.get(property)).as(Long.class);
	}

	public static <T> Projection<T> sumAsDouble(String property) {
		return (root, cb) -> cb.sum(root.get(property)).as(Double.class);
	}

	public static <T> Projection<T> sumAsFloat(String property) {
		return (root, cb) -> cb.sum(root.get(property)).as(Float.class);
	}

	// ------------------ Grouping ------------------

	public static <T> Projection<T> groupProperty(String property) {
		return (root, cb) -> root.get(property);
	}

	// ------------------ Distinct / Property-based ------------------

//	public static <T> Projection<T> distinct(Projection<T> projection) {
//		return (root, cb) -> cb.construct(Object.class, projection.toSelection(root, cb)).distinct(true);
//	}

	public static <T> Projection<T> property(String property) {
		return (root, cb) -> root.get(property);
	}
	public static <T> Projection<T> property(Path property) {
		return (root, cb) -> property;
	}

	public static Selection distinct(String fieldName, CriteriaQuery<?> cq, Root<?> root) {
		return cq.select(root.get("fieldName")).distinct(true).getSelection();
	}

	public static <T> Projection<T> id() {
		return (root, cb) -> root.get("id");
	}

	public static String idEN() {
		return "id";
	}

//	public static <T> Selection<T> alias(Projection<T> projection, String alias) {
//		return projection.toSelection(alias);
//	}

	// ------------------ SQL Projection (if supported) ------------------

//	public static <T> Projection<T> sqlProjection(String sql, String[] columns, Class<?>[] types) {
//		return (root, cb) -> cb.select(cb.function(sql, Object.class, root)).alias(columns[0]);
//	}

	// ------------------ To Selection ------------------

	@FunctionalInterface
	public interface Projection<T> {
		Selection<?> toSelection(Root<T> root, CriteriaBuilder cb);
	}

	// Example of custom projection usage:
	public static <T> Projection<T> tupleProjection(String property1, String property2) {
		return (root, cb) -> cb.tuple(root.get(property1), root.get(property2));
	}

	// Handling Projection List:
	public static <T> List<Selection<?>> projectionList(List<Projection<T>> projections, Root<T> root, CriteriaBuilder cb) {
		List<Selection<?>> selections = new ArrayList<>();
		for (Projection<T> projection : projections) {
			selections.add(projection.toSelection(root, cb));
		}
		return selections;
	}

	public static ProjectionList projectionList() {

		return new ProjectionList();
	}


	// ------------------ String Functions ------------------

	public static <T> Projection<T> concat(String property1, String property2) {
		return (root, cb) -> cb.concat(root.get(property1), root.get(property2));
	}

	public static <T> Projection<T> lower(String property) {
		return (root, cb) -> cb.lower(root.get(property));
	}

	public static <T> Projection<T> upper(String property) {
		return (root, cb) -> cb.upper(root.get(property));
	}

	// ------------------ Mathematical Functions ------------------

	public static <T> Projection<T> abs(String property) {
		return (root, cb) -> cb.abs(root.get(property));
	}

	public static <T> Projection<T> sqrt(String property) {
		return (root, cb) -> cb.sqrt(root.get(property));
	}

	// ------------------ Date Functions ------------------

	public static <T> Projection<T> currentDate() {
		return (root, cb) -> cb.currentDate();
	}

	public static <T> Projection<T> currentTime() {
		return (root, cb) -> cb.currentTime();
	}

	public static <T> Projection<T> currentTimestamp() {
		return (root, cb) -> cb.currentTimestamp();
	}

	// ------------------ Example - Custom Function ------------------

	public static <T> Projection<T> customFunction(String sqlFunction, String property) {
		return (root, cb) -> cb.function(sqlFunction, Object.class, root.get(property));
	}
}
