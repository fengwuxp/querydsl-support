package com.wuxp.querydsl.processor;

import org.junit.Test;

import java.util.List;

public class JpaEntityClassProcessorTest extends AbstractProcessorTest {

    private static final String PACKAGE_PATH = "src/test/java/com/wuxp/querydsl/processor/entities/";

    private static final List<String> CLASSES = getFiles(PACKAGE_PATH);

    @Test
    public void testProcess() throws Exception {
        process(JpaEntityClassProcessor.class, CLASSES, "entities");
    }

}