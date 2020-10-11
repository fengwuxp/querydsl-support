package test.com.wuxp.querydsl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import test.com.wuxp.querydsl.entities.Goods;
import test.com.wuxp.querydsl.entities.QGoods;
import test.com.wuxp.querydsl.entities.QGoodsCategory;
import test.com.wuxp.querydsl.entities.QGoodsTag;
import test.com.wuxp.querydsl.info.GoodsAndTypeInfo;
import test.com.wuxp.querydsl.req.QueryGoodsReq;

import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class QueryDslBuilderTest {

    @Mock
    private JPAQueryFactory queryFactory;

    @Test
    public void testSqlBuild() {

        //商品基本信息查询实体 子查询
        QGoods goods = QGoods.goods;
        List<Goods> goodsList = queryFactory
                .selectFrom(goods)
                //查询价格最大的商品列表
                .where(goods.price.eq(
                        JPAExpressions.select(goods.price.max()).from(goods)
                ))
//                .leftJoin(QGoodsBrand.goodsBrand)
                .fetch();
        log.info("{}", goodsList);


        //商品类型查询实体，俩张表关联
        QGoodsCategory goodsCategory = QGoodsCategory.goodsCategory;
        queryFactory
                .select(goods)
                .from(goods, goodsCategory)
                .where(
                        //为两个实体关联查询
                        goods.categoryId.eq(goodsCategory.id)
                                .and(
                                        //查询指定typeid的商品
                                        goodsCategory.id.eq("A00")
                                )
                )
                //根据排序字段倒序
                .orderBy(goods.recommendTop.desc())
                //执行查询
                .fetch();


        queryFactory.update(goods).set(goods.activityId, 1L).where().execute();
    }

    @Test
    public void testBuilderPredicate() {
        QueryGoodsReq req = new QueryGoodsReq();
        QGoods goods = QGoods.goods;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        JPAQuery<Goods> goodsJPAQuery = queryFactory
                .selectFrom(goods);

        if (req.getMaxPrice() != null) {
            booleanBuilder.and(
                    goods.price.eq(
                            JPAExpressions.select(goods.price.max()).from(goods)
                    ).or(goods.price.lt(req.getMaxPrice())));
        }
        if (req.getBrandId() != null) {
            booleanBuilder.and(
                    goods.brandId.eq(req.getBrandId())
            );
        }

        String searchName = req.getSearchName();
        if (searchName != null) {
            booleanBuilder.and(
                    goods.name.contains(searchName)
                            .or(goods.seoKeyword.contains(searchName))
            );
            QGoodsTag goodsTag = QGoodsTag.goodsTag;
            goodsJPAQuery.join(goodsTag)
                    .on(goodsTag.goodsId.eq(goods.id))
                    .on(goodsTag.tag.contains(searchName));
        }

        String searchTypeName = req.getSearchTypeName();
        if (searchTypeName != null) {
            QGoodsCategory goodsCategory = QGoodsCategory.goodsCategory;
            booleanBuilder.and(
                    goods.categoryId.in(
                            JPAExpressions.select(goodsCategory.id).from(goodsCategory)
                                    .where(goodsCategory.name.eq(searchName))
                    )
            );
        }


        List<Goods> goodsList = goodsJPAQuery
                .where(booleanBuilder)
                .groupBy(goods.type)
                .orderBy(goods.price.asc())
                .offset(10)
                .limit(5)
                //查询价格最大的商品列表
                .fetch();
    }

    @Test
    public void testCustomerResult() {
        List<GoodsAndTypeInfo> results = queryFactory.select(
                QGoods.goods.id,
                QGoods.goods.name,
                QGoodsCategory.goodsCategory.name)
                .from(QGoods.goods, QGoodsCategory.goodsCategory)
                .where(
                        QGoods.goods.categoryId.eq(QGoodsCategory.goodsCategory.id)
                                .and(QGoods.goods.name.contains("手机")))
                .fetch()
                .stream()
                .map(tuple -> {
                    GoodsAndTypeInfo goodsAndTypeInfo = new GoodsAndTypeInfo();
                    goodsAndTypeInfo.setGoodsId(tuple.get(QGoods.goods.id));
                    goodsAndTypeInfo.setGoodsName(tuple.get(QGoods.goods.name));
                    goodsAndTypeInfo.setGoodsCategoryName(tuple.get(QGoodsCategory.goodsCategory.name));
                    return goodsAndTypeInfo;
                }).collect(Collectors.toList());
    }

    @Test
    public void testQueryObjectBuilder() {
//        QueryDslBuilder builder = new QueryDslBuilder();
//
//        QueryGoodsReq queryGoodsReq = new QueryGoodsReq();
//        Predicate predicate = builder.getPredicate(QGoods.goods, queryGoodsReq);
    }
}
