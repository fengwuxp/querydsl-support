package com.wuxp.querydsl.core.codegen.model;

import java.util.List;

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
}
