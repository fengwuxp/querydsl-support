package com.wuxp.querydsl.core.codegen.methods;

import com.wuxp.querydsl.core.codegen.model.RepositoryMethodCodeGenContext;

/**
 * RepositoryMethodAnalyzer 的抽象实现
 *
 * @author wuxp
 */
public abstract class AbstractRepositoryMethodAnalyzer implements RepositoryMethodAnalyzer {


    private final RepositoryMethodCodeGenContext repositoryMethodCodeGenContext;

    public AbstractRepositoryMethodAnalyzer(RepositoryMethodCodeGenContext repositoryMethodCodeGenContext) {
        this.repositoryMethodCodeGenContext = repositoryMethodCodeGenContext;
    }
}
