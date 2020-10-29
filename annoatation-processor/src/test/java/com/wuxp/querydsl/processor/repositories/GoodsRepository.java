package com.wuxp.querydsl.processor.repositories;

import com.wuxp.querydsl.annoations.QueryDslRepository;
import com.wuxp.querydsl.annoations.op.Contains;
import com.wuxp.querydsl.processor.entities.Goods;
import com.wuxp.querydsl.processor.info.GoodsAndTypeInfo;
import com.wuxp.querydsl.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * @author wuxp
 */
@QueryDslRepository
public interface GoodsRepository extends JpaRepository<Goods, Long> {


    List<GoodsAndTypeInfo> findGoodsTypeName(@Contains(value = "") String goodsName);

    @Lock(LockModeType.OPTIMISTIC)
    @Override
    Optional<Goods> findById(Long id);
}
