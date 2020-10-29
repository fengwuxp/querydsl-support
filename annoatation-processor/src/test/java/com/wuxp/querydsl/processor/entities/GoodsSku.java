package com.wuxp.querydsl.processor.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author wxup
 */
@Table(name = "t_sku_goods", indexes = {
        @Index(name = "index_sku_goods_goods_id", columnList = "goods_id")
})
@Entity
@Schema(description = "商品sku，维护商品库存，销量相关信息")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsSku implements Serializable {

    private static final long serialVersionUID = -6487860612450656442L;

    @Schema(description = "sku商品id")
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

    @Schema(description = "商品实际库存")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Schema(description = "累计销量")
    @Column(name = "sales", nullable = false)
    private Integer sales;

    @Schema(description = "商品虚拟库存")
    @Column(name = "virtual_stock", nullable = false)
    private Integer virtualStock;

    @Schema(description = "被锁定的虚拟库存数量，用于标记临时销量")
    @Column(name = "lock_virtual_stock", nullable = false)
    private Integer lockVirtualStock;

    @Schema(description = "库存警报，默认0，当商品库存小于等于该值，商品进入库存不足的状态")
    @Column(name = "stock_Threshold", nullable = false)
    private Integer stockThreshold;

    @Schema(description = "商品销售价格，单位：分")
    @Column(name = "price", nullable = false)
    private Integer price;

    @Schema(description = "sku主图")
    @Column(name = "sku_image")
    private String mainImage;

    @Schema(description = "最小购买量，默认1")
    @Column(name = "min_num", nullable = false)
    private Integer minNum;

    @Schema(description = "单次最大购买量，默认1，-1表示不限制")
    @Column(name = "max_num", nullable = false)
    private Integer maxNum;

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

    @Schema(description = "商品的sku属性")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "goodsSku", cascade = CascadeType.ALL)
    private Set<GoodsSkuAttribute> skuAttributes;

}
