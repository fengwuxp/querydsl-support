package com.wuxp.querydsl.core.codegen.model;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

/**
 * repository 方法生成的上下文对象
 *
 * @author wuxp
 */
public class RepositoryMethodCodeGenContext {

    /**
     * repository操作的领域模型对象
     */
    private final ClassOrInterfaceType domainType;

    /**
     * repository操作的领域模型对象的主键类型
     */
    private final ClassOrInterfaceType idType;

    /**
     * repository 接口定义
     */
    private final ClassOrInterfaceDeclaration repositoryInterface;

    /**
     * repository 接口继承的接口定义
     */
    private final ClassOrInterfaceType supperRepositoryType;


    public RepositoryMethodCodeGenContext(ClassOrInterfaceType domainType, ClassOrInterfaceType idType, ClassOrInterfaceDeclaration repositoryInterface, ClassOrInterfaceType supperRepositoryType) {
        this.domainType = domainType;
        this.idType = idType;
        this.repositoryInterface = repositoryInterface;
        this.supperRepositoryType = supperRepositoryType;
    }

    public ClassOrInterfaceType getDomainType() {
        return domainType;
    }

    public ClassOrInterfaceType getIdType() {
        return idType;
    }

    public ClassOrInterfaceDeclaration getRepositoryInterface() {
        return repositoryInterface;
    }

    public ClassOrInterfaceType getSupperRepositoryType() {
        return supperRepositoryType;
    }
}
