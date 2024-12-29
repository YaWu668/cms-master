package com.xk.client;

import com.cms.common.core.web.domain.Response;


import com.xk.domain.vo.api.UserInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cms-system", path = "/user", contextId = "sysUserClient")
public interface SysUserClient {
    /**
     * 根据用户ID，获取用户信息
     *
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/userInfo/{id}")
    Response<UserInfoVo> getEntityById(@PathVariable("id") Long id);

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/getInfo")
    Response<UserInfoVo> getUserInfo();
}
