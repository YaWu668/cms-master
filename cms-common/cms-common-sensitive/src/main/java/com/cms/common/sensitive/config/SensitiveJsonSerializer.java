package com.cms.common.sensitive.config;

import com.cms.common.core.constant.UserConstants;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.common.sensitive.annotation.Sensitive;
import com.cms.common.sensitive.enmus.DesensitizedType;
import com.cms.system.api.model.LoginUser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 自定义 JSON 序列化器，用于处理敏感数据的序列化。
 * 该类实现了 Jackson 的 JsonSerializer<String> 和 ContextualSerializer 接口，
 * 允许自定义序列化逻辑以及支持上下文中的动态配置。
 */
public class SensitiveJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizedType desensitizedType;

    /**
     * 序列化方法，用于将 String 类型的数据序列化为 JSON 格式。
     * 该方法可以自定义序列化逻辑，以便处理敏感数据。
     *
     * @param s                  需要序列化的 String 数据
     * @param jsonGenerator      用于生成 JSON 数据的生成器
     * @param serializerProvider 提供序列化器的上下文
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // 检查是否需要进行脱敏处理
        if (desensitization()) {
            // 使用配置的脱敏方法对数据进行处理，并将处理后的数据写入 JSON 输出
            jsonGenerator.writeString(desensitizedType.desensitizer().apply(s));
        } else {
            // 直接将原始数据写入 JSON 输出
            jsonGenerator.writeString(s);
        }
    }

    /**
     * 创建一个上下文相关的序列化器，该方法允许根据序列化上下文动态配置序列化器。
     * 在这个方法中，可以根据当前的属性配置和序列化上下文创建适当的序列化器。
     *
     * @param serializerProvider 提供序列化器的上下文
     * @param beanProperty       当前序列化的 Bean 属性
     * @return 根据上下文配置的 JsonSerializer 实例
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 获取 Bean 属性上的 @Sensitive 注解
        Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
        // 如果注解存在且属性类型为 String，则根据注解中的脱敏类型配置当前序列化器
        if (Objects.nonNull(annotation) && Objects.equals(String.class, beanProperty.getType().getRawClass())) {
            this.desensitizedType = annotation.desensitizedType();
            return this;
        }
        // 否则，返回根据属性类型找到的默认序列化器
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }

    /**
     * 是否需要脱敏处理
     */
    private boolean desensitization() {
        try {
            LoginUser securityUser = SecurityUtils.getLoginUser();
            // 管理员不脱敏
            return !securityUser.getSysUser().getUserId().equals(UserConstants.ADMIN_ID);
        } catch (Exception e) {
            return true;
        }
    }
}
