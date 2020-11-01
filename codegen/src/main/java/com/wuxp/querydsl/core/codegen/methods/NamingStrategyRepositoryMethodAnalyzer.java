package com.wuxp.querydsl.core.codegen.methods;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodMetadata;

/**
 * 按照命名策略进行方法实现的生成
 * 参考spring data jpa 文档 https://docs.spring.io/spring-data/jpa/docs/2.0.9.RELEASE/reference/html/#jpa.query-methods.query-creation
 * <pre>
 *     额外增强：
 *     1：支持在find  get 关键字后面 By关键字之前声明需要查询的列，建议不要超过三个（太多将导致方法名过长，可读性太差），多列的查询，请使用注解声明
 *      findXxxByXx ==> findNameById  findNameAgeById  返回值可以是 Number\Boolean\String\List\Set\Map 或者是对象
 *     example:
 *     String findNameById(Long id)
 *     Map<String,Object> findNameAgeById(Long id)
 *     List<Map<String,Object>> findNameSexByAagLessThan(int age)
 *     List<Object[]> findNameSexByAagLessThan(int age)
 *     List<{name:string,sex:Sex}> findNameSexByAagLessThan(int age)
 *
 *     2：支持如下关键字缩写：
 *     LessThan ==> Lt
 *     LessThanEqual ==> Lte
 *     GreaterThan ==> Gt
 *     GreaterThanEqual ==> Gte
 *
 *     3：GroupBy支持
 *     example:
 *     findUserGroupByName
 * </pre>
 *
 * @author wuxp
 */
public class NamingStrategyRepositoryMethodAnalyzer implements RepositoryMethodAnalyzer {

    @Override
    public void analysis(MethodDeclaration methodDeclaration, RepositoryMethodMetadata metadata) {
        String methodName = methodDeclaration.getNameAsString();
    }
}
