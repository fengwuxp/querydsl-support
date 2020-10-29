package com.wuxp.querydsl.processor.entities;

import com.wuxp.querydsl.processor.enums.GoodsDetailType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author wxup
 */
@Table(name = "t_goods_detail", indexes = {@Index(name = "index_goods_detail_goods_id", columnList = "goods_id")})
@Entity
@Schema(description = "商品详情")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsDetail {

    private static final long serialVersionUID = 685881979495229811L;

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

    @Schema(description = "详情内容,html详情，或者手机端详情的json数据")
    @Column(name = "content", nullable = false)
    @Lob
    private String content;

    @Schema(description = "商品详情类型")
    @Column(name = "detail_type", nullable = false, length = 12)
    @Enumerated(EnumType.STRING)
    private GoodsDetailType detailType;

    @Schema(description = "是否删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;
}
