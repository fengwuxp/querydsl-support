package com.wuxp.querydsl.processor;

import com.wuxp.querydsl.core.codegen.repository.JapRepositoryGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
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

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        new JapRepositoryGenerator(this.processingEnv, roundEnv, annotations).generate();
        return false;
    }


}
