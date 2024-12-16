package com.xk.config;

import com.cms.common.core.exception.ServiceException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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


    /**
     * 校验配置项是否有效
     */
    @PostConstruct
    public void validateConfig() {
        //非null校验
        if (maxApply == null) {
            throw new ServiceException("配置项:xm.teacher.maxApply 不能为空，请检查 Nacos 配置，请联系管理员",500);
        }

        //合法性校验
        if (maxApply <= 0) {
            throw new ServiceException("配置项:xm.teacher.maxApply 必须大于0，请检查 Nacos 配置，请联系管理员",500);
        }
    }
}
