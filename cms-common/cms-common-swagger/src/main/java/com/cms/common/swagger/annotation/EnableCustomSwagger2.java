package com.cms.common.swagger.annotation;

import com.cms.common.swagger.config.SwaggerAutoConfiguration;
import org.springframework.context.annotation.Import;
import java.lang.annotation.*;

/**
 * 自定义注解，用于启用自定义的Swagger2配置。
 *
 * @author 邓志军
 * @date 2024年5月28日23:52:29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({SwaggerAutoConfiguration.class})
public @interface EnableCustomSwagger2 {
}
