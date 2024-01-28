package com.example.event;

import java.lang.reflect.Method;

public class MethodCache {
    private final Method method;
    private final String beanName;
    @SuppressWarnings("rawtypes")
    private final Class[] parameterTypes;

    public MethodCache(String beanName, Method method)
    {
        this.beanName=beanName;
        this.method = method;
        this.parameterTypes = method.getParameterTypes();
    }

    public Method getMethod()
    {
        return this.method;
    }

    @SuppressWarnings("rawtypes")
    public Class[] getParameterTypes()
    {
        return this.parameterTypes;
    }

    public String getBeanName() {
        return beanName;
    }
}
