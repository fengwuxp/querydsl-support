package com.wuxp.querydsl.annoations.op;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wuxp.querydsl.annoations.AnnotationConstantVariables.NOT_NULL_CONDITION_EXPR;

/**
 * 用于定义一个比较的操作
 *
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Op {

    /**
     * 用于查询的字段的 name
     */
    String value() default "";


    /**
     * 操作符
     */
    String op() default "";


    /**
     * 该注解生效的条件
     * spel 表达式，表达式的返回值必须为boolean值
     */
    String condition() default NOT_NULL_CONDITION_EXPR;
}
