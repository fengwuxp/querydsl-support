package org.springframework.data.jpa.repository.support;


import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Default implementation of {@link CrudMethodMetadata} that will inspect the backing method for annotations.
 *
 * @author Oliver Gierke
 * @author Thomas Darimont
 */
class DefaultCrudMethodMetadata implements CrudMethodMetadata {

    private final @Nullable
    LockModeType lockModeType;
    private final org.springframework.data.jpa.repository.support.QueryHints queryHints;
    private final org.springframework.data.jpa.repository.support.QueryHints queryHintsForCount;
    private final Optional<EntityGraph> entityGraph;
    private final Method method;

    /**
     * Creates a new {@link DefaultCrudMethodMetadata} for the given {@link Method}.
     *
     * @param method must not be {@literal null}.
     */
    DefaultCrudMethodMetadata(Method method) {

        Assert.notNull(method, "Method must not be null!");

        this.lockModeType = findLockModeType(method);
        this.queryHints = findQueryHints(method, it -> true);
        this.queryHintsForCount = findQueryHints(method, org.springframework.data.jpa.repository.QueryHints::forCounting);
        this.entityGraph = findEntityGraph(method);
        this.method = method;
    }

    private static Optional<EntityGraph> findEntityGraph(Method method) {
        return Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, EntityGraph.class));
    }

    @Nullable
    private static LockModeType findLockModeType(Method method) {

        Lock annotation = AnnotatedElementUtils.findMergedAnnotation(method, Lock.class);
        return annotation == null ? null : (LockModeType) AnnotationUtils.getValue(annotation);
    }

    private static org.springframework.data.jpa.repository.support.QueryHints findQueryHints(Method method,
                                                                                             Predicate<org.springframework.data.jpa.repository.QueryHints> annotationFilter) {

        MutableQueryHints queryHints = new MutableQueryHints();

        org.springframework.data.jpa.repository.QueryHints queryHintsAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, org.springframework.data.jpa.repository.QueryHints.class);

        if (queryHintsAnnotation != null && annotationFilter.test(queryHintsAnnotation)) {

            for (QueryHint hint : queryHintsAnnotation.value()) {
                queryHints.add(hint.name(), hint.value());
            }
        }

        QueryHint queryHintAnnotation = AnnotationUtils.findAnnotation(method, QueryHint.class);

        if (queryHintAnnotation != null) {
            queryHints.add(queryHintAnnotation.name(), queryHintAnnotation.value());
        }

        return queryHints;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.CrudMethodMetadata#getLockModeType()
     */
    @Nullable
    @Override
    public LockModeType getLockModeType() {
        return lockModeType;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.CrudMethodMetadata#getQueryHints()
     */
    @Override
    public org.springframework.data.jpa.repository.support.QueryHints getQueryHints() {
        return queryHints;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.CrudMethodMetadata#getQueryHintsForCount()
     */
    @Override
    public org.springframework.data.jpa.repository.support.QueryHints getQueryHintsForCount() {
        return queryHintsForCount;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.CrudMethodMetadata#getEntityGraph()
     */
    @Override
    public Optional<EntityGraph> getEntityGraph() {
        return entityGraph;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.jpa.repository.support.CrudMethodMetadata#getMethod()
     */
    @Override
    public Method getMethod() {
        return method;
    }
}