package com.cms.auth.service;

import com.cms.common.core.constant.CacheConstants;
import com.cms.common.core.constant.Constants;
import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.enums.UserStatus;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.text.Convert;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.utils.ip.IpUtils;
import com.cms.common.core.web.domain.Response;
import com.cms.common.redis.service.RedisService;
import com.cms.system.api.RemoteUserService;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.model.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 系统登录校验
 *
 * @author 邓志军
 * @date 2024年5月28日16:16:33
 */
@Component
public class SysLoginService {

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    @Resource
    private RedisService redisService;

    /**
     * 用户登录
     *
     * @param username 用户账号
     * @param password 用户密码
     * @return 登录对象
     */
    public LoginUser login(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(username, password)) {
            this.recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户/密码必须填写", UserConstants.LOGIN);
            throw new ServiceException("用户/密码必须填写");
        }

        // 判断密码长度是否在范围内
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            this.recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户密码不在指定范围", UserConstants.LOGIN);
            throw new ServiceException("用户密码不在指定范围");
        }

        // 判断账号长度是否在范围内
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            this.recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户名不在指定范围", UserConstants.LOGIN);
            throw new ServiceException("用户名不在指定范围");
        }

        // IP黑名单校验
        String blackStr = Convert.toStr(redisService.getCacheObject(CacheConstants.SYS_LOGIN_BLACKIPLIST));
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "很遗憾，访问IP已被列入系统黑名单", UserConstants.LOGIN);
            throw new ServiceException("很遗憾，访问IP已被列入系统黑名单");
        }

        // 查询用户信息
        Response<LoginUser> userResult = remoteUserService.getUserInfoByUserName(username, SecurityConstants.INNER);

        // 判断查询结果
        if (StringUtils.isNull(userResult.getData())) {
            this.recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "登录用户不存在", UserConstants.LOGIN);
            throw new ServiceException("登录用户：" + username + " 不存在");
        }

        // 判断返回值是否是失败
        if (Constants.FAIL.equals(userResult.getCode())) {
            throw new ServiceException(userResult.getMsg());
        }

        SysUserDto user = userResult.getData().getSysUser();

        // 判断账号是否被删除
        if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            this.recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "对不起，您的账号已被删除", UserConstants.LOGIN);
            throw new ServiceException("对不起，您的账号：" + username + " 已被删除");
        }

        // 判断账号是否被停用
        if (UserStatus.DELETED.getCode().equals(user.getStatus())) {
            this.recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, "用户已停用，请联系管理员", UserConstants.LOGIN);
            throw new ServiceException("对不起，您的账号：" + username + " 已停用");
        }

        // 校验密码是否正确
        this.passwordService.validate(user, password);

        // 记录登录成功日志
        this.recordLogService.recordLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功", UserConstants.LOGIN);

        return userResult.getData();
    }

    /**
     * 登录退出
     *
     * @param loginName 登录的名称
     */
    public void logout(String loginName) {
        recordLogService.recordLogininfor(loginName, Constants.LOGOUT, "退出成功", UserConstants.LOGOUT);
    }
}
