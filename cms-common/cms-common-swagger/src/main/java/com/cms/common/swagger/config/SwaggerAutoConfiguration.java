package com.cms.common.swagger.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// 引入第三方配置，开启接口参数校验处理，需要导入spring-boot-starter-validation依赖
@Configuration
@EnableSwagger2
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
@Import({SwaggerBeanPostProcessor.class, SwaggerWebConfiguration.class})
public class SwaggerAutoConfiguration {
    /**
     * 默认的排除路径，排除Spring Boot默认的错误处理路径和端点
     */
    private static final List<String> DEFAULT_EXCLUDE_PATH = Arrays.asList("/error", "/actuator/**");

    private static final String BASE_PATH = "/**";

    /**
     * 根据 SwaggerProperties 配置创建并返回一个 Docket 对象，用于配置 Swagger 文档生成。
     *
     * @param swaggerProperties 包含了 Swagger 相关配置信息的对象
     * @return 配置好的 Docket 对象
     */
    @Bean
    public Docket api(SwaggerProperties swaggerProperties) {
        // 处理 base-path
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }

        // 处理 exclude-path
        if (swaggerProperties.getExcludePath().isEmpty()) {
            swaggerProperties.getExcludePath().addAll(DEFAULT_EXCLUDE_PATH);
        }

        // 创建 Docket 对象
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties))
                .select()
                .paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()));

        // 设置 base-path 和 exclude-path
        swaggerProperties.getBasePath().forEach(p -> builder.paths(PathSelectors.ant(p)));
        swaggerProperties.getExcludePath().forEach(p -> builder.paths(PathSelectors.ant(p).negate()));

        // 返回配置好的 Docket 对象
        return builder.build()
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping("/");
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", "header"));
        return apiKeyList;
    }

    /**
     * 创建并返回一个安全上下文列表，用于配置 Swagger 文档的安全要求。
     *
     * @return 配置好的安全上下文列表
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();

        // 创建一个 SecurityContext 对象，并添加到列表中
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())  // 设置安全引用
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))  // 匹配所有请求操作
                        .build());
        return securityContexts;
    }

    /**
     * 创建并返回默认的全局鉴权策略，用于配置 Swagger 文档的默认安全引用。
     *
     * @return 默认的全局鉴权策略列表
     */
    private List<SecurityReference> defaultAuth() {
        // 创建一个 AuthorizationScope 对象，表示授权范围为 "global"，权限为 "accessEverything"
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");

        // 创建一个 AuthorizationScope 数组，包含上述创建的 AuthorizationScope 对象
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        // 创建一个 SecurityReference 对象，并添加到列表中
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));

        return securityReferences;
    }

    /**
     * 根据 SwaggerProperties 配置创建并返回一个 ApiInfo 对象，用于配置 Swagger 文档的基本信息。
     *
     * @param swaggerProperties 包含了 Swagger 基本信息配置的对象
     * @return 配置好的 ApiInfo 对象
     */
    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                // 设置标题
                .title(swaggerProperties.getTitle())
                // 设置描述
                .description(swaggerProperties.getDescription())
                // 设置许可证信息
                .license(swaggerProperties.getLicense())
                // 设置许可证链接
                .licenseUrl(swaggerProperties.getLicenseUrl())
                // 设置服务条款链接
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                // 设置联系信息
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                // 设置版本号
                .version(swaggerProperties.getVersion())
                .build();
    }

}
