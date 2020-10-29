package com.wuxp.querydsl.core.codegen.soucecode;

import com.github.javaparser.ast.CompilationUnit;

/**
 * @author wuxp
 */
public interface JavaSourceGenerator {



    /**
     * 生成
     */
    CompilationUnit generate();

}
