package com.cms.common.swagger.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * swagger 在 springboot 2.6.x 不兼容问题的处理
 *
 * @author 邓志军
 * @date 2024年5月28日23:55:14
 */
public class SwaggerBeanPostProcessor implements BeanPostProcessor {
    /**
     * 初始化后对bean进行处理
     *
     * @param bean     要处理的bean
     * @param beanName bean的名称
     * @return 处理后的bean
     * @throws BeansException 异常信息
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 检查bean是否为 WebMvcRequestHandlerProvider 或 WebFluxRequestHandlerProvider 的实例
        if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
            // 定制Springfox的请求处理映射
            customizeSpringfoxHandlerMappings(Objects.requireNonNull(getHandlerMappings(bean)));
        }
        return bean;
    }

    /**
     * 定制 Springfox 的请求处理映射
     *
     * @param mappings 请求处理映射列表
     * @param <T>      RequestMappingInfoHandlerMapping 类型
     */
    private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
        // 使用流过滤掉 PatternParser 不为 null 的映射，并将结果收集到新的列表中
        List<T> copy = mappings.stream().filter(mapping -> mapping.getPatternParser() == null)
                .collect(Collectors.toList());
        // 清空原映射列表并将定制后的映射列表添加进去
        mappings.clear();
        mappings.addAll(copy);
    }

    /**
     * 获取请求处理映射列表
     *
     * @param bean Springfox请求处理映射提供者
     * @return 请求处理映射列表
     */
    @SuppressWarnings("unchecked")
    private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
        try {
            // 通过反射获取 handlerMappings 字段
            Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
            if (field != null) {
                field.setAccessible(true);
                // 返回 handlerMappings 字段的值
                return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
            }
            // 如果 handlerMappings 字段为 null，则返回空
            return null;
        } catch (IllegalArgumentException | IllegalAccessException e) {
            // 抛出异常信息，表示无法获取 handlerMappings 字段
            throw new IllegalStateException(e);
        }
    }

}
