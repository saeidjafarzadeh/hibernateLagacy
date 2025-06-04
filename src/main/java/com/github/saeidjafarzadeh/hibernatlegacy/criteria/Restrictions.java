package com.github.saeidjafarzadeh.hibernatlegacy.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class Restrictions {


    public static <T> Criterion<T> and(Criterion<T>... criteria) {
        return (root, cb) -> cb.and(Arrays.stream(criteria).map(tCriterion -> tCriterion.toPredicate(root, cb)).toArray(Predicate[]::new));
    }

    public static <T> Criterion<T> or(Criterion<T>... criteria) {
        return (root, cb) -> cb.or(Arrays.stream(criteria).map(tCriterion -> tCriterion.toPredicate(root, cb)).toArray(Predicate[]::new));

    }

    public static <T> Criterion<T> not(Criterion<T> criteria) {
        return (root, cb) -> cb.not(criteria.toPredicate(root, cb));

    }

    // ------------------ Comparison ------------------

    public static <T> Criterion<T> eq(String property, Object value) {
        return (root, cb) -> cb.equal(resolvePath(root, property), value);
    }

    public static <T> Criterion<T> eq(Path property, Object value) {
        return (root, cb) -> cb.equal(property, value);
    }

    public static <T> Criterion<T> ne(String property, Object value) {
        return (root, cb) -> cb.notEqual(resolvePath(root, property), value);
    }

    public static <T> Criterion<T> ne(Path property, Object value) {
        return (root, cb) -> cb.notEqual(property, value);
    }

    public static <T> Criterion<T> gt(String property, Comparable value) {
        return (root, cb) -> cb.greaterThan(resolvePath(root, property), value);
    }
    public static <T> Criterion<T> gt(Path property, Comparable value) {
        return (root, cb) -> cb.greaterThan( property, value);
    }

    public static <T> Criterion<T> ge(String property, Comparable value) {
        return (root, cb) -> cb.greaterThanOrEqualTo(resolvePath(root, property), value);
    }
    public static <T> Criterion<T> ge(Path property, Comparable value) {
        return (root, cb) -> cb.greaterThanOrEqualTo( property, value);
    }

    public static <T> Criterion<T> lt(String property, Comparable value) {
        return (root, cb) -> cb.lessThan(resolvePath(root, property), value);
    }
    public static <T> Criterion<T> lt(Path property, Comparable value) {
        return (root, cb) -> cb.lessThan( property, value);
    }

    public static <T> Criterion<T> le(String property, Comparable value) {
        return (root, cb) -> cb.lessThanOrEqualTo(resolvePath(root, property), value);
    }
    public static <T> Criterion<T> le(Path property, Comparable value) {
        return (root, cb) -> cb.lessThanOrEqualTo( property, value);
    }

    public static <T> Criterion<T> between(String property, Comparable lo, Comparable hi) {
        return (root, cb) -> cb.between(resolvePath(root, property), lo, hi);
    }
    public static <T> Criterion<T> between(Path property, Comparable lo, Comparable hi) {
        return (root, cb) -> cb.between( property, lo, hi);
    }

    public static <T> Criterion<T> in(String property, Object... values) {
        return (root, cb) -> resolvePath(root, property).in(values);
    }
    public static <T> Criterion<T> in(Path property, Object... values) {
        return (root, cb) ->  property.in(values);
    }

    public static <T> Criterion<T> in(String property, Collection<?> values) {
        return (root, cb) -> resolvePath(root, property).in(values);
    }
    public static <T> Criterion<T> in(Path property, Collection<?> values) {
        return (root, cb) ->  property.in(values);
    }

    // ------------------ Null ------------------

    public static <T> Criterion<T> isNull(String property) {
        return (root, cb) -> cb.isNull(resolvePath(root, property));
    }
    public static <T> Criterion<T> isNull(Path property) {
        return (root, cb) -> cb.isNull( property);
    }


    public static <T> Criterion<T> isNotNull(String property) {
        return (root, cb) -> cb.isNotNull(resolvePath(root, property));
    }
    public static <T> Criterion<T> isNotNull(Path property) {
        return (root, cb) -> cb.isNotNull( property);
    }

    // ------------------ Empty ------------------

    public static <T> Criterion<T> isEmpty(String property) {
        return (root, cb) -> cb.isEmpty(root.get(property));
    }
    public static <T> Criterion<T> isEmpty(Path property) {
        return (root, cb) -> cb.isEmpty(property);
    }

    public static <T> Criterion<T> isNotEmpty(String property) {
        return (root, cb) -> cb.isNotEmpty(root.get(property));
    }

    // ------------------ Like / iLike ------------------

    public static <T> Criterion<T> like(String property, String pattern) {
        return (root, cb) -> cb.like(resolvePath(root, property), pattern);
    }
    public static <T> Criterion<T> like(Path property, String pattern) {
        return (root, cb) -> cb.like( property, pattern);
    }

    public static <T> Criterion<T> like(String property, String pattern, MatchMode mode) {
        return (root, cb) -> cb.like(resolvePath(root, property), mode.toMatchString(pattern));
    }
    public static <T> Criterion<T> like(Path property, String pattern, MatchMode mode) {
        return (root, cb) -> cb.like( property, mode.toMatchString(pattern));
    }

    public static <T> Criterion<T> ilike(String property, String pattern) {
        return (root, cb) -> cb.like(cb.lower(resolvePath(root, property)), pattern.toLowerCase());
    }
    public static <T> Criterion<T> ilike(Path property, String pattern) {
        return (root, cb) -> cb.like(cb.lower( property), pattern.toLowerCase());
    }

    public static <T> Criterion<T> ilike(String property, String pattern, MatchMode mode) {
        return (root, cb) -> cb.like(cb.lower(resolvePath(root, property)), mode.toMatchString(pattern.toLowerCase()));
    }
    public static <T> Criterion<T> ilike(Path property, String pattern, MatchMode mode) {
        return (root, cb) -> cb.like(cb.lower( property), mode.toMatchString(pattern.toLowerCase()));
    }

    // ------------------ Property to Property ------------------

    public static <T> Criterion<T> eqProperty(String p1, String p2) {
        return (root, cb) -> cb.equal(resolvePath(root, p1), resolvePath(root, p2));
    }
    public static <T> Criterion<T> eqProperty(Path p1, Path p2) {
        return (root, cb) -> cb.equal( p1,  p2);
    }

    public static <T> Criterion<T> neProperty(String p1, String p2) {
        return (root, cb) -> cb.notEqual(resolvePath(root, p1), resolvePath(root, p2));
    }

    public static <T> Criterion<T> neProperty(Path p1, Path p2) {
        return (root, cb) -> cb.notEqual( p1,  p2);
    }


    public static <T> Criterion<T> ltProperty(String p1, String p2) {
        return (root, cb) -> cb.lessThan(resolvePath(root, p1), resolvePath(root, p2));
    }
    public static <T> Criterion<T> ltProperty(Path p1, Path p2) {
        return (root, cb) -> cb.lessThan( p1,  p2);
    }


    public static <T> Criterion<T> leProperty(String p1, String p2) {
        return (root, cb) -> cb.lessThanOrEqualTo(resolvePath(root, p1), resolvePath(root, p2));
    }
    public static <T> Criterion<T> leProperty(Path p1, Path p2) {
        return (root, cb) -> cb.lessThanOrEqualTo( p1,  p2);
    }

    public static <T> Criterion<T> gtProperty(Path p1, Path p2) {
        return (root, cb) -> cb.greaterThan( p1,  p2);
    }

    public static <T> Criterion<T> geProperty(String p1, String p2) {
        return (root, cb) -> cb.greaterThanOrEqualTo(resolvePath(root, p1), resolvePath(root, p2));
    }

    public static <T> Criterion<T> geProperty(Path p1, Path p2) {
        return (root, cb) -> cb.greaterThanOrEqualTo( p1,  p2);
    }

    // ------------------ Logical composition ------------------

    public static <T> Criterion<T> allEq(Map<String, Object> propertyValues) {
        return (root, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            for (Map.Entry<String, Object> e : propertyValues.entrySet()) {
                preds.add(cb.equal(resolvePath(root, e.getKey()), e.getValue()));
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }

    public static <T> Criterion<T> conjunction() {

        return (root, cb) -> cb.conjunction();
    }

    public static <T> Criterion<T> disjunction() {
        return (root, cb) -> cb.disjunction();
    }

    // ------------------ SQL Restriction Placeholder ------------------

    public static <T> Criterion<T> sqlRestriction(String sql) {
        // Placeholder – no direct support in JPA
        return (root, cb) -> cb.isTrue(cb.literal(true));
    }

    // ------------------ Path Resolver ------------------

    @SuppressWarnings("unchecked")
    private static <Y> Path<Y> resolvePath(Root<?> root, String propertyPath) {
        String[] parts = propertyPath.split("\\.");
        Path<?> path = root;
        for (String part : parts) {
            path = path.get(part);
        }
        return (Path<Y>) path;
    }


}
