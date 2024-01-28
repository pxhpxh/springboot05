package com.example.base;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class AppContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (AppContext.applicationContext == null) {
            AppContext.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) throws Exception {
        Map<String, T> beans=getApplicationContext().getBeansOfType(type);
        return beans;
    }

    public static Object getBean(String name) throws Exception {
        Object obj=getApplicationContext().getBean(name);
        if(obj!=null){
            return getTarget(obj);
        } else {
            return null;
        }
    }

    public static Map<String, Object> getMapbeanwithAnnotion(Class<? extends Annotation> cls) {
        Map<String, Object> map = new HashMap<String, Object>();
        map = applicationContext.getBeansWithAnnotation(cls);
        return map;
    }

    public static Object getTarget(Object proxy) throws Exception {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;// 不是代理对象
        }
        // 根据jdk进行反射操作
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { // 根据cglib进行反射操作
            return getCglibProxyTargetObject(proxy);
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }
    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

    private static final Set<String> EXCLUDE_METHODS = new HashSet<>();

    static
    {
        EXCLUDE_METHODS.add("addAdvice");
        EXCLUDE_METHODS.add("addAdvisor");
        EXCLUDE_METHODS.add("equals");
        EXCLUDE_METHODS.add("getAdvisors");
        EXCLUDE_METHODS.add("getProxiedInterfaces");
        EXCLUDE_METHODS.add("getTargetSource");
        EXCLUDE_METHODS.add("getTargetClass");
        EXCLUDE_METHODS.add("hashCode");
        EXCLUDE_METHODS.add("indexOf");
        EXCLUDE_METHODS.add("isExposeProxy");
        EXCLUDE_METHODS.add("isFrozen");
        EXCLUDE_METHODS.add("isInterfaceProxied");
        EXCLUDE_METHODS.add("isProxyTargetClass");
        EXCLUDE_METHODS.add("isPreFiltered");
        EXCLUDE_METHODS.add("removeAdvice");
        EXCLUDE_METHODS.add("removeAdvisor");
        EXCLUDE_METHODS.add("replaceAdvisor");

        EXCLUDE_METHODS.add("setPreFiltered");
        EXCLUDE_METHODS.add("setExposeProxy");
        EXCLUDE_METHODS.add("setTargetSource");
        EXCLUDE_METHODS.add("toProxyConfigString");
        EXCLUDE_METHODS.add("toString");
        EXCLUDE_METHODS.add("delete");
        EXCLUDE_METHODS.add("in");
        EXCLUDE_METHODS.add("let");
        EXCLUDE_METHODS.add("function");
        EXCLUDE_METHODS.add("prototype");
        EXCLUDE_METHODS.add("console");
        EXCLUDE_METHODS.add("debugger");
        EXCLUDE_METHODS.add("typeof");
    }

    public static boolean isDefaultMethods(String methodsName){
        return EXCLUDE_METHODS.contains(methodsName);
    }
}
