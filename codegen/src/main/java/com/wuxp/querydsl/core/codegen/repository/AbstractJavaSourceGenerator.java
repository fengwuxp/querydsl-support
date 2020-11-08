package com.wuxp.querydsl.core.codegen.repository;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.wuxp.querydsl.core.codegen.methods.DefaultJpaRepositoryMethodGenerator;
import com.wuxp.querydsl.core.codegen.methods.JpaRepositoryMethodGenerator;
import com.wuxp.querydsl.core.codegen.methods.NamingStrategyRepositoryMethodAnalyzer;
import com.wuxp.querydsl.core.codegen.methods.RepositoryMethodAnalyzer;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodCodeGenContext;
import com.wuxp.querydsl.core.codegen.soucecode.JavaSourceGenerator;
import org.springframework.util.Assert;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wuxp
 */
public abstract class AbstractJavaSourceGenerator implements JavaSourceGenerator {

    protected final JavaParser javaParser = new JavaParser();

    protected final String simpleName;

    protected final FileObject fileObject;

    protected final List<RepositoryMethodAnalyzer> repositoryMethodAnalyzers = new ArrayList<>();

    protected final JpaRepositoryMethodGenerator jpaRepositoryMethodGenerator;

    protected final Filer filer;

    protected String packageName;

    protected CompilationUnit repositoryInterfaceUnit;

    protected RepositoryMethodCodeGenContext repositoryMethodCodeGenContext;

    public AbstractJavaSourceGenerator(String simpleName, FileObject fileObject, Filer filer) {
        this.simpleName = simpleName;
        this.fileObject = fileObject;
        this.filer = filer;
        jpaRepositoryMethodGenerator = new DefaultJpaRepositoryMethodGenerator();
        this.initContext();
        this.initRepositoryMethodAnalyzers();
    }


    /**
     * 初始化上下文
     */
    protected void initContext() {
        this.repositoryInterfaceUnit = this.parseJavaSource(fileObject);

        Optional<PackageDeclaration> packageDeclaration = repositoryInterfaceUnit.getPackageDeclaration();
        Assert.isTrue(packageDeclaration.isPresent(), fileObject.getName() + "类的包名定义有误");
        this.packageName = packageDeclaration.get().getNameAsString();

        ClassOrInterfaceDeclaration repositoryInterface = this.findInterface(repositoryInterfaceUnit, simpleName);

        // 查找接口上继承的 repository超类
        final String repositoryClassSimpleName = getRepositoryClassSimpleName();
        NodeList<ClassOrInterfaceType> extendedTypes = repositoryInterface.getExtendedTypes();
        Optional<ClassOrInterfaceType> repositoryTypeOptional = extendedTypes.stream()
                .filter(classOrInterfaceType -> repositoryClassSimpleName.equals(classOrInterfaceType.getNameAsString()))
                .findFirst();
        Assert.isTrue(repositoryTypeOptional.isPresent(), String.format("未查找到类：%s的超类", simpleName));
        ClassOrInterfaceType jpaRepositoryType = repositoryTypeOptional.get();
        NodeList<Type> typeArguments = jpaRepositoryType.getTypeArguments().orElse(NodeList.nodeList());
        Assert.isTrue(typeArguments.size() == 2, "类：" + simpleName + "超类上的泛型变量,数量不等于2");

        ClassOrInterfaceType domainType = (ClassOrInterfaceType) typeArguments.get(0);
        // 查找实体类对象
        Optional<ImportDeclaration> domainTyeImportOptional = repositoryInterfaceUnit.getImports().stream()
                .filter(importDeclaration -> importDeclaration.getName().asString().endsWith("." + domainType.asString()))
                .findFirst();
        Assert.isTrue(domainTyeImportOptional.isPresent(), "未找到实体类：" + domainType.asString() + "的导入语句");
        FileObject entitySourceFile = getJavaSourceFile(domainTyeImportOptional.get().getName().asString());
        CompilationUnit entityCompilationUnit = this.parseJavaSource(entitySourceFile);
        String entityPackageName = entityCompilationUnit.getPackageDeclaration().orElse(new PackageDeclaration()).getNameAsString();
        this.repositoryMethodCodeGenContext = new RepositoryMethodCodeGenContext(
                findClass(entityCompilationUnit, domainType.asString()),
                entityPackageName,
                (ClassOrInterfaceType) typeArguments.get(1), repositoryInterface, jpaRepositoryType);
    }

    protected CompilationUnit parseJavaSource(FileObject fileObject) {
        ParseResult<CompilationUnit> result = null;
        try {
            result = javaParser.parse(fileObject.openInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.notNull(result, "类：" + fileObject.getName() + "未找到");
        }
        Optional<CompilationUnit> compilationUnit = result.getResult();
        Assert.isTrue(compilationUnit.isPresent(), fileObject.getName() + "类的定义有误，没有存在内容");
        return compilationUnit.get();
    }


    protected ClassOrInterfaceDeclaration findClass(CompilationUnit compilationUnit, String simpleName) {
        Optional<ClassOrInterfaceDeclaration> classOrInterfaceDeclaration = compilationUnit.getClassByName(simpleName);
        Assert.isTrue(classOrInterfaceDeclaration.isPresent(), "类：" + simpleName + "未找到");
        return classOrInterfaceDeclaration.get();
    }

    protected ClassOrInterfaceDeclaration findInterface(CompilationUnit compilationUnit, String simpleName) {
        Optional<ClassOrInterfaceDeclaration> classOrInterfaceDeclaration = compilationUnit.getInterfaceByName(simpleName);
        Assert.isTrue(classOrInterfaceDeclaration.isPresent(), "类：" + simpleName + "未找到");
        return classOrInterfaceDeclaration.get();
    }

    protected void initRepositoryMethodAnalyzers() {
        repositoryMethodAnalyzers.add(new NamingStrategyRepositoryMethodAnalyzer(repositoryMethodCodeGenContext));
    }

    protected FileObject getJavaSourceFile(String className) {
        int beginIndex = className.lastIndexOf(".");
        String fileName = className.substring(beginIndex + 1);
        String packageName = className.substring(0, beginIndex);
        try {
            return filer.getResource(StandardLocation.SOURCE_PATH, packageName, String.format("%s.java", fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected abstract String getRepositoryClassSimpleName();
}
