package com.cms.auth.service;

import com.cms.common.core.constant.Constants;
import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.utils.ip.IpUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.system.api.RemoteLogService;
import com.cms.system.api.domain.pojo.SysLogininfor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 日志记录服务
 * SysRecordLogService 类用于记录用户登录信息，包括用户名、状态和消息内容，并调用远程日志服务保存登录信息。
 * 注意：该类需要结合 RemoteLogService 类来实现远程日志保存功能。
 *
 * @author 邓志军
 * @date 2024年5月28日22:45:43
 */
@Component
public class SysRecordLogService {

    @Autowired
    private RemoteLogService remoteLogService;

    /**
     * 记录登录信息
     * recordLogininfor 方法用于记录用户登录信息，并调用远程日志服务保存登录信息。
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    public void recordLogininfor(String username, String status, String message,Integer type) {
        // 创建 SysLogininfor 对象并设置相关属性
        SysLogininfor logininfor = new SysLogininfor();
        logininfor.setUserName(username);
        logininfor.setIpaddr(IpUtils.getIpAddr());
        logininfor.setMsg(message);
        logininfor.setType(type);

        // 判断并设置日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            // 登录成功、登出、注册操作设置为成功状态
            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            // 登录失败设置为失败状态
            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
        }

        // 调用远程日志服务保存登录信息
        remoteLogService.saveLogininfor(logininfor, SecurityConstants.INNER);
    }
}
