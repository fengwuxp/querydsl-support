package com.wuxp.querydsl.annoations;


/**
 * 注解相关常量
 *
 * @author wuxp
 */
public final class AnnotationConstantVariables {

    /**
     * 不为null生效的条件表达式
     */
    public static final String NOT_NULL_CONDITION_EXPR = "#_val!=null";

    /**
     * 为null生效的条件表达式
     */
    public static final String NULL_CONDITION_EXPR = "#_val==null";

    /**
     * 等于true生效的条件表达式
     */
    public static final String TRUE_CONDITION_EXPR = "#_val==true";

    /**
     * 等于false生效的条件表达式
     */
    public static final String FALSE_CONDITION_EXPR = "#_val==false";

}
