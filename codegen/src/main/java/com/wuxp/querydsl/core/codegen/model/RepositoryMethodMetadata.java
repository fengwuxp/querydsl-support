package com.wuxp.querydsl.core.codegen.model;

import java.util.List;
import java.util.Objects;

/**
 * repository 查询方法的元数据信息
 *
 * @author wuxp
 */
public class RepositoryMethodMetadata {


    /**
     * 查询的字段集合
     * <pre>
     *     select a.xxx,b.xxx,b.xx2
     *     select count(a.x),b.x
     * </pre>
     */
    private List<SelectColumnMetadata> selects;


    /**
     * where 条件
     * <pre>
     *    where a.xx=2 and b.xx=?
     * </pre>
     */
    private List<LogicalOperationMetadata> wheres;

    /**
     * 连接操作
     * <pre>
     *     left join a on a.x=b.x
     * </pre>
     */
    private List<JoinOperationMetadata> joins;


    /**
     * 排序的元数据信息
     * <pre>
     *     order by a.xx desc,a.xx2 asc
     * </pre>
     */
    private List<ColumnMetadata> orderByColumns;


    /**
     * group by的元数据信息
     * <pre>
     *     group by a.xx ,a.xx2
     * </pre>
     */
    private List<ColumnMetadata> groupByColumns;


    /**
     * having 支持
     * <pre>
     *     GROUP BY ProductID
     *     HAVING SUM(LineTotal) > $1000000.00
     *      AND AVG(OrderQty) < 3
     * </pre>
     */
    private List<ColumnMetadata> havingColumns;

    public List<SelectColumnMetadata> getSelects() {
        return selects;
    }

    public void setSelects(List<SelectColumnMetadata> selects) {
        this.selects = selects;
    }

    public List<LogicalOperationMetadata> getWheres() {
        return wheres;
    }

    public void setWheres(List<LogicalOperationMetadata> wheres) {
        this.wheres = wheres;
    }

    public List<JoinOperationMetadata> getJoins() {
        return joins;
    }

    public void setJoins(List<JoinOperationMetadata> joins) {
        this.joins = joins;
    }

    public List<ColumnMetadata> getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(List<ColumnMetadata> orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public List<ColumnMetadata> getGroupByColumns() {
        return groupByColumns;
    }

    public void setGroupByColumns(List<ColumnMetadata> groupByColumns) {
        this.groupByColumns = groupByColumns;
    }

    public List<ColumnMetadata> getHavingColumns() {
        return havingColumns;
    }

    public void setHavingColumns(List<ColumnMetadata> havingColumns) {
        this.havingColumns = havingColumns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RepositoryMethodMetadata that = (RepositoryMethodMetadata) o;
        return Objects.equals(selects, that.selects) &&
                Objects.equals(wheres, that.wheres) &&
                Objects.equals(joins, that.joins) &&
                Objects.equals(orderByColumns, that.orderByColumns) &&
                Objects.equals(groupByColumns, that.groupByColumns) &&
                Objects.equals(havingColumns, that.havingColumns);
    }

    @Override
    public int hashCode() {
        return Objects.hash(selects, wheres, joins, orderByColumns, groupByColumns, havingColumns);
    }
}
