package com.wuxp.querydsl.processor;

import org.junit.Test;

import java.io.File;
import java.util.Collections;

public class QueryDslRepositoryProcessorTest extends AbstractProcessorTest {

    private static final String PACKAGE_PATH = "src/test/java/com/wuxp/querydsl/processor/repositories/";

    @Test
    public void testProcess() throws Exception {
        String path = new File(PACKAGE_PATH, "GoodsRepository.java").getPath();
        process(QueryDslRepositoryProcessor.class, Collections.singletonList(path), "repositories");
    }
}