package com.wuxp.querydsl.core.codegen.repository;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.wuxp.querydsl.core.codegen.CodeGenerator;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Optional;

/**
 * @author wuxp
 */
public class JapRepositoryGenerator implements CodeGenerator {


    private final Map<String, FileObject> fileObjects;

    protected final ProcessingEnvironment processingEnv;


    public JapRepositoryGenerator(Map<String, FileObject> fileObjects, ProcessingEnvironment processingEnv) {
        this.fileObjects = fileObjects;
        this.processingEnv = processingEnv;
    }

    @Override
    public void generate() {
        Filer filer = this.processingEnv.getFiler();
        fileObjects.forEach((key, fileObject) -> {
            JpaRepositorySourceGenerator sourceGenerator = new JpaRepositorySourceGenerator(key, fileObject, filer);
            CompilationUnit compilationUnit = sourceGenerator.generate();
            if (compilationUnit == null) {
                return;
            }
            Optional<PackageDeclaration> packageDeclaration = compilationUnit.getPackageDeclaration();
            if (!packageDeclaration.isPresent()) {
                return;
            }
            String packageName = sourceGenerator.getPackageName();
            try {
                JavaFileObject sourceFile = filer.createSourceFile(String.format("%s.%s", packageName, sourceGenerator.getNewSimpleClassName()));
                Writer writer = sourceFile.openWriter();
                writer.write(compilationUnit.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
