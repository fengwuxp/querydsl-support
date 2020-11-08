package com.wuxp.querydsl.core.codegen.methods;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.wuxp.querydsl.core.codegen.enums.SupportReturnType;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodCodeGenContext;
import com.wuxp.querydsl.core.codegen.model.RepositoryMethodMetadata;
import com.wuxp.querydsl.core.codegen.util.JavaMethodNameUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.wuxp.querydsl.core.codegen.util.JavaMethodNameUtils.LINE_CHAR;

/**
 * 按照命名策略进行方法实现的生成
 * 参考spring data jpa 文档 https://docs.spring.io/spring-data/jpa/docs/2.0.9.RELEASE/reference/html/#jpa.query-methods.query-creation
 * <pre>
 *     额外增强：
 *     1：支持在select关键字后面 By关键字之前声明需要查询的列，建议不要超过2个（太多将导致方法名过长，可读性太差），多列的查询，请使用注解声明
 *      selectXxxByXx ==> selectNameById  selectNameAgeById  返回值可以是 Number\Boolean\String\List\Set\Map 或者是对象
 *     example:
 *     String selectNameById(Long id)
 *     Map<String,Object> selectNameAgeById(Long id)
 *     List<Map<String,Object>> selectNameSexByAagLessThan(int age)
 *     List<Object[]> selectNameSexByAagLessThan(int age)
 *     List<{name:string,sex:Sex}> selectNameSexByAagLessThan(int age)
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
public class NamingStrategyRepositoryMethodAnalyzer  extends AbstractRepositoryMethodAnalyzer{

    /**
     * 用于查询整个对象的关键字
     */
    private static final List<String> FIND_ENTITY_KEYWORDS = Arrays.asList("find", "get", "query", "read");

    /**
     * 用于查询某些列的关键字
     */
    private static final List<String> SELECT_COLUMN_KEYWORDS = Collections.singletonList("select");

    /**
     * 用于统计的关键字
     */
    private static final List<String> COUNT_COLUMN_KEYWORDS = Collections.singletonList("count");

    public NamingStrategyRepositoryMethodAnalyzer(RepositoryMethodCodeGenContext repositoryMethodCodeGenContext) {
        super(repositoryMethodCodeGenContext);
    }

    @Override
    public void analysis(MethodDeclaration methodDeclaration, RepositoryMethodMetadata metadata) {
        String methodName = methodDeclaration.getNameAsString();
        String[] names = JavaMethodNameUtils.humpToLine(methodName).split(LINE_CHAR);
        boolean isNamedMethods = isFindMethod(names) || isSelectMethod(names) || isCountMethod(names);
        if (!isNamedMethods) {
            return;
        }
        Type type = methodDeclaration.getType();
        if (type instanceof PrimitiveType) {
            // 原始类型
            handSimpleType(methodDeclaration, metadata);
            return;
        }
        if (type instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType returnType = (ClassOrInterfaceType) type;
            String simpleName = returnType.getName().asString();
            if (SupportReturnType.isSimpleType(simpleName)) {
                handSimpleType(methodDeclaration, metadata);
            }
//            SupportReturnType supportReturnType = SupportReturnType.valueOf(simpleName);
//            NodeList<Type> typeArguments = returnType.getTypeArguments().orElse(NodeList.nodeList());
//            int size = typeArguments.size();
//            if (size > 1 || SupportReturnType.Map.equals(supportReturnType)) {
//
//            }
        }
    }

    @Override
    public boolean supports(MethodDeclaration methodDeclaration) {
        boolean parameterHasOp = methodDeclaration.getParameters().stream().map(parameter -> parameter.getAnnotations().size() > 0).findFirst().orElse(false);
        if (parameterHasOp) {
            // 方法参数上存在注解，不支持
            return false;
        }
        return true;
    }


    /**
     * 处理返回值为简单类型的方法定义
     *
     * @param methodDeclaration 方法定义
     * @param metadata          方法的元数据信息
     */
    private void handSimpleType(MethodDeclaration methodDeclaration, RepositoryMethodMetadata metadata) {


    }


    /**
     * @param methodNames 方法名称按照驼峰切割后的数组
     * @return 是否为 findXxx相关方法
     */
    private boolean isFindMethod(String[] methodNames) {
        return FIND_ENTITY_KEYWORDS.contains(methodNames[0]);
    }

    /**
     * @param methodNames 方法名称按照驼峰切割后的数组
     * @return 是否为 SelectXxx相关方法
     */
    private boolean isSelectMethod(String[] methodNames) {
        return SELECT_COLUMN_KEYWORDS.contains(methodNames[0]);
    }

    /**
     * @param methodNames 方法名称按照驼峰切割后的数组
     * @return 是否为 CountXxx相关方法
     */
    private boolean isCountMethod(String[] methodNames) {
        return COUNT_COLUMN_KEYWORDS.contains(methodNames[0]);
    }

}
