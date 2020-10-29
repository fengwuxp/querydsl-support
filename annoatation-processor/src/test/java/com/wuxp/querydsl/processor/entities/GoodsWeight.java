package com.wuxp.querydsl.processor.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品重量
 *
 * @author wxup
 */
@Schema(description = "商品重量")
@Embeddable
@Data
@NoArgsConstructor
public class GoodsWeight implements Serializable {

    private static final long serialVersionUID = 4341844769192158387L;

    @Schema(description = "商品重量")
    @Column(name = "weight")
    protected BigDecimal weight;

    @Schema(description = "商品重量计量单位")
    @Column(name = "weight_unit_of_measure")
    protected String weightUnitOfMeasure;
}
