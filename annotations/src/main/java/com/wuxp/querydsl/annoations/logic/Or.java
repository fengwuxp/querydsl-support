package com.wuxp.querydsl.annoations.logic;

import com.wuxp.querydsl.annoations.op.Op;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static com.wuxp.querydsl.annoations.AnnotationConstantVariables.NOT_NULL_CONDITION_EXPR;

/**
 * @author wuxp
 */
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Or {


    /**
     * 使用or连接的操作条件
     */
    Op[] value() default {};

    /**
     * 该注解生效的条件
     * spel 表达式，表达式的返回值必须为boolean值
     */
    String condition() default NOT_NULL_CONDITION_EXPR;

}
