package com.cms.common.swagger.config;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Swagger 资源映射路径配置类
 * 用于配置 Swagger UI 的资源映射路径，使其可以访问到相应的静态资源
 *
 * @author 邓志军
 * @date 2024年5月28日23:54:57
 */
public class SwaggerWebConfiguration implements WebMvcConfigurer {

    /**
     * 添加Swagger UI资源处理器
     *
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}
