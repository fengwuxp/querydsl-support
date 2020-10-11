package test.com.wuxp.querydsl.enums;

import com.wuxp.basic.enums.DescriptiveEnum;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品属性值类型
 *
 * @author wxup
 */
@Schema(description = "商品属性值类型")
public enum GoodsAttributeValueType implements DescriptiveEnum {


    @Schema(description = "int")
    INT,

    @Schema(description = "double")
    DOUBLE,

    @Schema(description = "boolean")
    BOOLEAN,

    @Schema(description = "字符串")
    STRING,

    @Schema(description = "字符串")
    JSON,

    @Schema(description = "枚举")
    ENUM
}
