package com.wuxp.querydsl.annoations.map;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查找的表字段
 * 用于标记在DTO对象上的字段上或者 repository的方法返回值上
 *
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {

    /**
     * 查找的实体字段名称 通过全类名加上字段名称进行唯一标识
     */
    String value() default "";
}
