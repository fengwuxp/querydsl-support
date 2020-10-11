package com.wuxp.querydsl.core.codegen;


/**
 * code generator
 * 在编译阶段通过{@link javax.annotation.processing.AbstractProcessor} 扫描得到期望生成的目标类或者接口
 *
 * @author wxup
 */
public interface CodeGenerator {

    /**
     * 生成
     */
    void generate();
}
