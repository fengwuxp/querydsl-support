package com.wuxp.querydsl.core.domain;


/**
 * 查询类型
 *
 * @author wxup
 */
public enum QueryType {

    /**
     * 查询总数
     */
    QUERY_NUM,

    /**
     * 查询结果集
     */
    QUERY_RESET,

    /**
     * 查询总数和结果集
     */
    QUERY_BOTH;

    public boolean isQueryNum() {
        return this.equals(QUERY_NUM) || this.equals(QUERY_BOTH);
    }

    public boolean isQueryResult() {
        return this.equals(QUERY_RESET) || this.equals(QUERY_BOTH);
    }
}
