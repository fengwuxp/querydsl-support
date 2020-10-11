//package com.wuxp.querydsl.processor;
//
//import com.squareup.javapoet.*;
//import com.sun.tools.javac.code.Symbol;
//import com.sun.tools.javac.code.Type;
//import com.sun.tools.javac.util.List;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.processing.AbstractProcessor;
//import javax.annotation.processing.RoundEnvironment;
//import javax.annotation.processing.SupportedAnnotationTypes;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.ElementKind;
//import javax.lang.model.element.Modifier;
//import javax.lang.model.element.TypeElement;
//import javax.lang.model.util.Elements;
//import javax.tools.Diagnostic;
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.util.Set;
//
///**
// * 用于给 {@link com.wuxp.querydsl.annoations.QueryDslRepository}标记的
// * 类或者接口生成更具体的实现
// * <pre>
// *     1：通过扫描接口或类的抽象方法进，得到方法的返回值和参数，按照既定（querydsl jpa）的标准进行生成实现类
// *     2：通过返回值类型和注解标记得到需要查询的列，通过参数和参数的注解组装查询条件
// * </pre>
// *
// * @author wuxp
// * @see JpaEntityClassProcessor
// */
//@SupportedAnnotationTypes("com.wuxp.querydsl.annoations.QueryDslRepository")
//public class QueryDslRepositoryProcessor_old extends AbstractProcessor {
//
//    private static final String CLASS_NAME_SUFFIX = "Impl";
//
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        for (String typeName : this.getSupportedAnnotationTypes()) {
//            Class<? extends Annotation> entityAnnotationType = null;
//            try {
//                entityAnnotationType = (Class<? extends Annotation>) Class.forName(typeName);
//            } catch (ClassNotFoundException e) {
//                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s", getClass().getSimpleName(), typeName));
//            }
//            if (entityAnnotationType == null) {
//                continue;
//            }
//            processAnnotations(roundEnv, roundEnv.getElementsAnnotatedWith(entityAnnotationType));
//        }
//
//        return false;
//    }
//
//    private void processAnnotations(RoundEnvironment roundEnv, Set<? extends Element> elementList) {
//
//        Elements elementUtils = this.processingEnv.getElementUtils();
//        for (Element element : elementList) {
//
//            //只支持对类，接口，注解的处理，对字段不做处理
//            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
//                continue;
//            }
//            TypeElement typeElement = (TypeElement) element;
//            final String newSimpleClassName = getNewSimpleClassName(typeElement);
//            final String packageName = elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
//            final String simpleClassName = element.getSimpleName().toString();
//            final String className = packageName + "." + simpleClassName;
////            Class<?> classType = this.getClassType(className);
////            Assert.notNull(classType, "加载类:" + className + "失败");
////
////            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("扫描到类:%s", classType.getName()));
//            TypeSpec.Builder builder = TypeSpec.classBuilder(newSimpleClassName)
//                    .addModifiers(Modifier.FINAL, Modifier.PUBLIC);
//            builder.addAnnotation(Component.class);
//            builder.addSuperinterface(element.asType());
//            this.addMethods(element, builder);
//            JavaFile.Builder javaFileBuilder = JavaFile.builder(packageName, builder.build());
//            try {
//                javaFileBuilder.build().writeTo(this.processingEnv.getFiler());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//
//    private void addMethods(Element element, TypeSpec.Builder builder) {
//        element.getEnclosedElements().stream()
//                .filter(e -> e.getKind() == ElementKind.METHOD)
//                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
//                .filter(e -> e.getModifiers().contains(Modifier.ABSTRACT))
//                .filter(e -> !e.getModifiers().contains(Modifier.FINAL))
//                .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
//                .filter(e -> !e.getModifiers().contains(Modifier.DEFAULT))
//                .map(e -> (Symbol.MethodSymbol) e)
//                .forEach(method -> {
//                    String name = method.getSimpleName().toString();
//                    MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(name);
//                    methodBuilder.addAnnotation(Override.class)
//                            .addModifiers(Modifier.PUBLIC);
//                    method.getParameters().forEach(parameter -> {
//                        Type.ClassType classType = (Type.ClassType) parameter.type;
//                        String parameterTypeName = classType.toString();
//                        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("parameterTypeName:%s", parameterTypeName));
////                        Class<?> classType = getClassType(parameterTypeName);
////                        ParameterSpec.Builder parameterBuilder = ParameterSpec.builder(
////                                classType,
////                                parameter.name.toString()
////                        );
////                        builderParameterAnnotations(parameter, parameterBuilder);
////                        methodBuilder.addParameter(parameterBuilder.build());
//                    });
//                    Type returnType = method.getReturnType();
//                    List<Type> parameterTypes = returnType.getParameterTypes();
//
//                    String returnTypeNames = returnType.toString();
//                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, returnTypeNames);
//
////                    returnType.asMethodType().argtypes;
////                    methodBuilder.returns()'
//
//                    builder.addMethod(methodBuilder.build());
//
//                });
//
//
//    }
//
//    private void builderParameterAnnotations(Symbol.VarSymbol parameter, ParameterSpec.Builder parameterBuilder) {
//        parameter.getAnnotationMirrors().forEach(compound -> {
//            Class<?> type = null;
//            String className = compound.type.asElement().getQualifiedName().toString();
//            try {
//                type = Class.forName(className);
//            } catch (ClassNotFoundException e) {
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "加载annotation：" + className + "失败");
//                return;
//            }
//            AnnotationSpec.Builder annotationBuilder = AnnotationSpec.builder(type);
//            compound.getElementValues().forEach((key, val) -> {
//                Object value = val.getValue();
//                annotationBuilder.addMember(key.name.toString(), "$S", value);
//            });
//            parameterBuilder.addAnnotation(annotationBuilder
//                    .build());
//        });
//    }
//
//    private Class<?> getClassType(String className) {
//
//        try {
//            return Class.forName(className);
//        } catch (ClassNotFoundException e) {
//            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, String.format("not found class:%s", className));
//            return null;
//        }
//    }
//
//    private String getNewSimpleClassName(TypeElement element) {
//        return element.getSimpleName().toString() + CLASS_NAME_SUFFIX;
//    }
//}
