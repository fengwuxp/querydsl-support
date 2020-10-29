package com.wuxp.querydsl.processor.entities;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 商品品牌
 *
 * @author wxup
 */
@Table(name = "t_goods_brand", indexes = {@Index(name = "index_goods_brand_name", columnList = "name")})
@Entity
@Schema(description = "商品品牌")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString(exclude = "goodsCategories", callSuper = true)
@EqualsAndHashCode(of = {"id"})
public class GoodsBrand implements Serializable {


    private static final long serialVersionUID = -8067782755309830809L;

    @Schema(description = "品牌id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "名称")
    @Column(name = "name", nullable = false)
    protected String name;

    @Schema(description = "品牌log")
    @Column(name = "logo", nullable = false)
    private String logo;

    @Schema(description = "是否推荐品牌")
    @Column(name = "is_recommend", nullable = false)
    private Boolean recommend;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = GoodsCategory.class)
    @Schema(description = "关联的分类列表")
    @JoinTable(
            // 用来指定中间表的名称
            name = "t_goods_brand_category",
            // joinColumns,当前对象在中间表中的外键
            joinColumns = {@JoinColumn(name = "brand_id", referencedColumnName = "id")},
            // inverseJoinColumns，对方对象在中间表的外键
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")}
    )
    private Set<GoodsCategory> goodsCategories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "goodsBrand", cascade = CascadeType.ALL)
    private Set<GoodsBrandSeries> brandSeries;

    @Schema(description = "是否已删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;

    @Schema(description = "排序代码，默认0")
    @Column(name = "order_code", nullable = false)
    private Integer orderCode;

    @Schema(description = "是否允许")
    @Column(name = "is_enable", nullable = false)
    private Boolean enabled = true;

    @Schema(description = "创建时间")
    @Column(name = "create_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;

    @Schema(description = "更新时间")
    @Column(name = "last_update_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastUpdateTime;

}
