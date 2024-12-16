package com.xk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "xm.teacher")
public class XMTeacherProperties {
    /**
     * 老师最大报名人数
     */
    private Long maxApply;
}
