package com.cms.system.api;

import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.constant.ServiceNameConstants;
import com.cms.common.core.web.domain.Response;
import com.cms.system.api.factory.RemoteUserFallbackFactory;
import com.cms.system.api.model.LoginUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * FeignClients 用户服务客户端
 *
 * @author 邓志军
 * @date 2024年5月28日18:01:53
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @param source   请求来源
     * @return 请求结果
     */
    @GetMapping("/user/info/{username}")
    Response<LoginUser> getUserInfoByUserName(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
