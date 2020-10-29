package com.wuxp.querydsl.annoations.sql;

import com.wuxp.querydsl.annoations.op.Op;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 内连接
 * <code>
 *     inner join
 * </code>
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Join(type = Join.JoinType.INNER)
public @interface InnerJoin {

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
}
