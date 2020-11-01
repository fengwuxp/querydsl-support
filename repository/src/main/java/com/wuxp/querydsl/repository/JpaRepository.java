package com.wuxp.querydsl.repository;

import com.wuxp.querydsl.core.domain.UpdatePredicate;
import com.wuxp.querydsl.core.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;


/**
 * JPA specific extension of {@link Repository}.
 *
 * @author wuxp
 */
public interface JpaRepository<E, ID> extends PagingAndSortingRepository<E, ID> {


    /**
     * Flushes all pending changes to the database.
     */
    void flush();

    /**
     * Saves an entity and flushes changes instantly.
     *
     * @param entity 实体对象
     * @return the saved entity
     */
    <S extends E> S saveAndFlush(S entity);
}
