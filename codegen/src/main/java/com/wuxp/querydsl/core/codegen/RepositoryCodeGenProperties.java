package com.wuxp.querydsl.core.codegen;

/**
 * @author wuxp
 */
public final class RepositoryCodeGenProperties {

    /**
     * 用于生成repository的类生成器类
     */
    private String repositoryCodeGenClassName;

    public String getRepositoryCodeGenClassName() {
        return repositoryCodeGenClassName;
    }

    public void setRepositoryCodeGenClassName(String repositoryCodeGenClassName) {
        this.repositoryCodeGenClassName = repositoryCodeGenClassName;
    }
}
