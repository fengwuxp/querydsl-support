package test.com.wuxp.querydsl.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 商品品牌系列
 *
 * @author wxup
 */
@Table(name = "t_goods_brand_series",
        indexes = {
                @Index(name = "index_brand_series_name", columnList = "name"),
                @Index(name = "index_brand_id", columnList = "brand_id"),
        })
@Entity
@Schema(description = "品牌系列")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class GoodsBrandSeries {

    private static final long serialVersionUID = 6545635551500578159L;

    @Schema(description = "系列id")
    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Schema(description = "系列log")
    @Column(name = "logo", nullable = false)
    private String logo;

    @Schema(description = "是否推荐的系列")
    @Column(name = "is_recommend", nullable = false)
    private Boolean recommend;

    @Schema(description = "系列层级，默认从0开始")
    @Column(name = "level", nullable = false)
    private Integer level;

    @Schema(description = "归属的品牌Id")
    @Column(name = "brand_id", nullable = false)
    private Long brandId;

    @Schema(description = "归属的品牌")
    @JoinColumn(referencedColumnName = "id", name = "brand_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private GoodsBrand goodsBrand;

    @Schema(description = "是否已删除")
    @Column(name = "is_deleted", nullable = false)
    private Boolean deleted;
}
