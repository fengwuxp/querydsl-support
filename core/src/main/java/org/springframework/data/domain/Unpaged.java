package org.springframework.data.domain;

import com.wuxp.querydsl.core.domain.QSort;
import com.wuxp.querydsl.core.domain.QueryType;

/**
 * {@link Pageable} implementation to represent the absence of pagination information.
 *
 * @author Oliver Gierke
 */
enum Unpaged implements Pageable {

    INSTANCE;

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#isPaged()
     */
    @Override
    public boolean isPaged() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#previousOrFirst()
     */
    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#next()
     */
    @Override
    public Pageable next() {
        return this;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#hasPrevious()
     */
    @Override
    public boolean hasPrevious() {
        return false;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getSort()
     */
    @Override
    public QSort getSort() {
        return QSort.unsorted();
    }

    @Override
    public QueryType getQueryType() {
        return QueryType.QUERY_BOTH;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getPageSize()
     */
    @Override
    public int getPageSize() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getPageNumber()
     */
    @Override
    public int getPageNumber() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#getOffset()
     */
    @Override
    public long getOffset() {
        throw new UnsupportedOperationException();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#first()
     */
    @Override
    public Pageable first() {
        return this;
    }
}
