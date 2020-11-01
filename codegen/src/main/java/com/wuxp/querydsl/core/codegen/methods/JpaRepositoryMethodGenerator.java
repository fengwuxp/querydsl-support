package com.wuxp.querydsl.core.codegen.methods;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodMetadata;

/**
 * 用于生成 jpa repository 方法的实现
 *
 * @author wuxp
 */
public interface JpaRepositoryMethodGenerator {


    /**
     * 生成
     *
     * @param methodDeclaration 方法实现定义
     * @param metadata          描述方法实现元数据
     */
    void generate(MethodDeclaration methodDeclaration, RepositoryMethodMetadata metadata);
}
