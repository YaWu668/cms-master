package com.cms.auth.service;

import com.cms.common.core.constant.CacheConstants;
import com.cms.common.core.constant.Constants;
import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.redis.service.RedisService;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.dto.SysUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 密码校验服务
 * SysPasswordService 类用于提供用户密码校验服务，包括校验用户密码、获取缓存键名、匹配密码、清除登录记录缓存等功能。
 * 注意：该类需要结合 SecurityUtils 类中的密码匹配方法进行密码校验。
 *
 * @author 邓志军
 * @date 2024年5月28日22:45:43
 */
@Component
public class SysPasswordService {

    // 密码错误最大重试次数
    private final int maxRetryCount = CacheConstants.PASSWORD_MAX_RETRY_COUNT;

    // 密码锁定时长
    private final Long lockTime = CacheConstants.PASSWORD_LOCK_TIME;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 校验用户密码
     * <p>
     * validate 方法用于校验用户输入的密码是否与数据库中存储的密码匹配。
     *
     * @param user     用户信息，包含用户的相关数据
     * @param password 用户输出的密码
     */
    public void validate(SysUserDto user, String password) {
        // 获取用户用户名
        String username = user.getUserName();

        // 从缓存中获取密码输入错误次数
        Integer retryCount = redisService.getCacheObject(getCacheKey(username));

        // 如果缓存中不存在记录，则初始化为0
        if (retryCount == null) {
            retryCount = 0;
        }

        // 检查密码输入错误次数是否超过最大允许次数，如果超过则锁定帐户一段时间
        if (retryCount >= maxRetryCount) {
            String errMsg = String.format("密码输入错误%s次，帐户锁定%s分钟", maxRetryCount, lockTime);
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, errMsg, UserConstants.LOGIN);
            throw new ServiceException(errMsg);
        }

        // 验证用户输入的密码是否与数据库中存储的密码匹配
        if (!matches(user, password)) {
            // 密码不匹配，增加错误次数并记录日志
            retryCount = retryCount + 1;
            recordLogService.recordLogininfor(username, Constants.LOGIN_FAIL, String.format("密码输入错误%s次", retryCount),UserConstants.LOGIN);
            redisService.setCacheObject(getCacheKey(username), retryCount, lockTime, TimeUnit.MINUTES);
            throw new ServiceException("用户不存在/密码错误");
        } else {
            // 密码匹配，清除错误次数记录
            clearLoginRecordCache(username);
        }
    }

    /**
     * 登录账户密码错误次数缓存键名
     * getCacheKey 方法用于获取登录账户密码错误次数的缓存键名。
     *
     * @param username 用户名
     * @return 缓存键key
     */
    private String getCacheKey(String username) {
        return CacheConstants.PWD_ERR_CNT_KEY + username;
    }

    /**
     * 匹配密码
     * matches 方法用于匹配用户输入的原始密码和数据库中存储的加密密码是否匹配。
     *
     * @param user        用户信息
     * @param rawPassword 用户输入的原始密码
     * @return 匹配结果，true 表示匹配成功，false 表示匹配失败
     */
    public boolean matches(SysUserDto user, String rawPassword) {
        return SecurityUtils.matchesPassword(rawPassword, user.getPassword());
    }

    /**
     * 清除登录记录缓存
     * clearLoginRecordCache 方法用于清除登录记录的缓存，通常在用户成功登录后调用，以清除登录失败次数等相关信息。
     *
     * @param loginName 登录名
     */
    public void clearLoginRecordCache(String loginName) {
        if (redisService.hasKey(getCacheKey(loginName))) {
            redisService.deleteObject(getCacheKey(loginName));
        }
    }
}
