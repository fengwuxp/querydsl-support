package com.wuxp.querydsl.core.codegen.model;

import java.util.Objects;

/**
 * 列元数据信息
 * <pre>
 *     xx op ?
 * </pre>
 *
 * @author wuxp
 */
public class ColumnMetadata {

    /**
     * 操作的实体（表）
     * <pre>
     *     com.xx.xx.entities.XxxxEntity
     * </pre>
     */
    private String entity;

    /**
     * 表字段名称
     * 操作符的左侧边操作值
     */
    private String name;

    /**
     * 操作符
     * {@link com.wuxp.querydsl.annoations.constant.OperateConstantVariables}
     * {@link com.wuxp.querydsl.annoations.constant.SqlOperateConstantVariables}
     */
    private String op;

    /**
     * 操作符的右边操作值
     */
    private String right;


    /**
     * 字段生效查询表达式
     */
    private String conditionExpr;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getConditionExpr() {
        return conditionExpr;
    }

    public void setConditionExpr(String conditionExpr) {
        this.conditionExpr = conditionExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnMetadata that = (ColumnMetadata) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(name, that.name) &&
                Objects.equals(op, that.op) &&
                Objects.equals(right, that.right) &&
                Objects.equals(conditionExpr, that.conditionExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, name, op, right, conditionExpr);
    }

    @Override
    public String toString() {
        return "ColumnMetadata{" +
                "entity='" + entity + '\'' +
                ", name='" + name + '\'' +
                ", op='" + op + '\'' +
                ", right='" + right + '\'' +
                ", conditionExpr='" + conditionExpr + '\'' +
                '}';
    }
}
