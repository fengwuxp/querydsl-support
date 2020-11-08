package com.wuxp.querydsl.core.codegen.repository;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
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

import javax.tools.FileObject;
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
public class JpaRepositorySourceGenerator implements JavaSourceGenerator {

    /**
     * 默认的超类
     */
    private final static String DEFAULT_SUPPER_REPOSITORY_CLASS = "com.wuxp.querydsl.repository.jpa.AbstractJpaCrudRepository";

    private final static String DEFAULT_JPA_REPOSITORY_CLASS_SIMPLE_NAME = "JpaRepository";

    private final static String CLASS_NAME_SUFFIX = "Impl";

    private final static List<String> NEED_IMPORTS_CLASS_NAMES = Arrays.asList(
            "com.querydsl.jpa.JPQLQueryFactory",
            "javax.persistence.EntityManager"
    );

    private final String simpleName;

    protected final FileObject fileObject;

    private final List<RepositoryMethodAnalyzer> repositoryMethodAnalyzers = new ArrayList<>();

    private final JpaRepositoryMethodGenerator jpaRepositoryMethodGenerator;

    private String packageName;

    private CompilationUnit repositoryInterfaceUnit;

    private RepositoryMethodCodeGenContext repositoryMethodCodeGenContext;

    public JpaRepositorySourceGenerator(String simpleName, FileObject fileObject) {
        this.simpleName = simpleName;
        this.fileObject = fileObject;
        jpaRepositoryMethodGenerator = new DefaultJpaRepositoryMethodGenerator();
        this.initContext();
        this.initRepositoryMethodAnalyzers();
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

        setConstructor(repositoryInterfaceImpl, repositoryClassName, repositoryMethodCodeGenContext.getDomainType().getNameAsString());
        repositoryInterfaceImpl.addAnnotation(Repository.class);
        repositoryInterface.getMethods()
                .stream()
                .filter(methodDeclaration -> !methodDeclaration.isDefault())
                .forEach(methodDeclaration -> {
                    this.implementMethod(repositoryInterfaceImpl, methodDeclaration);
                });
        return implClass;
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

    /**
     * 初始化上下文
     */
    private void initContext() {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result;
        try {
            result = javaParser.parse(fileObject.openInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Optional<CompilationUnit> unitOptional = result.getResult();
        Assert.isTrue(unitOptional.isPresent(), fileObject.getName() + "类的定义有误，没有存在内容");
        this.repositoryInterfaceUnit = unitOptional.get();
        Optional<PackageDeclaration> packageDeclaration = repositoryInterfaceUnit.getPackageDeclaration();
        Assert.isTrue(packageDeclaration.isPresent(), fileObject.getName() + "类的包名定义有误");
        this.packageName = packageDeclaration.get().getNameAsString();

        Optional<ClassOrInterfaceDeclaration> repositoryInterfaceOptional = repositoryInterfaceUnit.getInterfaceByName(simpleName);
        Assert.isTrue(repositoryInterfaceOptional.isPresent(), "类：" + simpleName + "未找到");
        ClassOrInterfaceDeclaration repositoryInterface = repositoryInterfaceOptional.get();
        // 查找 {@link com.wuxp.querydsl.repository.JpaRepository} 类上的类型变量
        NodeList<ClassOrInterfaceType> extendedTypes = repositoryInterface.getExtendedTypes();
        Optional<ClassOrInterfaceType> jpaRepositoryTypeOptional = extendedTypes.stream()
                .filter(classOrInterfaceType -> DEFAULT_JPA_REPOSITORY_CLASS_SIMPLE_NAME.equals(classOrInterfaceType.getNameAsString()))
                .findFirst();
        Assert.isTrue(jpaRepositoryTypeOptional.isPresent(), String.format("未查找到类：%s的超类", simpleName));
        ClassOrInterfaceType jpaRepositoryType = jpaRepositoryTypeOptional.get();
        NodeList<Type> typeArguments = jpaRepositoryType.getTypeArguments().orElse(NodeList.nodeList());
        Assert.isTrue(typeArguments.size() == 2, "类：" + simpleName + "超类上的泛型变量,数量不等于2");
        this.repositoryMethodCodeGenContext = new RepositoryMethodCodeGenContext((ClassOrInterfaceType) typeArguments.get(0),
                (ClassOrInterfaceType) typeArguments.get(1), repositoryInterface, jpaRepositoryType);
    }

    private void initRepositoryMethodAnalyzers() {
        repositoryMethodAnalyzers.add(new NamingStrategyRepositoryMethodAnalyzer(repositoryMethodCodeGenContext));
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
