package com.wuxp.querydsl.core.codegen;

/**
 * @author wuxp
 */
public final class RepositoryCodeGenProperties {

    /**
     * 超类名称
     */
    private String repositorySupperClassName;

    /**
     * 用于生成repository的类生成器类
     */
    private String repositoryCodeGenClassName;

    public String getRepositorySupperClassName() {
        return repositorySupperClassName;
    }

    public void setRepositorySupperClassName(String repositorySupperClassName) {
        this.repositorySupperClassName = repositorySupperClassName;
    }

    public String getRepositoryCodeGenClassName() {
        return repositoryCodeGenClassName;
    }

    public void setRepositoryCodeGenClassName(String repositoryCodeGenClassName) {
        this.repositoryCodeGenClassName = repositoryCodeGenClassName;
    }
}
