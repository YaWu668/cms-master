package com.cms.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 登录认证：只有登录之后才能进入该方法
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresLogin {
}
