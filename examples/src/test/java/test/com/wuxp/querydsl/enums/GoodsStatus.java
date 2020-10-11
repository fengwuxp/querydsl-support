package test.com.wuxp.querydsl.enums;

import com.wuxp.basic.enums.DescriptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * @author wxup
 */
@Schema(description = "商品状态")
public enum GoodsStatus implements DescriptiveEnum {

    @Schema(description = "未发布")
    UNRELEASED,

    @Schema(description = "正常")
    NORMAL,

    @Schema(description = "下架")
    OFF_SHELF,

    @Schema(description = "违规下架")
    VIOLATION,


}
