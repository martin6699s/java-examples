package com.example.jaspersoft.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 *
 * 不用注入，联系上下文 获取spring容器中的bean
 */
@Component
public class SpringTool implements ApplicationContextAware {
    private static ApplicationContext applicationContext = null;
    @Override public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        // 静态变量的操作最好在静态方法里面实现
        setApplicationContextValue(applicationContext);
    }

    private static void setApplicationContextValue(ApplicationContext applicationContext) {
        // 避免不同的请求获取到相同ID的上下文
        if (SpringTool.applicationContext == null ||
            !Objects.equals(SpringTool.applicationContext, applicationContext)) {
            SpringTool.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(Class<T> clazz, Object... args) {
        return getApplicationContext().getBean(clazz, args);
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static Object getBean(String name, Object... args) {
        return getApplicationContext().getBean(name, args);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}

