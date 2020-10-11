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
@Table(name = "t_goods_tag", indexes = {
        @Index(name = "index_goods_tag_goods_id", columnList = "goods_id"),
        @Index(name = "index_goods_tag_tag", columnList = "tag")
})
@Entity
@Schema(description = "商品标签")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class GoodsTag implements Serializable {

    private static final long serialVersionUID = -6047467797588634015L;

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

    @Schema(description = "标签内容")
    @Column(name = "tag", nullable = false, length = 32)
    private String tag;

    @Schema(description = "排序代码,默认：0")
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
}
