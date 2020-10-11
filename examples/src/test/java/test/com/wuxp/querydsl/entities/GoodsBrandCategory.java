package test.com.wuxp.querydsl.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author wxup
 */
@Table(name = "t_goods_brand_category")
@Entity
@Schema(description = "商品分类品牌关系表")
@Data
@Accessors(chain = true)
public class GoodsBrandCategory implements Serializable {


    private static final long serialVersionUID = -7198893053744805269L;

    @EmbeddedId
    private GoodsBrandCategoryPrimaryKey categoryPrimaryKey;


    @Embeddable
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsBrandCategoryPrimaryKey implements Serializable {

        private static final long serialVersionUID = -5227637028053610545L;


        @Schema(description = "分类id")
        @Column(name = "category_id", length = 20, nullable = false)
        private String categoryId;

        @Schema(description = "品牌id")
        @Column(name = "brand_id", length = 20, nullable = false)
        private Long brandId;


    }
}
