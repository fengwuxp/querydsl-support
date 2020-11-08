package com.wuxp.querydsl.processor.info;

import com.wuxp.querydsl.annoations.map.Select;
import lombok.Data;

@Data
public class GoodsAndTypeInfo {

    @Select("")
    private Long goodsId;

    @Select("")
    private String goodsName;

    @Select("")
    private String goodsCategoryName;

}
