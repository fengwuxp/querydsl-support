package test.com.wuxp.querydsl.enums;

import com.wuxp.basic.enums.DescriptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * @author wxup
 */
@Schema(description = "商品详情类型")
public enum GoodsDetailType implements DescriptiveEnum {

    @Schema(description = "html")
    HTML,

    @Schema(description = "json")
    JSON
}
