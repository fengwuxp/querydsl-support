package com.wuxp.querydsl.annoations.sql;


import com.wuxp.querydsl.annoations.op.Op;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <code>
 * inner join xx_table
 * left join xx_table
 * right join xx_table
 * </code>
 *
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Join {

    /**
     * join的表
     * <p>
     * 实体类的全路径
     * example:
     * <code>
     * com.xxx.entity.XxxEntity
     * </code>
     * </p>
     */
    String value() default "";

    /**
     * 连接操作条件
     */
    Op[] on() default {};


    JoinType type() default JoinType.INNER;

    enum JoinType {

        INNER,

        LEFT,

        RIGHT,

        OUT
    }
}
