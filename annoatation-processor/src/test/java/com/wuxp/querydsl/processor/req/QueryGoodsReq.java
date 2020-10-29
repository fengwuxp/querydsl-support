package com.wuxp.querydsl.processor.req;

import com.wuxp.querydsl.annoations.logic.Or;
import com.wuxp.querydsl.annoations.op.Eq;
import com.wuxp.querydsl.annoations.op.Op;
import com.wuxp.querydsl.processor.enums.GoodsStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 查询商品基础信息表
 * 2020年6月8日 下午5:15:11
 *
 * @author chen
 */
@Schema(description = "查询商品基础信息表")
@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
@Accessors(chain = true)
public class QueryGoodsReq {

    @Schema(description = "商品id")
    @Eq(value = "id")
    private Long id;

    /**
     * <pre>
     *    @Or(value={
     *          @Contains(value="name"),
     *          @Contains(value="seoKeyword"),
     *    })
     *   @LeftJoin(
     *        value={QGoodsTag.goodsTag},
     *        on={
     *       goodsTag.goodsId.eq(goods.id)
     *      },
     *      where={
     *         @Contains(value="tag"),
     *     })
     * </pre>
     */
    @Or(value = {
            @Op(value = "name", op = "like"),
            @Op(value = "seoKeyword", op = "like")
    })
    @Schema(description = "模糊查询：SN编号，商品名称")
    private String searchName;

    /**
     * <pre>
     *     @SubQuery(
     *         value={
     *             @In(value="categoryId")
     *         },
     *         @Query(
     *              from={QGoodsCategory.goodsCategory},
     *               value={"id"},
     *               where={
     *               @Eq(value={goodsCategory.name})
     *         })
     *     })
     * </pre>
     */
    @Schema(description = "通过商品分类名称查询商品")
    private String searchTypeName;

    @Schema(description = "名称")
    private String name;


    @Schema(description = "商品状态")
    private GoodsStatus status;

    @Schema(description = "商品类型")
    private String type;

    @Schema(description = "地区CODE")
    private String areaCode;

    @Schema(description = "店铺ID")
    private Long storeId;

    @Schema(description = "商品分类id")
    private String categoryId;

    @Schema(description = "品牌id")
    private Long brandId;

    @Schema(description = "品牌系列")
    private String seriesId;

    @Schema(description = "最小商品发布时间")
    private Date minPublishDate;

    @Schema(description = "最大商品发布时间")
    private Date maxPublishDate;

    @Schema(description = "最小创建时间")
    private Date minCreateTime;

    @Schema(description = "最大创建时间")
    private Date maxCreateTime;

    @Schema(description = "查询最低价")
    private Integer minPrice;

    @Schema(description = "查询最高价")
    private Integer maxPrice;

    @Schema(description = "是否删除")
    private Boolean deleted;

    @Schema(description = "是否加载商品的sku")
    private Boolean loadSku;

    public QueryGoodsReq() {
    }

    public QueryGoodsReq(Long id) {
        this.id = id;
    }
}
