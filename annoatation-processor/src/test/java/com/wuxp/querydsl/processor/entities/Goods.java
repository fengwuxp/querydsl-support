package com.wuxp.querydsl.processor.entities;

import com.wuxp.querydsl.processor.enums.GoodsStatus;
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
 * 商品表
 *
 * @author wxup
 */
@Table(name = "t_goods",
        indexes = {
                @Index(name = "index_goods_name", columnList = "name"),
                @Index(name = "index_goods_store_id", columnList = "store_id"),
                @Index(name = "index_goods_category_id", columnList = "category_id"),
                @Index(name = "index_goods_brand_id", columnList = "brand_id"),
                @Index(name = "index_goods_area_code", columnList = "area_code")
        })
@Entity
@Schema(description = "商品基础信息表")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode( callSuper = true)
public class Goods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7974107442673134065L;

    @Schema(description = "商品货号")
    @Column(name = "sn", nullable = false, length = 32)
    private String sn;

    @Schema(description = "名称")
    @Column(name = "name", nullable = false, length = 128)
    protected String name;

    @Schema(description = "商品推荐")
    @Column(name = "recommend", nullable = false)
    private Boolean recommend = false;

    @Schema(description = "推荐排序")
    @Column(name = "recommend_top")
    private Integer recommendTop;

    @Schema(description = "商品类型")
    @Column(name = "goods_type", nullable = false)
    private String type;

    @Schema(description = "商品销售价格，单位：分，仅用于显示")
    @Column(name = "price", nullable = false)
    private Integer price;

    @Schema(description = "市场价，没有则与销售价相同，仅用于显示")
    @Column(name = "market_price", nullable = false)
    private Integer marketPrice;

    @Schema(description = "商品状态")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private GoodsStatus status;

    @Schema(description = "商品重量")
    @Embedded
//    @AttributeOverrides({
//            @AttributeOverride(name = "weight", column = @Column(name = "weight")),
//            @AttributeOverride(name = "weight_unit_of_measure", column = @Column(name = "weight_unit_of_measure"))}
//    )
    private GoodsWeight goodsWeight;

    @Schema(description = "商品尺寸")
    @Embedded
    private GoodsSize goodsSize;

    @Schema(description = "店铺ID")
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Schema(description = "商品分类id")
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    @Schema(description = "品牌id")
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Schema(description = "品牌系列")
    @Column(name = "series_id", length = 32)
    private String seriesId;

    @Schema(description = "地区ID")
    @Column(name = "area_code", length = 12)
    private String areaCode;

    @Schema(description = "商品主图")
    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @Schema(description = "商品主视频")
    @Column(name = "main_video")
    private String mainVideo;

    @Schema(description = "虚拟销量，用于初始情况,如果没有则为：0")
    @Column(name = "virtual_sales", nullable = false)
    private Integer virtualSales;


    @Schema(description = "商品发布时间")
    @Column(name = "publish_date", nullable = false, length = 19)
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @Schema(description = "附标题（广告语）")
    @Column(name = "slogan", length = 128)
    private String slogan;

    @Schema(description = "运费模板ID")
    @Column(name = "transport_id")
    private Long transportId;

    @Schema(description = "固定运费（分），0表示免运费")
    @Column(name = "shipping_fee", nullable = false)
    private Integer shippingFee;

    @Schema(description = "好评星级")
    @Column(name = "evaluation_star", nullable = false)
    private Float evaluationStar;

    @Schema(description = "评价数")
    @Column(name = "evaluation_count", nullable = false)
    private Integer evaluationCount;

    @Schema(description = "产地")
    @Column(name = "origin")
    private String origin;

    @Schema(description = "商品参加的活动的活动id")
    @Column(name = "activity_id")
    private Long activityId;

//    @Schema(description = "商品参加活动那个需要冻结的库存")
//    @Column(name = "activity_frozen_stock", nullable = false)
//    private Integer activityFrozenStock;

    @Schema(description = "商品查询搜索关键词,关键词用“#”包裹")
    @Column(name = "seo_keyword", length = 128)
    private String seoKeyword;

    @Schema(description = "商品资源")
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    @OrderBy("orderCode ASC")
    private Set<GoodsResources> resources;

    @Schema(description = "商品详情")
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    private Set<GoodsDetail> details;

    @Schema(description = "商品属性值")
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    @OrderBy("orderCode ASC")
    private Set<GoodsAttributeValue> attributeValues;

    @Schema(description = "商品标签")
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    @OrderBy("orderCode ASC")
    private Set<GoodsTag> tags;

    @Schema(description = "sku")
    @OneToMany(mappedBy = "goods", fetch = FetchType.LAZY)
    private Set<GoodsSku> skus;

}
