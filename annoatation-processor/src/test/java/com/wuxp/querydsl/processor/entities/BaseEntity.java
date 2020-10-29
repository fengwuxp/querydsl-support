package com.wuxp.querydsl.processor.entities;

import com.wuxp.querydsl.core.domain.Persistable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author wuxp
 */
@MappedSuperclass
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public abstract class BaseEntity implements Persistable<Long> {

    @Schema(description = "商品id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Schema(description = "创建时间")
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime createTime;

    @Schema(description = "更新时间")
    @Column(name = "last_modified_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected LocalDateTime lastModifiedTime;

    @Schema(description = "是否删除")
    @Column(name = "is_deleted", nullable = false)
    protected Boolean deleted;

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
