package test.com.wuxp.querydsl.repositories;

import com.wuxp.querydsl.annoations.QueryDslRepository;
import com.wuxp.querydsl.annoations.op.Contains;
import test.com.wuxp.querydsl.entities.E_Goods;
import test.com.wuxp.querydsl.info.GoodsAndTypeInfo;

import java.util.List;

@QueryDslRepository
public interface GoodsRepository {


    List<GoodsAndTypeInfo> findGoodsTypeNames(@Contains(value = E_Goods.name) String goodsName);

}
