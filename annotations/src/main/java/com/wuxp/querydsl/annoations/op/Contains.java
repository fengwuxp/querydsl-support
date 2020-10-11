package com.wuxp.querydsl.annoations.op;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wuxp.querydsl.annoations.AnnotationConstantVariables.DEFAULT_CONDITION_EXPR;

/**
 * 是否包含
 * <ul>
 *     <li>1：如果是sql 则使用like</li>
 * </ul>
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Contains {

    /**
     * 用于查询的字段的 name
     */
    String value() default "";


    /**
     * 该注解生效的条件
     * spel 表达式，表达式的返回值必须为boolean值
     */
    String condition() default DEFAULT_CONDITION_EXPR;
}
