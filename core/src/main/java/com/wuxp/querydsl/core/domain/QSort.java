package com.wuxp.querydsl.core.domain;

import com.querydsl.core.types.OrderSpecifier;

import java.util.Arrays;
import java.util.List;

/**
 * 基于querydsl的排序
 *
 * @author wuxp
 */
public class QSort implements Sort {

    private static final long serialVersionUID = 6560227634299607183L;

    private static final QSort UNSORTED = new QSort();

    private final List<OrderSpecifier<?>> orderSpecifiers;


    /**
     * Creates a new {@link QSort} instance with the given {@link OrderSpecifier}s.
     *
     * @param orderSpecifiers must not be {@literal null} .
     */
    public QSort(OrderSpecifier<?>... orderSpecifiers) {
        this(Arrays.asList(orderSpecifiers));
    }

    /**
     * Creates a new {@link QSort} instance with the given {@link OrderSpecifier}s.
     *
     * @param orderSpecifiers must not be {@literal null}.
     */
    public QSort(List<OrderSpecifier<?>> orderSpecifiers) {
        this.orderSpecifiers = orderSpecifiers;
    }

    public static QSort by(OrderSpecifier<?>... orderSpecifiers) {
        return new QSort(orderSpecifiers);
    }

    public static QSort unsorted() {
        return UNSORTED;
    }


    /**
     * @return the orderSpecifier
     */
    public List<OrderSpecifier<?>> getOrderSpecifiers() {
        return orderSpecifiers;
    }

    @Override
    public boolean isSorted() {
        return !orderSpecifiers.isEmpty();
    }
}
