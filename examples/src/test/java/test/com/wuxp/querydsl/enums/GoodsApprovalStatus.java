package test.com.wuxp.querydsl.enums;

import com.wuxp.basic.enums.DescriptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * @author wxup
 */
@Schema(description = "商品审核状态")
public enum GoodsApprovalStatus implements DescriptiveEnum {


    @Schema(description = "审核中")
    PENDING,

    @Schema(description = "审核通过")
    APPROVED,

    @Schema(description = "拒绝")
    REFUSE;

}
