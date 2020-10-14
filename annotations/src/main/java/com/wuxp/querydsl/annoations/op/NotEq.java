package com.wuxp.querydsl.annoations.op;


import com.wuxp.querydsl.annoations.constant.OperateConstantVariables;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wuxp.querydsl.annoations.AnnotationConstantVariables.NOT_NULL_CONDITION_EXPR;

/**
 * 查询操作标记注解，用于 "!=" 操作，可以标记在字段或方法上
 * <code>
 * xx!=#_val
 * </code>
 *
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Op(value = OperateConstantVariables.NOT_EQ)
public @interface NotEq {

    /**
     * 用于查询的字段的 name
     */
    String value() default "";


    /**
     * 该注解生效的条件
     * spel 表达式，表达式的返回值必须为boolean值
     */
    String condition() default NOT_NULL_CONDITION_EXPR;
}
