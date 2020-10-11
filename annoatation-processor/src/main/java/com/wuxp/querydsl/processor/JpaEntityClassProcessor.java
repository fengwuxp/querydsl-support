package com.wuxp.querydsl.processor;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.persistence.MappedSuperclass;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Set;

/**
 * 给实体类生成字段常量类
 *
 * @author wuxp
 * @see QueryDslRepositoryProcessor
 */
@SupportedAnnotationTypes(value = {"javax.persistence.Entity"})
public class JpaEntityClassProcessor extends AbstractProcessor {

    public static final String CLASS_NAME_PREFIX = "E_";


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

        for (Element element : elementList) {

            //只支持对类，接口，注解的处理，对字段不做处理
            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
            JavaFile.Builder javaFileBuilder = JavaFile.builder(packageName, this.buildTypeSpec(typeElement));
            try {
                javaFileBuilder.build().writeTo(this.processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private TypeSpec buildTypeSpec(TypeElement element) {
        final String newSimpleClassName = getNewSimpleClassName(element);
        TypeSpec.Builder builder = TypeSpec.classBuilder(newSimpleClassName)
                .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
        TypeMirror superTypeMirror = element.getSuperclass();
        int i = 5;
        String fullClassName = element.getQualifiedName().toString();
        while (i > 0) {
            i--;
            if (superTypeMirror instanceof NoType) {
                break;
            }
            Element supperElement = processingEnv.getTypeUtils().asElement(superTypeMirror);
            if (supperElement == null) {
                break;
            }
            TypeElement superTypeElement = (TypeElement) supperElement;
            if (Object.class.getName().equals(superTypeElement.getQualifiedName().toString())) {
                break;
            }
            if (superTypeElement.getAnnotation(MappedSuperclass.class) == null) {
                break;
            }
            // 遍历超类的属性
            this.addFields(superTypeElement, fullClassName, builder);
        }
        this.addFields(element, fullClassName, builder);
        return builder.build();
    }


    private void addFields(TypeElement element, String fullClassName, TypeSpec.Builder builder) {

        element.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
                .map(field -> {
                    FieldSpec fieldSpec = getFieldSpec(fullClassName, field);
                    return builder.fieldSpecs.contains(fieldSpec) ? null : fieldSpec;
                })
                .filter(Objects::nonNull)
                .forEach(builder::addField);
    }

    private FieldSpec getFieldSpec(String fullClassName, Element filed) {
        String filedName = filed.getSimpleName().toString();
        return FieldSpec.builder(String.class, filedName, Modifier.FINAL, Modifier.PUBLIC, Modifier.STATIC)
                .initializer("$S", String.format("%s#%s", fullClassName, filedName))
                .build();
    }


    private String getNewSimpleClassName(TypeElement element) {
        return CLASS_NAME_PREFIX + element.getSimpleName().toString();
    }

}
