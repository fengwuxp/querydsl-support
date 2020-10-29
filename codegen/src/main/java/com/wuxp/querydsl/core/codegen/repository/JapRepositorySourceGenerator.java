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
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.wuxp.querydsl.core.codegen.soucecode.JavaSourceGenerator;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.tools.FileObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public class JapRepositorySourceGenerator implements JavaSourceGenerator {

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

    private String packageName;

    public JapRepositorySourceGenerator(String simpleName, FileObject fileObject) {
        this.simpleName = simpleName;
        this.fileObject = fileObject;
    }

    @Override
    public CompilationUnit generate() {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result;
        try {
            result = javaParser.parse(fileObject.openInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (!result.getResult().isPresent()) {
            return null;
        }
        CompilationUnit repositoryInterfaceUnit = result.getResult().get();
        Optional<PackageDeclaration> packageDeclaration = repositoryInterfaceUnit.getPackageDeclaration();
        if (!packageDeclaration.isPresent()) {
            return null;
        }
        String packageName = packageDeclaration.get().getNameAsString();
        this.packageName = packageName;

        Optional<ClassOrInterfaceDeclaration> repositoryInterfaceOptional = repositoryInterfaceUnit.getInterfaceByName(simpleName);
        if (!repositoryInterfaceOptional.isPresent()) {
            return null;
        }
        ClassOrInterfaceDeclaration repositoryInterface = repositoryInterfaceOptional.get();
        CompilationUnit implClass = new CompilationUnit();
        implClass.setPackageDeclaration(packageName);
        final ClassOrInterfaceDeclaration repositoryInterfaceImpl = implClass.addClass(this.getNewSimpleClassName(), com.github.javaparser.ast.Modifier.Keyword.PUBLIC);
        repositoryInterfaceUnit.getImports().forEach(importDeclaration -> {
            implClass.addImport(importDeclaration.getName().asString());
        });
        NEED_IMPORTS_CLASS_NAMES.forEach(implClass::addImport);
        String repositoryClassName = String.format("%s.%s", packageName, simpleName);
        repositoryInterfaceImpl.addImplementedType(repositoryClassName);
        List<TypeParameter> supperTypeParameters = addSupperClass(repositoryInterface, repositoryInterfaceImpl);
        Assert.notEmpty(supperTypeParameters, "超类上必须填写实体和ID的泛型");
        setConstructor(repositoryInterfaceImpl, repositoryClassName, supperTypeParameters.get(0).getNameAsString());

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
     * @param repositoryInterface     接口类
     * @param repositoryInterfaceImpl 待生成的实现类
     * @return 接口类上的泛型变量
     */
    private List<TypeParameter> addSupperClass(ClassOrInterfaceDeclaration repositoryInterface,
                                               ClassOrInterfaceDeclaration repositoryInterfaceImpl) {

        String repositorySupperClassName = "";
        if (!StringUtils.hasText(repositorySupperClassName)) {
            repositorySupperClassName = DEFAULT_SUPPER_REPOSITORY_CLASS;
        }
        ClassOrInterfaceType extendClass = new ClassOrInterfaceType();
        extendClass.setName(repositorySupperClassName);
        repositoryInterfaceImpl.addExtendedType(extendClass);

        // 查找 {@link com.wuxp.querydsl.repository.JpaRepository} 类上的类型变量
        NodeList<ClassOrInterfaceType> extendedTypes = repositoryInterface.getExtendedTypes();
        Optional<ClassOrInterfaceType> jpaRepositoryTypeOptional = extendedTypes.stream()
                .filter(classOrInterfaceType -> DEFAULT_JPA_REPOSITORY_CLASS_SIMPLE_NAME.equals(classOrInterfaceType.getNameAsString()))
                .findFirst();
        if (!jpaRepositoryTypeOptional.isPresent()) {
            return Collections.emptyList();
        }
        ClassOrInterfaceType jpaRepositoryType = jpaRepositoryTypeOptional.get();
        Optional<NodeList<Type>> typeArguments = jpaRepositoryType.getTypeArguments();
        if (!typeArguments.isPresent()) {
            return Collections.emptyList();
        }
        NodeList<Type> typeNodeList = typeArguments.get();
        extendClass.setTypeArguments(typeNodeList);

        return typeNodeList.stream().map(type -> {
            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) type;
            return new TypeParameter(classOrInterfaceType.getNameAsString());
        }).collect(Collectors.toList());
    }

    /**
     * 添加一个方法
     *
     * @param goodsRepositoryImpl {@link com.wuxp.querydsl.repository.Repository}的实现类
     * @param methodDeclaration   方法定义
     */
    private void implementMethod(ClassOrInterfaceDeclaration goodsRepositoryImpl, MethodDeclaration methodDeclaration) {
        NodeList<Modifier> modifiers = goodsRepositoryImpl.getModifiers();
        MethodDeclaration methodDeclarationImpl = goodsRepositoryImpl.addMethod(methodDeclaration.getName().toString(), modifiers.stream().map(com.github.javaparser.ast.Modifier::getKeyword).toArray(Modifier.Keyword[]::new));
        methodDeclarationImpl.addAndGetAnnotation(Override.class);
        methodDeclarationImpl.setType(methodDeclaration.getType());
        methodDeclarationImpl.setParameters(methodDeclaration.getParameters());

        BlockStmt body = new BlockStmt();
        body.addStatement("return null;");

        methodDeclarationImpl.setBody(body);
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
