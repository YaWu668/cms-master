package com.cms.system.api;

import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.constant.ServiceNameConstants;
import com.cms.common.core.web.domain.Response;
import com.cms.system.api.domain.pojo.SysLogininfor;
import com.cms.system.api.domain.pojo.SysOperLog;
import com.cms.system.api.factory.RemoteLogFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * 日志服务
 *
 * @author 邓志军
 * @date 2024年5月28日23:32:51
 */
@FeignClient(contextId = "remoteLogService", value = ServiceNameConstants.SYSTEM_SERVICE, fallbackFactory = RemoteLogFallbackFactory.class)
public interface RemoteLogService {
    /**
     * 保存系统日志
     *
     * @param sysOperLog 日志实体
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping("/operlog")
    Response<Boolean> saveLog(@RequestBody SysOperLog sysOperLog, @RequestHeader(SecurityConstants.FROM_SOURCE) String source) throws Exception;

    /**
     * 保存访问记录
     *
     * @param sysLogininfor 访问实体
     * @param source        请求来源
     * @return 结果
     */
    @PostMapping("/logininfor")
    Response<Boolean> saveLogininfor(@RequestBody SysLogininfor sysLogininfor, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
