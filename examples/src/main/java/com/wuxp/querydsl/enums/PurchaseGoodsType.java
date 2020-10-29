package com.wuxp.querydsl.enums;

import com.wuxp.basic.enums.DescriptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 售卖商品的类型
 *
 * @author wxup
 */
@Schema(description = "售卖商品的类型")
public enum PurchaseGoodsType implements DescriptiveEnum {

    @Schema(description = "sku")
    SKU,

    @Schema(description = "限时抢购")
    SNAPPED_UP
}
