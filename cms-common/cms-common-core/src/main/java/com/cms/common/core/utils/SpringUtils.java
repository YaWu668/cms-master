package com.cms.common.core.utils;

import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**
 * spring工具类 方便在非spring管理环境中获取bean
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
@Component
public final class SpringUtils implements BeanFactoryPostProcessor {

    /**
     * Spring应用上下文环境
     */
    private static ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        SpringUtils.beanFactory = beanFactory;
    }

    /**
     * 根据名称获取注册的 bean 实例
     *
     * @param name bean 的名称
     * @return 指定名称注册的 bean 实例
     * @throws BeansException 如果未找到指定名称的 bean 或发生其他 bean 异常时抛出
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    /**
     * 根据类型获取注册的 bean 实例
     *
     * @param clz 要获取的 bean 的类型
     * @return 指定类型的 bean 实例
     * @throws BeansException 如果未找到指定类型的 bean 或发生其他 bean 异常时抛出
     */
    public static <T> T getBean(Class<T> clz) throws BeansException {
        return (T) beanFactory.getBean(clz);
    }

    /**
     * 检查是否存在指定名称的 bean 定义
     *
     * @param name 要检查的 bean 名称
     * @return 如果 BeanFactory 包含一个与所给名称匹配的 bean 定义，则返回 true；否则返回 false
     */
    public static boolean containsBean(String name) {
        return beanFactory.containsBean(name);
    }

    /**
     * 判断以给定名称注册的 bean 定义是一个 singleton 还是一个 prototype。
     *
     * @param name 要检查的 bean 名称
     * @return 如果与给定名称相应的 bean 定义是一个 singleton，则返回 true；否则返回 false
     * @throws NoSuchBeanDefinitionException 如果未找到与给定名称相应的 bean 定义时抛出
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.isSingleton(name);
    }

    /**
     * 获取以指定名称注册的 bean 的类型。
     *
     * @param name 要获取类型的 bean 名称
     * @return Class 对象，表示注册对象的类型
     * @throws NoSuchBeanDefinitionException 如果未找到与指定名称相应的 bean 定义时抛出
     */
    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getType(name);
    }

    /**
     * 获取给定 bean 名称在 bean 定义中的所有别名。
     *
     * @param name 要获取别名的 bean 名称
     * @return 字符串数组，包含与给定名称相关联的所有别名
     * @throws NoSuchBeanDefinitionException 如果未找到与给定名称相应的 bean 定义时抛出
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return beanFactory.getAliases(name);
    }

    /**
     * 获取给定对象的 AOP 代理对象。
     *
     * @param invoker 要获取 AOP 代理对象的对象
     * @return 给定对象的 AOP 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAopProxy(T invoker) {
        return (T) AopContext.currentProxy();
    }

    /**
     * 将原始对象的属性复制到目标对象中。
     *
     * @param original    原始对象
     * @param targetClass 目标对象的类型
     * @return 复制完成属性后的目标对象
     */
    public static <T> T copyProperties(Object original, Class<T> targetClass) {
        if (StringUtils.isNull(original)) {
            return null;
        }
        T target;
        try {
            target = targetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace(); // 可以根据实际情况处理异常
            return null;
        }
        BeanUtils.copyProperties(original, target);
        return target;
    }

    /**
     * 将原始对象的属性复制到目标对象中。
     *
     * @param original 原始对象
     * @param target   目标对象的类型
     * @return 复制完成属性后的目标对象
     */
    public static <T> T copyProperties(Object original, T target) {
        if (StringUtils.isNull(original)) {
            return null;
        }
        BeanUtils.copyProperties(original, target);
        return target;
    }

}
