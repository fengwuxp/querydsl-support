package com.wuxp.querydsl.annoations;

import java.util.function.Function;

/**
 * @author wuxp
 */
public final class AnnotationConstantVariables {

    public static final String DEFAULT_CONDITION_EXPR = "#_val!=b=null";

    public static final Function DEFAULT_FUNCTION=new Function() {
        @Override
        public Object apply(Object o) {
            return null;
        }
    };
}
