package com.wuxp.querydsl.core.domain;

import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Simple interface for entities.
 *
 * @param <ID>
 * @author wuxp
 */
public interface Persistable<ID> extends Serializable {

    /**
     * Returns the id of the entity.
     *
     * @return the id. Can be {@literal null}.
     */
    @Nullable
    ID getId();

    /**
     * create date time
     */
    LocalDateTime getCreateTime();

    /**
     * last modified time
     */
    LocalDateTime getLastModifiedTime();

    /**
     * set create date time on create
     */
    void setCreateTime(LocalDateTime createTime);

    /**
     * set last modified time on create and update
     */
    void setLastModifiedTime(LocalDateTime lastModifiedTime);

}
