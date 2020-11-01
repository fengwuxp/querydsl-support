package com.wuxp.querydsl.core.codegen.model;

import java.util.List;
import java.util.Objects;

/**
 * 逻辑操作的描述对象，逻辑对象可以相互嵌套
 * <pre>
 *     xxx >? and xxx=? and (xx = ? or xxx like ?)
 * </pre>
 *
 * @author wuxp
 */
public class LogicalOperationMetadata {

    /**
     * 关联的列
     */
    private List<ColumnMetadata> columns;

    /**
     * 嵌套节点
     */
    private LogicalOperationMetadata children;

    /**
     * 逻辑操作类型
     */
    private String operation;

    public List<ColumnMetadata> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMetadata> columns) {
        this.columns = columns;
    }

    public LogicalOperationMetadata getChildren() {
        return children;
    }

    public void setChildren(LogicalOperationMetadata children) {
        this.children = children;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LogicalOperationMetadata that = (LogicalOperationMetadata) o;
        return Objects.equals(columns, that.columns) &&
                Objects.equals(children, that.children) &&
                Objects.equals(operation, that.operation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, children, operation);
    }
}
