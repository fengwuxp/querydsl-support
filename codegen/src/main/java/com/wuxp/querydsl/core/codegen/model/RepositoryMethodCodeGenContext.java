package com.wuxp.querydsl.core.codegen.model;

import com.github.javaparser.ast.CompilationUnit;
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
    private final ClassOrInterfaceDeclaration domain;

    /**
     * repository操作的领域模型对象所在的包名
     */
    private final String domainPackageName;

    /**
     * repository操作的领域模型对象所在的包名 全类限定名称
     */
    private final String domainClassName;

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


    public RepositoryMethodCodeGenContext(ClassOrInterfaceDeclaration domain, String domainPackageName, ClassOrInterfaceType idType, ClassOrInterfaceDeclaration repositoryInterface, ClassOrInterfaceType supperRepositoryType) {
        this.domain = domain;
        this.domainPackageName = domainPackageName;
        this.domainClassName = domainPackageName + "." + domain.getNameAsString();
        this.idType = idType;
        this.repositoryInterface = repositoryInterface;
        this.supperRepositoryType = supperRepositoryType;
    }


    public ClassOrInterfaceDeclaration getDomain() {
        return domain;
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

    public String getDomainPackageName() {
        return domainPackageName;
    }

    public String getDomainClassName() {
        return domainClassName;
    }
}
