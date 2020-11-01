package com.wuxp.querydsl.core.codegen.methods;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodMetadata;

/**
 * 解析方法定义：
 * <pre>
 *     1：得到方法的操作和对应操作的实体以及字段、生效的条件，用于构建查询表达式
 *     2：得到方法的需要操作实体（表）以及关联字段、生效条件、用于构建查询表达式
 *     3：得到方法的返回值关联的实体以及字段
 *     4：得到方法是否需要分页、排序、groupBy等
 *     5：得到方法上的锁标识（乐观锁、悲观锁）
 * </pre>
 *
 * @author wuxp
 */
public interface RepositoryMethodAnalyzer {


    /**
     * 解析一个方法的定义
     *
     * @param methodDeclaration 方法定义
     * @param metadata          方法的元数据信息
     */
    void analysis(MethodDeclaration methodDeclaration, RepositoryMethodMetadata metadata);
}
