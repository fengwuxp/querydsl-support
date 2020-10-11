package test.com.wuxp.querydsl.entities;


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
@Table(name = "t_sku_attribute", indexes = {
        @Index(name = "index_sku_attribute_goods_id", columnList = "goods_id")
})
@Entity
@Schema(description = "商品sku属性")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsSkuAttribute implements Serializable {

    private static final long serialVersionUID = 1486001444789933884L;
    @Schema(description = "sku商品id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "sku属性名称")
    @Column(name = "name", nullable = false, length = 128)
    protected String name;

    @Schema(description = "商品id")
    @Column(name = "goods_id", nullable = false)
    private Long goodsId;

    @Schema(description = "商品sku id")
    @Column(name = "sku_id", nullable = false)
    private Long skuId;

    @Schema(description = "商品sku属性值")
    @Column(name = "value", nullable = false)
    private String value;

    @Schema(description = "是否删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;

    @Schema(description = "排序代码,默认：0")
    @Column(name = "order_code", nullable = false)
    protected Integer orderCode;

    @Schema(description = "创建时间")
    @Column(name = "create_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createTime;

    @Schema(description = "商品sku")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sku_id", referencedColumnName = "id", insertable = false, updatable = false)
    private GoodsSku goodsSku;
}
