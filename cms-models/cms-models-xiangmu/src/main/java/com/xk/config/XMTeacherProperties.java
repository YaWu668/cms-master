package com.xk.config;

import com.cms.common.core.exception.ServiceException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
/**
 * 老师热更新配置
 */

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
     * 热配置初始化<br>
     * 执行时机: 项目启动时 和 Nacos 配置变更时<br>
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
