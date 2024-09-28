package com.cms.system.api.factory;

import com.cms.common.core.web.domain.Response;
import com.cms.system.api.RemoteUserService;
import com.cms.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 *
 * @author 邓志军
 * @date 2024年5月28日21:10:01
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return (username, source) -> Response.error("获取用户失败:" + throwable.getMessage());
    }
}
