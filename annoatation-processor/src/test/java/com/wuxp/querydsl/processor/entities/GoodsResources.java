package com.wuxp.querydsl.processor.entities;


import com.wuxp.querydsl.processor.enums.GoodsResourceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 存储商品相关资源，如图片，视频等
 *
 * @author wxup
 */
@Table(name = "t_goods_resources", indexes = {@Index(name = "index_goods_resources_goods_id", columnList = "goods_id")})
@Entity
@Schema(description = "商品资源")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsResources implements Serializable {

    private static final long serialVersionUID = -2558697821653405291L;

    @Schema(description = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "商品id")
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Schema(description = "商品")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Goods goods;

    @Schema(description = "资源内容，一般是url地址")
    @Column(name = "content", nullable = false)
    private String content;

    @Schema(description = "商品资源类型")
    @Column(name = "resource_type", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private GoodsResourceType resourceType;

    @Schema(description = "排序代码,默认：0，第0张为主图或主视频")
    @Column(name = "order_code", nullable = false)
    protected Integer orderCode;

    @Schema(description = "是否删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;

    @Schema(description = "创建时间")
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;

    @Schema(description = "更新时间")
    @Column(name = "last_update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateTime;


    /**
     * 默认图片
     *
     * @return
     */
    @Transient
    public boolean isDefault() {
        return this.orderCode == 0;
    }

}
