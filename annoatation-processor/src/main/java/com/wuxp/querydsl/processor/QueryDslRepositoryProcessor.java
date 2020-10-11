package com.wuxp.querydsl.processor;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import org.springframework.stereotype.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

/**
 * 用于给 {@link com.wuxp.querydsl.annoations.QueryDslRepository}标记的
 * 类或者接口生成更具体的实现
 * <pre>
 *     1：通过扫描接口或类的抽象方法进，得到方法的返回值和参数，按照既定（querydsl jpa）的标准进行生成实现类
 *     2：通过返回值类型和注解标记得到需要查询的列，通过参数和参数的注解组装查询条件
 * </pre>
 *
 * @author wuxp
 * @see JpaEntityClassProcessor
 */
@SupportedAnnotationTypes("com.wuxp.querydsl.annoations.QueryDslRepository")
public class QueryDslRepositoryProcessor extends AbstractProcessor {

    private final static String CLASS_NAME_SUFFIX = "Impl";

    private final static String[] TEST_SOURCE_PATHS = {"examples", "src", "test", "java"};
    private final static String[] MAIN_SOURCE_PATHS = {"examples", "src", "main", "java"};

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (String typeName : this.getSupportedAnnotationTypes()) {
            Class<? extends Annotation> entityAnnotationType = null;
            try {
                entityAnnotationType = (Class<? extends Annotation>) Class.forName(typeName);
            } catch (ClassNotFoundException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s", getClass().getSimpleName(), typeName));
            }
            if (entityAnnotationType == null) {
                continue;
            }
            processAnnotations(roundEnv, roundEnv.getElementsAnnotatedWith(entityAnnotationType));
        }

        return false;
    }

    private void processAnnotations(RoundEnvironment roundEnv, Set<? extends Element> elementList) {
        Elements elementUtils = this.processingEnv.getElementUtils();
        Filer filer = this.processingEnv.getFiler();
        for (Element element : elementList) {

            //只支持对类，接口，注解的处理，对字段不做处理
            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            final String className = String.format("%s.%s", packageName, element.getSimpleName().toString());
            CompilationUnit compilationUnit = codeGenImpl(element, packageName, className);
            if (compilationUnit == null) {
                continue;
            }
            try {
                JavaFileObject sourceFile = filer.createSourceFile(String.format("%s.%s", packageName, this.getNewSimpleClassName(element)));
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, sourceFile.toUri().toString());
                Writer writer = sourceFile.openWriter();
                writer.write(compilationUnit.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("%s Processing  %s error, %s", this.getClass().getSimpleName(), className, e));

            }

        }
    }


    /**
     * 生成Repository的实现类
     *
     * @param element     扫描的类
     * @param packageName 包名
     * @param className   类全名称
     * @return
     */
    private CompilationUnit codeGenImpl(Element element, String packageName, String className) {
        JavaParser javaParser = new JavaParser();
        ParseResult<CompilationUnit> result = null;
        try {
            result = javaParser.parse(tryGetJavaFile(className));
        } catch (FileNotFoundException e) {
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
        goodsRepositoryImpl.addAnnotation(Component.class);
        goodsRepository.get().getMethods()
                .stream()
                .filter(methodDeclaration -> !methodDeclaration.isDefault())
                .forEach(methodDeclaration -> {
                    NodeList<com.github.javaparser.ast.Modifier> modifiers = goodsRepositoryImpl.getModifiers();
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

    private File tryGetJavaFile(String className) {
        File file = new File(this.getJavaFilePath(true, className));
        if (file.exists()) {
            return file;
        }
        return new File(this.getJavaFilePath(false, className));
    }

    private String getModuleDir(boolean isMain) {
        return Paths.get(System.getProperty("user.dir")).resolve(String.join(File.separator, isMain ? MAIN_SOURCE_PATHS : TEST_SOURCE_PATHS)).toString();
    }

    private String getJavaFilePath(boolean isMain, String className) {
        return String.format("%s%s%s.java", getModuleDir(isMain), File.separator, className.replaceAll("\\.", File.separator));
    }

    private String getNewSimpleClassName(Element element) {
        return String.format("%s%s", element.getSimpleName(), CLASS_NAME_SUFFIX);
    }
}
