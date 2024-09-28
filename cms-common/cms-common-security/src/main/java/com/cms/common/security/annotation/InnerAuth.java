package com.cms.common.security.annotation;

import java.lang.annotation.*;

/**
 * 内部认证注解
 *
 * @author 邓志军
 * @date 2024年5月28日22:29:06
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InnerAuth {
    /**
     * 是否校验用户信息
     */
    boolean isUser() default false;
}