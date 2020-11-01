package com.wuxp.querydsl.core.codegen.model;

/**
 * join的操作元数据描述
 * <pre>
 *     left join a on a.xx = b.xx where a.xx=? and b.xx=?
 * </pre>
 *
 * @author wuxp
 */
public class JoinOperationMetadata {

    /**
     * 连接的目标
     */
    private String joinTarget;

    /**
     * 连接的操作类型
     */
    private String joinOp;

    /**
     * on 逻辑操作
     */
    private LogicalOperationMetadata onCondition;

    /**
     * 逻辑操作
     */
    private LogicalOperationMetadata whereCondition;
}
