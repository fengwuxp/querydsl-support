package test.com.wuxp.querydsl.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * @author wxup
 */
@Schema(description = "商品尺寸相关信息")
@Embeddable
@Data
@NoArgsConstructor
public class GoodsSize implements Serializable {

    private static final long serialVersionUID = -5882245705821545835L;


    @Schema(description = "商品长度（CM）")
    @Column(name = "length")
    private Double length;

    @Schema(description = "商品宽度（CM）")
    @Column(name = "width")
    private Double width;

    @Schema(description = "商品高度（CM）")
    @Column(name = "height")
    private Double height;
}
