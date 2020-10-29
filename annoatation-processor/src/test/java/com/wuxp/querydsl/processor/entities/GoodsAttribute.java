package com.wuxp.querydsl.processor.entities;

import com.wuxp.querydsl.processor.enums.GoodsAttributeValueType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 商品属性定义表
 *
 * @author wxup
 */
@Table(name = "t_goods_attribute", indexes = {
        @Index(name = "index_goods_attribute_name", columnList = "name"),
        @Index(name = "index_goods_attribute_category_id", columnList = "category_id"),
})
@Entity
@Schema(description = "商品属性")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsAttribute implements Serializable {

    private static final long serialVersionUID = 4339055896589126404L;

    @Schema(description = "属性id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "名称")
    @Column(name = "name", nullable = false, length = 128)
    protected String name;

    @Schema(description = "所属分类的id，如果为空表示所有商品都有的属性")
    @Column(name = "category_id")
    private String categoryId;

    @Schema(description = "可选值列表，如果有的话，使用json数组保存")
    @Column(name = "optional_value")
    private String optionalValue;

    @Schema(description = "值类型，可以按照值类型进行值转换")
    @Column(name = "value_type", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private GoodsAttributeValueType valueType;

    @Schema(description = "属性是否必填")
    @Column(name = "is_required", length = 128)
    private Boolean required;

    @Schema(description = "排序代码,默认：0")
    @Column(name = "order_code", nullable = false)
    protected Integer orderCode;

    @Schema(description = "创建时间")
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;

    @Schema(description = "更新时间")
    @Column(name = "last_update_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date lastUpdateTime;

    @Schema(description = "属性描述")
    @Column(name = "description")
    protected String description;


}
