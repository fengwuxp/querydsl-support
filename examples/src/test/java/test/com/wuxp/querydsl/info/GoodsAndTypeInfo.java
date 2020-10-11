package test.com.wuxp.querydsl.info;

import com.wuxp.querydsl.annoations.map.Select;
import lombok.Data;
import test.com.wuxp.querydsl.entities.E_Goods;
import test.com.wuxp.querydsl.entities.E_GoodsCategory;

@Data
public class GoodsAndTypeInfo {

    @Select(E_Goods.id)
    private Long goodsId;

    @Select(E_Goods.name)
    private String goodsName;

    @Select(E_GoodsCategory.name)
    private String goodsCategoryName;
}
