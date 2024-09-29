package com.cms.common.security.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;

import com.cms.common.core.constant.CacheConstants;
import com.cms.common.core.constant.SecurityConstants;
import com.cms.common.core.utils.JwtUtils;
import com.cms.common.core.utils.ip.IpUtils;
import com.cms.common.core.utils.ServletUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.core.utils.uuid.IdUtils;
import com.cms.common.redis.service.RedisService;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * token验证处理
 *
 * @author 邓志军
 */
@Component
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     * @param loginUser 登录用户信息
     * @return 包含访问令牌和过期时间的Map对象
     */
    public Map<String, Object> createToken(LoginUser loginUser) {
        // 生成令牌
        String token = IdUtils.fastUUID();

        // 获取用户ID和用户名
        Long userId = loginUser.getSysUser().getUserId();
        String userName = loginUser.getSysUser().getUserName();

        // 设置登录用户的令牌、用户ID、用户名和IP地址
        loginUser.setToken(token);
        loginUser.setUserid(userId);
        loginUser.setUsername(userName);
        loginUser.setIpaddr(IpUtils.getIpAddr());

        // 刷新登录用户的信息
        refreshToken(loginUser);

        // 准备Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put(SecurityConstants.USER_KEY, token); // 将令牌存储为用户键
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId); // 存储用户ID
        claimsMap.put(SecurityConstants.DETAILS_USERNAME, userName); // 存储用户名

        // 准备接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap)); // 添加访问令牌
        rspMap.put("expires_in", expireTime); // 添加过期时间

        return rspMap; // 返回包含访问令牌和过期时间的Map对象
    }


    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser() {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(String token) {
        LoginUser user = null;
        try {
            if (StringUtils.isNotEmpty(token)) {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        } catch (Exception e) {
            log.error("获取用户信息异常'{}'", e.getMessage());
        }
        return user;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (StringUtils.isNotNull(loginUser) && StringUtils.isNotEmpty(loginUser.getToken())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token) {
        if (StringUtils.isNotEmpty(token)) {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUser
     */
    public void verifyToken(LoginUser loginUser) {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisService.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String token) {
        return ACCESS_TOKEN + token;
    }
}