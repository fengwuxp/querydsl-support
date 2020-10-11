package test.com.wuxp.querydsl.enums;

import com.wuxp.basic.enums.DescriptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * @author wxup
 */
@Schema(description = "商品资源类型")
public enum GoodsResourceType implements DescriptiveEnum {

    @Schema(description = "图片")
    IMAGE,

    @Schema(description = "视频")
    VIDEO,

}
