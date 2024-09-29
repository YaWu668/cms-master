package com.cms.common.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限认证：必须具有指定权限才能进入该方法
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequiresPermissions {
    /**
     * 需要校验的权限码
     */
    String[] value() default {};

    /**
     * 验证模式：AND | OR，默认AND
     *
     * and 表示必须持有 value 上的所有权限，
     * or 表示有一个就行。
     */
    Logical logical() default Logical.AND;
}
