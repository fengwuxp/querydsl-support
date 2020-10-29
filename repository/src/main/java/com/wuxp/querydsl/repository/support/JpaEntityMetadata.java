package com.wuxp.querydsl.repository.support;


import com.wuxp.querydsl.core.support.AbstractEntityMetadata;

import javax.persistence.Id;

/**
 * @author wuxp
 */
public class JpaEntityMetadata extends AbstractEntityMetadata{

    public JpaEntityMetadata(Class<?> entityClass) {
        super(entityClass, Id.class);
    }
}
