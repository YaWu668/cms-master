package com.cms.common.log.service;

import com.cms.common.core.constant.SecurityConstants;
import com.cms.system.api.RemoteLogService;
import com.cms.system.api.domain.pojo.SysOperLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 异步调用日志服务
 *
 * @author 邓志军
 * @date 2024年5月28日23:45:31
 */
@Service
public class AsyncLogService {
    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 保存系统日志记录
     *
     * @param sysOperLog 系统操作日志信息
     */
    @Async
    public void saveSysLog(SysOperLog sysOperLog) throws Exception {
        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
    }
}
