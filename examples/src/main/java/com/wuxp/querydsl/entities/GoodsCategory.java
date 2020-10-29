package com.wuxp.querydsl.entities;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

/**
 * 商品分类
 *
 * @author wxup
 */
@Table(name = "t_goods_category",
        indexes = {@Index(name = "index_goods_category_name", columnList = "name")})
@Entity
@Schema(description = "商品分类")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString(exclude = {"goodsBrands"})
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class GoodsCategory {

    private static final long serialVersionUID = 5585243661858036175L;

    @Schema(description = "分类id，使用字母加数字组合")
    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Schema(description = "分类名称")
    @Column(name = "name")
    private String name;

    @Schema(description = "分类图标")
    @Column(name = "logo")
    private String logo;

    @Schema(description = "是否推荐分类")
    @Column(name = "is_recommend", nullable = false)
    private Boolean recommend;

    @Schema(description = "分类层级，默认从0开始")
    @Column(name = "level", nullable = false)
    private Integer level;

    @Schema(description = "是否已删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;

    @ManyToMany(mappedBy = "goodsCategories",fetch = FetchType.LAZY)
    @Schema(description = "关联的品牌列表")
    private Set<GoodsBrand> goodsBrands;

}
