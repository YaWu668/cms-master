package com.cms.system.api.factory;

import com.cms.common.core.web.domain.Response;
import com.cms.system.api.RemoteLogService;
import com.cms.system.api.domain.pojo.SysLogininfor;
import com.cms.system.api.domain.pojo.SysOperLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * 日志服务降级处理
 *
 * @author 邓志军
 * @date 2024年5月28日23:32:33
 */
@Component
public class RemoteLogFallbackFactory implements FallbackFactory<RemoteLogService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteLogFallbackFactory.class);

    @Override
    public RemoteLogService create(Throwable throwable) {
        log.error("日志服务调用失败:{}", throwable.getMessage());
        return new RemoteLogService() {
            @Override
            public Response<Boolean> saveLog(SysOperLog sysOperLog, String source) {
                return Response.error("保存操作日志失败:" + throwable.getMessage());
            }

            @Override
            public Response<Boolean> saveLogininfor(SysLogininfor sysLogininfor, String source) {
                return Response.error("保存登录日志失败:" + throwable.getMessage());
            }
        };

    }
}
