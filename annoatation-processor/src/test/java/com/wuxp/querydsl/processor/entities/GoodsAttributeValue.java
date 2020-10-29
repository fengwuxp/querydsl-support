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
 * @author wxup
 */
@Table(name = "t_goods_attribute_value", indexes = {
        @Index(name = "index_goods_attribute_value_goods_id", columnList = "goods_id")
//        , @Index(name = "index_goods_attribute_value_attribute_id", columnList = "attribute_id")
})
@Entity
@Schema(description = "商品属性值")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsAttributeValue implements Serializable {


    private static final long serialVersionUID = -1617233320613669375L;

    @Schema(description = "属性值id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "属性名称")
    @Column(name = "name", nullable = false, length = 128)
    protected String name;

    @Schema(description = "属性值")
    @Column(name = "value", nullable = false, length = 128)
    private String value;

    @Schema(description = "值类型，可以按照值类型进行值转换")
    @Column(name = "value_type", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    private GoodsAttributeValueType valueType;

    @Schema(description = "关联的商品id")
    @Column(name = "goods_id", length = 16, nullable = false)
    private Long goodsId;

    @Schema(description = "关联的商品")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goods_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Goods goods;

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

    @Schema(description = "商品属性值描述")
    @Column(name = "description", length = 64)
    private String description;

    @Schema(description = "是否删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;

}
