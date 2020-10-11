package com.wuxp.querydsl.annoations.sql;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {

    String value() default "";

    /**
     * join table name
     */
    String table() default "";

    JoinType type() default JoinType.INNER;

    enum JoinType {

        INNER,

        LEFT,

        RIGHT,

        OUT
    }
}
