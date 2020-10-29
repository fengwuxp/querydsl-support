package com.wuxp.querydsl.core.repository;

import com.querydsl.core.types.Predicate;
import com.wuxp.querydsl.core.domain.Pagination;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 * Extension of {@link CrudRepository} to provide additional methods to retrieve entities using the pagination and
 * sorting abstraction.
 *
 * @author wuxp
 * @see com.wuxp.querydsl.core.domain.Sort
 * @see com.wuxp.querydsl.core.domain.QSort
 * @see Pagination
 * @see Pageable
 */
public interface PagingAndSortingRepository<E, ID> extends CrudRepository<E, ID> {

    /**
     * Returns a {@link Pagination} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param pageable 分页信息
     * @return a page of entities
     */
    default Pagination<E> findAll(Pageable pageable) {
        return find(null, pageable);
    }

    /**
     * Returns a {@link Pagination} of entities meeting the paging restriction provided in the {@code Pageable} object.
     *
     * @param predicate 查询条件
     * @param pageable  分页信息
     * @return a page of entities
     */
    Pagination<E> find(Predicate predicate, Pageable pageable);

}
