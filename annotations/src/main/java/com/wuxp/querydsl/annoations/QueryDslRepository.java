package com.wuxp.querydsl.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标记一个基于querydsl的 repository
 *
 * @author wuxp
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface QueryDslRepository {
}
