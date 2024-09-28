package com.cms.common.security.annotation;

/**
 * 权限注解的验证模式
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 *
 */
public enum Logical
{
    /**
     * 必须具有所有的元素
     */
    AND,

    /**
     * 只需具有其中一个元素
     */
    OR
}
