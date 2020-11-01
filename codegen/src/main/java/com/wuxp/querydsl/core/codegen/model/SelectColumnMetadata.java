package com.wuxp.querydsl.core.codegen.model;

import java.util.Objects;

/**
 * 查询的列对象
 *
 * @author wuxp
 */
public class SelectColumnMetadata {

    /**
     * 操作的实体（表）
     * <pre>
     *     com.xx.xx.entities.XxxxEntity
     * </pre>
     */
    private String entity;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 操作符
     * <pre>
     *     count\sum\avg
     * </pre>
     */
    private String op;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SelectColumnMetadata that = (SelectColumnMetadata) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(name, that.name) &&
                Objects.equals(op, that.op);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, name, op);
    }


    @Override
    public String toString() {
        return "SelectColumnMetadata{" +
                "entity='" + entity + '\'' +
                ", name='" + name + '\'' +
                ", op='" + op + '\'' +
                '}';
    }
}
