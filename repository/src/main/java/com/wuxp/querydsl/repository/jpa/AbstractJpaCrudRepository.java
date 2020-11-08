package com.wuxp.querydsl.repository.jpa;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQueryFactory;
import com.wuxp.querydsl.core.domain.Pagination;
import com.wuxp.querydsl.core.support.EntityMetadata;
import com.wuxp.querydsl.repository.JpaRepository;
import com.wuxp.querydsl.repository.support.JpaEntityMetadata;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.QueryHints;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wuxp
 */
public abstract class AbstractJpaCrudRepository<E, ID> implements JpaRepository<E, ID> {

    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";

    protected final JPQLQueryFactory jpqlQueryFactory;

    protected final EntityManager entityManager;

    protected final EntityMetadata entityMetadata;

//    private final Map<JpaCrudMethodKey, CrudMethodMetadata> methodMetadataMap = new HashMap<>();

    public AbstractJpaCrudRepository(JPQLQueryFactory jpqlQueryFactory,
                                     EntityManager entityManager,
                                     Class<E> entityClass,
                                     Class<? extends JpaRepository<E, ID>> repositoryInterface) {
        this.jpqlQueryFactory = jpqlQueryFactory;
        this.entityManager = entityManager;
        this.entityMetadata = new JpaEntityMetadata(entityClass);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public <S extends E> S save(S entity) {
        if (getEntityId(entity).isPresent()) {
            entityManager.merge(entity);
        } else {
            entityManager.persist(entity);
        }
        return entity;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public <S extends E> Iterable<S> saveAll(Iterable<S> entities) {
        Assert.notNull(entities, "entities must not be null!");

        List<S> result = new ArrayList<>();
        for (S entity : entities) {
            result.add(save(entity));
        }
        return result;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public <S extends E> S saveAndFlush(S entity) {
        S result = save(entity);
        flush();
        return result;
    }


    @Transactional
    @Override
    public void flush() {
        entityManager.flush();
    }

    @Override
    public Optional<E> findById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        EntityMetadata entityMetadata = this.entityMetadata;
        Class<E> domainType = entityMetadata.getDomainClass();
        return Optional.ofNullable(entityManager.find(domainType, id));
    }

    @Override
    public boolean existsById(ID id) {

        return false;
    }

    @Override
    public long count() {
        return 0;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteById(ID id) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL);
        Optional<E> optional = findById(id);
        optional.ifPresent(entityManager::remove);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void delete(E entity) {
        Assert.notNull(entity, "Entity must not be null!");
        Optional<Object> entityId = this.getEntityId(entity);
        if (entityId.isPresent()) {
            E existing = (E) entityManager.find(entity.getClass(), entityId);
            // if the entity to be deleted doesn't exist, delete is a NOOP
            if (existing == null) {
                return;
            }
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        } else {
            // try remove
            entityManager.remove(entity);
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteAll(Iterable<? extends E> entities) {
        Assert.notNull(entities, "Entities must not be null!");
        for (E entity : entities) {
            delete(entity);
        }
    }


    @Override
    public Pagination<E> find(Predicate predicate, Pageable pageable) {
        return null;
    }

    /**
     * Returns {@link QueryHints} with the query hints based on the current {@link CrudMethodMetadata} and potential
     * {@link EntityGraph} information.
     *
     * @param metadata
     * @return
     */
    protected QueryHints getQueryHints(CrudMethodMetadata metadata) {
//        return metadata == null ? QueryHints.NoHints.INSTANCE : DefaultQueryHints.of(entityInformation, metadata);
        return QueryHints.NoHints.INSTANCE;
    }


    private Optional<Object> getEntityId(E entity) {
        Optional<Field> idField = this.entityMetadata.getIdField();
        return idField.map(field -> ReflectionUtils.getField(field, entity));
    }
}
