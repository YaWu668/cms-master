package com.xk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "xm.state")
public class XMStateProperties {
    /**
     * 项目状态列表,从小到大排序开始
     */
    private List<Long> stateList;
}
