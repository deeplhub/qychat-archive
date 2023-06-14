package com.xh.qychat.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * Title:
 * Description:
 *
 * @author H.Yang
 * @date 2020/12/25
 */
@Slf4j
@Configuration
public class SpringBeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     *
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringBeanUtils.applicationContext = applicationContext;
    }

    //取得存储在静态变量中的ApplicationContext.
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    //从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
    public static <T> T getBean(String beanName) {
        checkApplicationContext();
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBeanThrow(String beanName) {
        T bean = getBean(beanName);
        if (bean == null) {
            throw new RuntimeException("Can not get bean name：" + beanName);
        }
        return bean;
    }


    //从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
    //如果有多个Bean符合Class, 取出第一个.
    public static <T> T getBean(Class<T> clazz) {
        checkApplicationContext();
        Map beanMaps = applicationContext.getBeansOfType(clazz);
        if (beanMaps != null && !beanMaps.isEmpty()) {
            return (T) beanMaps.values().iterator().next();
        } else {
            return null;
        }
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("ApplicaitonContext 未注入,请在 ApplicationContext.xml 中定义 SpringContextHolder ");
        }
    }

}
