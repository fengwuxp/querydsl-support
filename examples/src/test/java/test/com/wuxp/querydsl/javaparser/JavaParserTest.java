package test.com.wuxp.querydsl.javaparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.wuxp.querydsl.repositories.GoodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
public class JavaParserTest {


    @Test
    public void testParseAndCodeGen() throws Exception {
        JavaParser javaParser = new JavaParser();
        String pathname = getJavaFilePath(GoodsRepository.class.getName());
        ParseResult<CompilationUnit> result = javaParser.parse(new File(pathname));
        Assert.assertTrue(result.getResult().isPresent());
        CompilationUnit repositoryInterface = result.getResult().get();
        Optional<ClassOrInterfaceDeclaration> goodsRepository = repositoryInterface.getInterfaceByName("GoodsRepository");
        Assert.assertTrue(goodsRepository.isPresent());

        CompilationUnit implClass = new CompilationUnit();
        implClass.setPackageDeclaration("com.wuxp.querydsl.repositories.impl");
        ClassOrInterfaceDeclaration goodsRepositoryImpl = implClass.addClass("GoodsRepositoryImpl", Modifier.Keyword.PUBLIC);
        repositoryInterface.getImports().forEach(importDeclaration -> {
            implClass.addImport(importDeclaration.getName().asString());
        });
        goodsRepositoryImpl.addImplementedType("test.com.wuxp.querydsl.repositories.GoodsRepository");
        goodsRepositoryImpl.addAnnotation(Component.class);
        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = goodsRepository.get();
        classOrInterfaceDeclaration.getMethods()
                .stream()
                .filter(methodDeclaration -> !methodDeclaration.isDefault())
                .forEach(methodDeclaration -> {
                    NodeList<Modifier> modifiers = goodsRepositoryImpl.getModifiers();
                    MethodDeclaration methodDeclarationImpl = goodsRepositoryImpl.addMethod(methodDeclaration.getName().toString(), modifiers.stream().map(Modifier::getKeyword).toArray(Modifier.Keyword[]::new));
                    methodDeclarationImpl.addAndGetAnnotation(Override.class);
                    methodDeclarationImpl.setType(methodDeclaration.getType());
                    methodDeclarationImpl.setParameters(methodDeclaration.getParameters());

                    BlockStmt body = new BlockStmt();
                    body.addStatement("return null;");

//                    ExpressionStmt expressionStmt = new ExpressionStmt();
//                    VariableDeclarationExpr variableDeclarationExpr = new VariableDeclarationExpr();
//                    VariableDeclarator variableDeclarator = new VariableDeclarator();
//                    variableDeclarator.setName("anyVariableName");
//                    variableDeclarator.setType(new ClassOrInterfaceType("AnyVariableType"));
//                    variableDeclarator.setInitializer("new AnyVariableType()");
//                    NodeList<VariableDeclarator> variableDeclarators = new NodeList<>();
//                    variableDeclarators.add(variableDeclarator);
//                    variableDeclarationExpr.setVariables(variableDeclarators);
//                    expressionStmt.setExpression(variableDeclarationExpr);
//
//                    ReturnStmt returnStmt = new ReturnStmt();
//                    NameExpr returnNameExpr = new NameExpr();
//                    returnNameExpr.setName("anyVariableName");
//                    returnStmt.setExpression(returnNameExpr);
//                    body.addStatement(returnStmt);
//                    body.addStatement(expressionStmt);
                    methodDeclarationImpl.setBody(body);
                });

        log.info("{}", implClass);
        log.info("{}", getJavaFilePath(GoodsRepository.class.getName()));
        log.info("{}", System.getProperty("user.dir"));
    }

    private String getModuleDir() {
        String[] outPaths = {"src", "main", "java"};
        return Paths.get(System.getProperty("user.dir")).resolve(String.join(File.separator, outPaths)).toString();
    }

    private String getJavaFilePath(String className) {
        return String.format("%s%s%s.java", getModuleDir(),File.separator, className.replaceAll("\\.", File.separator));
    }

}
