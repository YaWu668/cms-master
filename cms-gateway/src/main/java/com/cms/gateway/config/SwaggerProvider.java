package com.cms.gateway.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * 聚合系统接口
 * 该类用于聚合系统中各个微服务的接口信息，以便在统一的 Swagger UI 中查看和测试。
 *
 * @author 邓志军
 * @date 2024年5月29日00:11:11
 */
@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider, WebFluxConfigurer {
    /**
     * Swagger2默认的url后缀
     */
    public static final String SWAGGER2URL = "/v2/api-docs";

    /**
     * 网关路由
     */
    @Lazy
    @Autowired
    private RouteLocator routeLocator;

    @Autowired
    private GatewayProperties gatewayProperties;

    /**
     * 聚合其他服务接口
     * 获取并聚合其他服务的接口信息，返回SwaggerResource列表。
     *
     * @return 包含其他服务接口信息的SwaggerResource列表
     */
    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resourceList = new ArrayList<>();
        List<String> routes = new ArrayList<>();

        // 获取网关中配置的route
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));

        // 遍历网关配置中的routes
        gatewayProperties.getRoutes().stream()
                .filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> {

                    // 获取元数据
                    Map<String, Object> metadata = routeDefinition.getMetadata();

                    // 对于每个路由，获取其Predicates并筛选出Path类型的Predicate
                    routeDefinition.getPredicates().stream()
                            .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                            .filter(predicateDefinition -> !"cms-auth".equalsIgnoreCase(routeDefinition.getId()))
                            .forEach(predicateDefinition -> {
                                // 构建SwaggerResource对象并加入到resourceList中
                                resourceList.add(swaggerResource(
                                        // routeDefinition.getId(),
                                        metadata.get("swaggerName").toString(),
                                        predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                                .replace("/**", SWAGGER2URL)));
                            });
                });

        return resourceList;
    }

    /**
     * 构建SwaggerResource对象
     * 根据服务名和url构建SwaggerResource对象。
     *
     * @param name     服务名
     * @param location url地址
     * @return 构建好的SwaggerResource对象
     */
    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

    /**
     * 添加Swagger UI资源处理器
     * 用于添加Swagger UI的资源处理器，指定swagger-ui的访问地址和静态资源路径。
     *
     * @param registry ResourceHandlerRegistry对象
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** swagger-ui 地址 */
        registry.addResourceHandler("/swagger-ui/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }
}
