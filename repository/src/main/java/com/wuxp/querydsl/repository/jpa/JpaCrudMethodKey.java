package com.wuxp.querydsl.repository.jpa;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author wuxp
 */
final class JpaCrudMethodKey {

    private String methodName;

    private Class<?>[] parameters;

    JpaCrudMethodKey(String methodName, Class<?>[] parameters) {
        this.methodName = methodName;
        this.parameters = parameters;
    }

    static JpaCrudMethodKey of(String methodName, Class<?>[] parameters) {
        return new JpaCrudMethodKey(methodName, parameters);
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameters() {
        return parameters;
    }

    public void setParameters(Class<?>[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JpaCrudMethodKey that = (JpaCrudMethodKey) o;
        return Objects.equals(methodName, that.methodName) &&
                Arrays.equals(parameters, that.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(methodName);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }
}
