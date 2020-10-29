package com.wuxp.querydsl.annoations.sql;

import com.wuxp.querydsl.annoations.logic.And;
import com.wuxp.querydsl.annoations.logic.Or;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <code>
 * where
 * </code>
 *
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Where {

    Or[] or() default {};

    And[] and() default {};

}
