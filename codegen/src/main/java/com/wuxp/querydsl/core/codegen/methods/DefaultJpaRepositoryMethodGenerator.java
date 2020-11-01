package com.wuxp.querydsl.core.codegen.methods;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodMetadata;

/**
 * 默认的方法实现生成器
 *
 * @author wuxp
 */
public class DefaultJpaRepositoryMethodGenerator implements JpaRepositoryMethodGenerator {

    @Override
    public void generate(MethodDeclaration methodDeclaration, RepositoryMethodMetadata metadata) {

        BlockStmt body = new BlockStmt();
        body.addStatement("return null;");
        methodDeclaration.setBody(body);
    }
}
