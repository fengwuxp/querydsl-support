package com.wuxp.querydsl.annoations.map;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 更新的的表字段
 * 用于标记在DTO对象上或者 repository的方法参数上
 * <p>
 * 默认情况只有在属性不为null时进行更新，若需要更新null{@link #updateOnNull()}
 * </p>
 *
 * @author wuxp
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Update {

    /**
     * 查找的实体字段名称 通过全类名加上字段名称进行唯一标识
     */
    String value() default "";

    /**
     * 当前值为null是是否进行更新，如果<code>true</code> 则表示当前值为null的情况下也会进行更新
     */
    boolean updateOnNull() default false;
}
