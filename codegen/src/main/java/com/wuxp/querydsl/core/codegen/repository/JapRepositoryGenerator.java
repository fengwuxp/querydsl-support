package com.wuxp.querydsl.core.codegen.repository;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.wuxp.querydsl.core.codegen.AbstractCodeGenerator;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

/**
 * @author wuxp
 */
public class JapRepositoryGenerator extends AbstractCodeGenerator {


    public JapRepositoryGenerator(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, Set<? extends TypeElement> annotations) {
        super(processingEnv, roundEnv, annotations);
    }


    /**
     * 生成Repository的实现类
     *
     * @param element     扫描的类
     * @param fileObject  扫描的类的源代码文件
     * @param packageName 包名
     * @param className   类全名称
     * @return 生成的实现类
     */
    @Override
    protected CompilationUnit generateSingle(Element element, FileObject fileObject, String packageName, String className) {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result = null;
        try {
            result = javaParser.parse(fileObject.openInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("%s  can't found java file %s", getClass().getSimpleName(), className));
            return null;
        }
        if (!result.getResult().isPresent()) {
            return null;
        }
        final String simpleClassName = element.getSimpleName().toString();
        CompilationUnit repositoryInterface = result.getResult().get();
        Optional<ClassOrInterfaceDeclaration> goodsRepository = repositoryInterface.getInterfaceByName(simpleClassName);
        if (!goodsRepository.isPresent()) {
            return null;
        }
        CompilationUnit implClass = new CompilationUnit();
        implClass.setPackageDeclaration(packageName);
        ClassOrInterfaceDeclaration goodsRepositoryImpl = implClass.addClass(this.getNewSimpleClassName(element), com.github.javaparser.ast.Modifier.Keyword.PUBLIC);
        repositoryInterface.getImports().forEach(importDeclaration -> {
            implClass.addImport(importDeclaration.getName().asString());
        });
        goodsRepositoryImpl.addImplementedType(className);
        goodsRepositoryImpl.addAnnotation(Repository.class);
        goodsRepository.get().getMethods()
                .stream()
                .filter(methodDeclaration -> !methodDeclaration.isDefault())
                .forEach(methodDeclaration -> {
                    NodeList<Modifier> modifiers = goodsRepositoryImpl.getModifiers();
                    MethodDeclaration methodDeclarationImpl = goodsRepositoryImpl.addMethod(methodDeclaration.getName().toString(), modifiers.stream().map(com.github.javaparser.ast.Modifier::getKeyword).toArray(Modifier.Keyword[]::new));
                    methodDeclarationImpl.addAndGetAnnotation(Override.class);
                    methodDeclarationImpl.setType(methodDeclaration.getType());
                    methodDeclarationImpl.setParameters(methodDeclaration.getParameters());

                    BlockStmt body = new BlockStmt();
                    body.addStatement("return null;");

                    methodDeclarationImpl.setBody(body);
                });
        return implClass;
    }

}
