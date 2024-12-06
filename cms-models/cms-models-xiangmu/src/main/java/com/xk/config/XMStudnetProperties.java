package com.xk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xm.studnet")
public class XMStudnetProperties {
    /**
     * 项目类的报名人数 key: 项目类型 vluae: 项目报名人数
     */
    private Map<Long, Long> maxApplys;
    /**
     *  申请文件是否需要企业老师的支持 key(字典类型的): vlaue(0不需要企业老师,1需要企业)
     */
    private Map<Long, Long> teacherBusinessGuidance;
}
