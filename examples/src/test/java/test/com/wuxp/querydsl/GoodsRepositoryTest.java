package test.com.wuxp.querydsl;

import com.wuxp.querydsl.QueryDslApplicationTest;
import com.wuxp.querydsl.info.GoodsAndTypeInfo;
import com.wuxp.querydsl.repositories.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest(classes = QueryDslApplicationTest.class)
@Slf4j
public class GoodsRepositoryTest {

    @Autowired
    private GoodsRepository goodsRepository;

    @Test
    public void testFindGoodsTypeName() {
        List<GoodsAndTypeInfo> andTypeInfos = goodsRepository.findGoodsTypeName("手机");
        log.info("{}", andTypeInfos);

    }
}
