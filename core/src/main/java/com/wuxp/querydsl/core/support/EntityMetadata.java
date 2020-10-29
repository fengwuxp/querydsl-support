package com.wuxp.querydsl.core.support;


import java.lang.reflect.Field;
import java.util.Optional;

/**
 * entity metadata
 *
 * @author wuxp
 */
public interface EntityMetadata {

    /**
     * get entity class
     *
     * @return
     */
   <E> Class<E> getDomainClass();

    /**
     * get marked by {@link javax.persistence.Id}  field
     *
     * @return not null
     */
    Optional<Field> getIdField();

    /**
     * get marked by {@link org.springframework.data.annotation.CreatedDate}  field
     *
     * @return not null
     */
    Optional<Field> getCreateTimeField();

    /**
     * get marked by {@link org.springframework.data.annotation.LastModifiedDate}  field
     *
     * @return null able
     */
    Optional<Field> getLastModifiedTimeField();

    /**
     * get marked by {@link org.springframework.data.annotation.CreatedBy}  field
     *
     * @return not null
     */
    Optional<Field> getCreateByField();

    /**
     * get marked by {@link org.springframework.data.annotation.LastModifiedBy}  field
     *
     * @return null able
     */
    Optional<Field> getLastModifiedByField();
}
