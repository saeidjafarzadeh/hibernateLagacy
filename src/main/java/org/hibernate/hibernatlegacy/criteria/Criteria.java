package org.hibernate.hibernatlegacy.criteria;

import java.util.*;

import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.ResultTransformer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;
import lombok.Getter;

public class Criteria<T> {

    @Getter
    private final CriteriaBuilder cb;
    @Getter
    private final CriteriaBuilder cbCount;
    private CriteriaQuery<T> cq;
    @Getter
    private final Root<T> root;
    private final EntityManager em;
    private final Set<Predicate> predicates = new HashSet<>();
    private final Root<?> countRoot;
    Set<Predicate> countPredicate = new HashSet<>();
    private CriteriaQuery<Long> countCq;
    private final List<Order> orders = new ArrayList<>();
    private final Map<String, Join<Object, Object>> joins = new HashMap<>();
    private Predicate wherePredicate;


    private int maxResults = -1;
    private int firstResult = -1;
    private int timeout = -1;
    private int fetchSize = -1;
    private boolean cacheable = false;
    private String cacheRegion = null;
    private boolean readOnly = false;
    private LockModeType lockMode = null;
    private Map<String, Join<Object, Object>> getJoins = new HashMap<>();

    public Criteria(EntityManager em, Class<T> clazz) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        this.cq = cb.createQuery(clazz);
        this.countCq = cb.createQuery(Long.class);
        this.root = cq.from(clazz);
        this.countRoot = countCq.from(clazz);
        this.cbCount = em.getCriteriaBuilder();
    }

    public Criteria(EntityManager em, Class<T> clazz, String alias) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
        this.cbCount = em.getCriteriaBuilder();
        this.cq = cb.createQuery(clazz);
        this.countCq = cb.createQuery(Long.class);
        this.root = cq.from(clazz);
        this.root.alias(alias);
        this.countRoot = countCq.from(clazz);
        this.countRoot.alias(alias);
    }


    public Criteria<T> add(Criterion criterion) {
        Predicate predicate = criterion.toPredicate(root, cb);
        predicates.add(predicate);
        return this;
    }
    public Criteria<T> addCountPaginationPredicate(Criterion criterion) {
        Predicate predicateCount = criterion.toPredicate(countRoot, cbCount);
        countPredicate.add(predicateCount);
        return this;
    }
    public Criteria<T> addCountPaginationPredicate(Conjunction conjunction) {
        Predicate predicateCount = conjunction.toPredicate();
        countPredicate.add(predicateCount);
        return this;
    }
    public Criteria<T> addCountPaginationPredicate(Disjunction disjunction) {
        Predicate predicateCount = disjunction.toPredicate();
        countPredicate.add(predicateCount);
        return this;
    }

    public Criteria<T> add(Conjunction conjunction) {

        Predicate newCondition = conjunction.toPredicate();
        if (wherePredicate == null)
            wherePredicate = newCondition;
        else wherePredicate = cb.and(wherePredicate, newCondition);
        cq.where(wherePredicate);
        return this;
    }

    public Criteria<T> add(Disjunction disjunction) {

        Predicate newCondition = disjunction.toPredicate();
        if (wherePredicate == null)
            wherePredicate = newCondition;
        else wherePredicate = cb.or(wherePredicate, newCondition);
        predicates.add(wherePredicate);
//        cq.where(wherePredicate);
        return this;
    }

    public Criteria<T> addOrder(String propertyName, boolean ascending) {
        Path<Object> objectPath = root.get(propertyName);
        if (ascending)
            cb.asc(objectPath);
        else cb.desc(objectPath);
        return this;
    }

    public Criteria<T> addOrder(Path propertyName, boolean ascending) {

        if (ascending) {
            orders.add(cb.asc(propertyName));
        } else {
            orders.add(cb.desc(propertyName));
        }
        return this;
    }

    public Criteria<T> setFetchMode(String fieldName, JoinType value) {
        root.fetch(fieldName, value);
        return this;
    }

    //todo check after test
    public <Y> Criteria<T> createAlias(String currentAssociationPath, String currentAssociationAlias, JoinType joinType) {
        Join<Object, Object> join = root.join(currentAssociationPath, joinType);
        join.alias(currentAssociationAlias);
        getJoins.put(currentAssociationAlias, join);
        return this;
    }

    // متدهای Criterionها
    public Criteria<T> addEqual(String fieldName, Object value) {
        predicates.add(cb.equal(root.get(fieldName), value));
        countPredicate.add(cb.equal(countRoot.get(fieldName), value));
        return this;
    }

    public Criteria<T> addLike(String fieldName, String value) {
        predicates.add(cb.like(root.get(fieldName), value));
        countPredicate.add(cb.like(countRoot.get(fieldName), value));
        return this;
    }

    public Criteria<T> addGreaterThan(String fieldName, Number value) {
        predicates.add(cb.gt(root.get(fieldName), value));
        countPredicate.add(cb.gt(countRoot.get(fieldName), value));
        return this;
    }

    public Criteria<T> addLessThan(String fieldName, Number value) {
        predicates.add(cb.lt(root.get(fieldName), value));
        countPredicate.add(cb.lt(countRoot.get(fieldName), value));
        return this;
    }

    public Criteria<T> addNotEqual(String fieldName, Object value) {
        predicates.add(cb.notEqual(root.get(fieldName), value));
        countPredicate.add(cb.notEqual(countRoot.get(fieldName), value));
        return this;
    }

    // مرتب‌سازی
    public Criteria<T> addOrderAsc(String fieldName) {
        orders.add(cb.asc(root.get(fieldName)));
        return this;
    }

    public Criteria<T> addOrderDesc(String fieldName) {
        orders.add(cb.desc(root.get(fieldName)));
        return this;
    }

    // Joinها و Fetch
    public Criteria<T> addJoin(String path, String alias) {
        Join<Object, Object> join = root.join(path);
        joins.put(alias, join);
        return this;
    }

//	public MyCriteria<T> addFetch(String path, String alias) {
//		Join<Object, Object> fetch = root.fetch(path);
//		joins.put(alias, fetch);
//		return this;
//	}

    // Projection
    public Criteria<T> setProjection(Selection projection) {
        this.cq.select(projection);
        return this;
    }

    public Criteria<T> setProjection(ProjectionList projectionList) {
        if (projectionList != null) {
            List<Selection<?>> selections = projectionList.getSelections();
            if (selections.size() == 1) cq.select((Selection<? extends T>) selections.get(0));
            else cq.multiselect(selections);
        }
        return this;
    }

    // تنظیمات محدودیت‌ها
    public Criteria<T> setMaxResults(int max) {
        this.maxResults = max;
        return this;
    }

    public Criteria<T> setFirstResult(int first) {
        this.firstResult = first;
        return this;
    }

    public Criteria<T> setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public Criteria<T> setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        return this;
    }

    // کش
    public Criteria<T> setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
        return this;
    }

    public Criteria<T> setCacheRegion(String cacheRegion) {
        this.cacheRegion = cacheRegion;
        return this;
    }

    // دیگر تنظیمات
    public Criteria<T> setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        return this;
    }

    public Criteria<T> setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    // اجرای query
    public List<T> list() {
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(orders);

        TypedQuery<T> query = em.createQuery(cq);
//       if (maxResults > -1) query.setMaxResults(maxResults);
//        if (firstResult > -1) query.setFirstResult(firstResult);
        if (timeout > -1) query.setHint("jakarta.persistence.query.timeout", timeout);
        if (fetchSize > -1) query.setHint("org.hibernate.fetchSize", fetchSize);
        if (cacheable) query.setHint("org.hibernate.cacheable", true);
        if (cacheRegion != null) query.setHint("org.hibernate.cacheRegion", cacheRegion);
        if (readOnly) query.setHint("jakarta.persistence.query.readOnly", true);
        if (lockMode != null) query.setLockMode(lockMode);
        return query.getResultList();
    }

    public T uniqueResult() {
        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(orders);
        TypedQuery<T> query = em.createQuery(cq);
        if (timeout > -1) query.setHint("jakarta.persistence.query.timeout", timeout);
        if (lockMode != null) query.setLockMode(lockMode);
        return query.getSingleResult();
    }

    // دسترسی به aliasها
    public Join<Object, Object> getJoin(String alias) {
        return joins.get(alias);
    }

    public Root<T> getRoot() {
        return root;
    }

    public CriteriaQuery<T> getQuery() {
        return cq;
    }

    @Deprecated
    public <T> ResultTransformer setResultTransformer(ResultTransformer transformer) {

        return this.resultTransformer = transformer;
    }

    private ResultTransformer resultTransformer;
    public static final ResultTransformer PROJECTION = new ResultTransformer() {

        @Override
        public List transformList(List resultList) {
            return resultList;
        }

        @Override
        public Object transformTuple(Object[] objects, String[] strings) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < strings.length; i++) {
                map.put(strings[i], objects[i]);
            }
            return map;
        }
    };
    public static final ResultTransformer DISTINCT_ROOT_ENTITY = new ResultTransformer() {

        @Override
        public List transformList(List resultList) {
            return resultList;
        }

        @Override
        public Object transformTuple(Object[] objects, String[] strings) {
            return null;
        }
    };
    public static final ResultTransformer ROOT_ENTITY = new ResultTransformer() {

        @Override
        public List transformList(List resultList) {
            return resultList;
        }

        @Override
        public Object transformTuple(Object[] objects, String[] strings) {
            return null;
        }
    };


    public CriteriaQuery<Long> getCountCq() {
        return countCq;
    }

    public void setCountCq(CriteriaQuery<Long> countCq) {
        this.countCq = countCq;
    }

    public Set<Predicate> getPredicates() {
        return predicates;
    }

    public void setCq(CriteriaQuery<T> cq) {
        this.cq = cq;
    }

    public CriteriaQuery<T> getCq() {
        return cq;
    }

    public Set<Predicate> getCountPredicate() {
        return countPredicate;
    }

    public void setCountPredicate(Set<Predicate> countPredicate) {
        this.countPredicate = countPredicate;
    }

    public Root<?> getCountRoot() {
        return countRoot;
    }

}
