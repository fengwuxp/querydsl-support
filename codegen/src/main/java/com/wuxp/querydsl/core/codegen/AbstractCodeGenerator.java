package com.wuxp.querydsl.core.codegen;

import com.github.javaparser.ast.CompilationUnit;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * @author wuxp
 */
public abstract class AbstractCodeGenerator implements CodeGenerator {

    protected final static String CLASS_NAME_SUFFIX = "Impl";

    protected final ProcessingEnvironment processingEnv;

    protected final RoundEnvironment roundEnv;

    /**
     * Annotation Processor 扫描到的注解
     */
    protected final Set<? extends TypeElement> annotations;

    public AbstractCodeGenerator(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, Set<? extends TypeElement> annotations) {
        this.processingEnv = processingEnv;
        this.roundEnv = roundEnv;
        this.annotations = annotations;
    }


    @Override
    public void generate() {
        for (TypeElement element : annotations) {
            Class<? extends Annotation> entityAnnotationType = null;
            String className = element.getQualifiedName().toString();
            try {
                entityAnnotationType = (Class<? extends Annotation>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s", getClass().getSimpleName(), className));
                continue;
            }
            processAnnotations(roundEnv.getElementsAnnotatedWith(entityAnnotationType));
        }
    }

    protected abstract CompilationUnit generateSingle(Element element, FileObject fileObject, String packageName, String className);

    protected void processAnnotations(Set<? extends Element> elementList) {
        Elements elementUtils = this.processingEnv.getElementUtils();
        Filer filer = this.processingEnv.getFiler();
        Messager messager = this.processingEnv.getMessager();
        for (Element element : elementList) {

            //只支持对类，接口，注解的处理，对字段不做处理
            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            final String className = String.format("%s.%s", packageName, element.getSimpleName().toString());
            FileObject resource = null;
            try {
                resource = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, packageName, String.format("%s.java", element.getSimpleName().toString()));
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("find class source file error %s, %s", className, e));
                continue;
            }
            CompilationUnit compilationUnit = generateSingle(element, resource, packageName, className);
            if (compilationUnit == null) {
                continue;
            }
            try {
                JavaFileObject sourceFile = filer.createSourceFile(String.format("%s.%s", packageName, this.getNewSimpleClassName(element)));
                messager.printMessage(Diagnostic.Kind.ERROR, sourceFile.toUri().toString());
                Writer writer = sourceFile.openWriter();
                writer.write(compilationUnit.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("%s Processing  %s error, %s", this.getClass().getSimpleName(), className, e));

            }

        }
    }


    protected String getNewSimpleClassName(Element element) {
        return String.format("%s%s", element.getSimpleName(), CLASS_NAME_SUFFIX);
    }
}
