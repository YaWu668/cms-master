package com.cms.common.sensitive.annotation;

import com.cms.common.sensitive.config.SensitiveJsonSerializer;
import com.cms.common.sensitive.enmus.DesensitizedType;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据脱敏注解
 *
 * @author 邓志军
 * @date 2024年9月5日14:52:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveJsonSerializer.class)
public @interface Sensitive {

    DesensitizedType desensitizedType();
}
