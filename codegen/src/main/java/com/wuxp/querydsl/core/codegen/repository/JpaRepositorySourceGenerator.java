package com.wuxp.querydsl.core.codegen.repository;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.wuxp.querydsl.core.codegen.methods.DefaultJpaRepositoryMethodGenerator;
import com.wuxp.querydsl.core.codegen.methods.JpaRepositoryMethodGenerator;
import com.wuxp.querydsl.core.codegen.methods.NamingStrategyRepositoryMethodAnalyzer;
import com.wuxp.querydsl.core.codegen.methods.RepositoryMethodAnalyzer;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodCodeGenContext;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodMetadata;
import com.wuxp.querydsl.core.codegen.soucecode.JavaSourceGenerator;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 基于源代码的 jpa repository 生成器
 *
 * @author wuxp
 */
public class JpaRepositorySourceGenerator extends AbstractJavaSourceGenerator {

    /**
     * 默认的超类
     */
    private final static String DEFAULT_SUPPER_REPOSITORY_CLASS = "com.wuxp.querydsl.repository.jpa.AbstractJpaCrudRepository";

    private final static String DEFAULT_REPOSITORY_CLASS_SIMPLE_NAME = "JpaRepository";

    private final static String CLASS_NAME_SUFFIX = "Impl";

    private final static List<String> NEED_IMPORTS_CLASS_NAMES = Arrays.asList(
            "com.querydsl.jpa.JPQLQueryFactory",
            "javax.persistence.EntityManager"
    );


    public JpaRepositorySourceGenerator(String simpleName, FileObject fileObject, Filer filer) {
        super(simpleName, fileObject, filer);
    }

    @Override
    public CompilationUnit generate() {

        RepositoryMethodCodeGenContext repositoryMethodCodeGenContext = this.repositoryMethodCodeGenContext;
        ClassOrInterfaceDeclaration repositoryInterface = repositoryMethodCodeGenContext.getRepositoryInterface();

        CompilationUnit implClass = new CompilationUnit();
        implClass.setPackageDeclaration(packageName);
        final ClassOrInterfaceDeclaration repositoryInterfaceImpl = implClass.addClass(this.getNewSimpleClassName(), com.github.javaparser.ast.Modifier.Keyword.PUBLIC);
        repositoryInterfaceUnit.getImports().forEach(importDeclaration -> {
            implClass.addImport(importDeclaration.getName().asString());
        });
        NEED_IMPORTS_CLASS_NAMES.forEach(implClass::addImport);
        String repositoryClassName = String.format("%s.%s", packageName, simpleName);
        repositoryInterfaceImpl.addImplementedType(repositoryClassName);
        addSupperClass(repositoryInterfaceImpl);

        setConstructor(repositoryInterfaceImpl, repositoryClassName, repositoryMethodCodeGenContext.getDomain().getNameAsString());
        repositoryInterfaceImpl.addAnnotation(Repository.class);
        repositoryInterface.getMethods()
                .stream()
                .filter(methodDeclaration -> !methodDeclaration.isDefault())
                .forEach(methodDeclaration -> {
                    this.implementMethod(repositoryInterfaceImpl, methodDeclaration);
                });
        return implClass;
    }

    @Override
    protected String getRepositoryClassSimpleName() {
        return DEFAULT_REPOSITORY_CLASS_SIMPLE_NAME;
    }

    /**
     * 设置实现类的构造函数
     *
     * @param repositoryInterfaceImpl 待生成的实现类
     * @param repositoryClassName     接口类的全类名
     * @param entitySimpleName        实体类的SimpleName
     */
    private void setConstructor(ClassOrInterfaceDeclaration repositoryInterfaceImpl,
                                String repositoryClassName,
                                String entitySimpleName) {
        ConstructorDeclaration constructorDeclaration = repositoryInterfaceImpl.addConstructor(Modifier.Keyword.PUBLIC);
        constructorDeclaration.addParameter("JPQLQueryFactory", "jpqlQueryFactory");
        constructorDeclaration.addParameter("EntityManager", "entityManager");
        BlockStmt body = new BlockStmt();
        body.addStatement(String.format("super(jpqlQueryFactory, entityManager, %s.class,%s.class);", entitySimpleName, repositoryClassName));
        constructorDeclaration.setBody(body);
    }


    /**
     * 给实现类添加超类
     *
     * @param repositoryInterfaceImpl 待生成的实现类
     */
    private void addSupperClass(ClassOrInterfaceDeclaration repositoryInterfaceImpl) {
        ClassOrInterfaceType extendClass = new ClassOrInterfaceType();
        extendClass.setName(DEFAULT_SUPPER_REPOSITORY_CLASS);
        repositoryInterfaceImpl.addExtendedType(extendClass);

    }

    /**
     * 添加一个方法
     *
     * @param goodsRepositoryImpl {@link com.wuxp.querydsl.repository.Repository}的实现类
     * @param methodDeclaration   方法定义
     */
    private void implementMethod(ClassOrInterfaceDeclaration goodsRepositoryImpl, MethodDeclaration methodDeclaration) {
        NodeList<Modifier> modifiers = goodsRepositoryImpl.getModifiers();
        MethodDeclaration methodDeclarationImpl = goodsRepositoryImpl.addMethod(methodDeclaration.getName().toString(),
                modifiers.stream().map(Modifier::getKeyword).toArray(Modifier.Keyword[]::new));
        methodDeclarationImpl.addAndGetAnnotation(Override.class);
        Type type = methodDeclaration.getType();
        methodDeclarationImpl.setType(type);
        NodeList<Parameter> parameters = methodDeclaration.getParameters();
        methodDeclarationImpl.setParameters(parameters);

        // 解析方法 生成方法实现的描述元数据
        RepositoryMethodMetadata methodMetadata = new RepositoryMethodMetadata();
        for (RepositoryMethodAnalyzer methodAnalyzer : repositoryMethodAnalyzers) {
            if (methodAnalyzer.supports(methodDeclaration)) {
                methodAnalyzer.analysis(methodDeclaration, methodMetadata);
            }
        }
        // 移除参数上所有的注解
        parameters.forEach(parameter -> {
            parameter.setAnnotations(NodeList.nodeList());
        });
        jpaRepositoryMethodGenerator.generate(methodDeclarationImpl, methodMetadata);

    }


    protected String getNewSimpleClassName() {
        return String.format("%s%s", this.getSimpleName(), CLASS_NAME_SUFFIX);
    }

    public FileObject getFileObject() {
        return fileObject;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getPackageName() {
        return packageName;
    }
}
