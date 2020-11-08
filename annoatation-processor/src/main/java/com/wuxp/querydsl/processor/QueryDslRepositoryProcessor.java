package com.wuxp.querydsl.processor;

import com.wuxp.querydsl.core.codegen.CodeGenerator;
import com.wuxp.querydsl.core.codegen.RepositoryCodeGenProperties;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
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

    private final static String PROPERTIES_FILE_NAME = "repository-codegen.properties";

    private final static String PROPER_CODE_GENERATOR_NAME = "repository.codegen.name";

    private final static String DEFAULT_CODEGEN_CLASS_NAME = "com.wuxp.querydsl.core.codegen.repository.JapRepositoryGenerator";

    private final RepositoryCodeGenProperties repositoryCodeGenProperties = new RepositoryCodeGenProperties();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        File file = new File(PROPERTIES_FILE_NAME);
        Properties properties = new Properties();
        if (file.exists() && file.isFile()) {
            try {
                properties.load(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            } catch (Exception var4) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("%s 加载错误, %s", file.getAbsolutePath(), var4.getMessage()));
                throw new RuntimeException(String.format("读取配置文件错误-%s", file.getAbsolutePath()), var4);
            }
            this.injectProperties(properties);
        } else {
            repositoryCodeGenProperties.setRepositoryCodeGenClassName(DEFAULT_CODEGEN_CLASS_NAME);
        }
        super.init(processingEnv);

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement typeElement : annotations) {
            String className = typeElement.getQualifiedName().toString();
            try {
                Class<? extends Annotation> entityAnnotationType = (Class<? extends Annotation>) Class.forName(className);
                processAnnotations(roundEnv.getElementsAnnotatedWith(entityAnnotationType));
            } catch (ClassNotFoundException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s", getClass().getSimpleName(), className));
            }
        }
        return false;
    }


    protected void processAnnotations(Set<? extends Element> elementList) {
        Map<String, FileObject> sourceFiles = new HashMap<>(64);
        for (Element element : elementList) {
            //只支持对类，接口，注解的处理，对字段不做处理
            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
                continue;
            }
            FileObject resource = getSourceFileObject(element);
            if (resource != null) {
                String simpleName = element.getSimpleName().toString();
                sourceFiles.put(simpleName, resource);
            }
        }
        CodeGenerator codeGenerator = getCodeGenerator(sourceFiles);
        if (codeGenerator != null) {
            codeGenerator.generate();
        }
    }


    private FileObject getSourceFileObject(Element element) {
        Elements elementUtils = this.processingEnv.getElementUtils();
        Messager messager = this.processingEnv.getMessager();
        TypeElement typeElement = (TypeElement) element;
        final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        final String className = String.format("%s.%s", packageName, element.getSimpleName().toString());
        FileObject resource = null;
        try {
            resource = processingEnv.getFiler().getResource(StandardLocation.SOURCE_PATH, packageName, String.format("%s.java", element.getSimpleName().toString()));
        } catch (IOException e) {
            e.printStackTrace();
            messager.printMessage(Diagnostic.Kind.ERROR, String.format("find class source file error %s, %s", className, e));

        }
        return resource;
    }

    private CodeGenerator getCodeGenerator(Map<String, FileObject> fileObjects) {
        String repositoryCodeGenClassName = this.repositoryCodeGenProperties.getRepositoryCodeGenClassName();
        Class<? extends CodeGenerator> codeGenClass = null;
        try {
            codeGenClass = (Class<? extends CodeGenerator>) Class.forName(repositoryCodeGenClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Constructor<? extends CodeGenerator> constructor = null;
        try {
            constructor = codeGenClass.getConstructor(Map.class, ProcessingEnvironment.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        try {
            return constructor.newInstance(fileObjects, this.processingEnv);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void injectProperties(Properties properties) {
        RepositoryCodeGenProperties repositoryCodeGenProperties = this.repositoryCodeGenProperties;
        if (properties.get(PROPER_CODE_GENERATOR_NAME) != null) {
            repositoryCodeGenProperties.setRepositoryCodeGenClassName(properties.getProperty(PROPER_CODE_GENERATOR_NAME));
        } else {
            repositoryCodeGenProperties.setRepositoryCodeGenClassName(DEFAULT_CODEGEN_CLASS_NAME);
        }
    }

}
