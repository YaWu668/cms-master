package com.cms.system.service.impl;

import com.cms.common.core.constant.CacheConstants;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.redis.service.RedisService;
import com.cms.system.api.model.LoginUser;
import com.cms.system.domain.vo.SysUserOnline;
import com.cms.system.service.SysUserOnlineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SysUserOnlineServiceImpl implements SysUserOnlineService {

    @Resource
    private RedisService redisService;

    /**
     * 获取在线用户列表
     *
     * @param userName 用户名称
     * @return 在线用户回合集合
     */
    @Override
    public List<SysUserOnline> getOnlineUser(String userName) {
        // 获取所有与 LOGIN_TOKEN_KEY 匹配的 Redis 键
        Collection<String> keys = this.redisService.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        // 创建一个列表来存储符合条件的用户
        List<SysUserOnline> list = new ArrayList<>();

        // 遍历所有 Redis 键
        for (String key : keys) {
            // 从 Redis 中获取与当前键关联的 LoginUser 对象
            LoginUser loginUser = this.redisService.getCacheObject(key);
            // 确保 loginUser 对象不为空
            if (loginUser != null) {
                // 判断用户名是否匹配：如果 userName 为空，则直接匹配
                boolean matchesUserName = StringUtils.isEmpty(userName) ||
                        loginUser.getSysUser().getUserName().contains(userName);
                // 如果用户名匹配，则将其转换为 SysUserOnline 对象并添加到列表中
                if (matchesUserName) {
                    list.add(this.loginUserToUserOnline(loginUser));
                }
            }
        }

        // 返回符合条件的在线用户列表
        return list;
    }

    /**
     * 强退在线用户
     *
     * @param tokenId 会话id
     */
    @Override
    public boolean forceLogout(String tokenId) {
        return this.redisService.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
    }


    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    private SysUserOnline loginUserToUserOnline(LoginUser user) {
        if (StringUtils.isNull(user)) {
            return null;
        }
        return SysUserOnline.builder()
                .tokenId(user.getToken())
                .userName(user.getUsername())
                .ipaddr(user.getIpaddr())
                .loginTime(user.getLoginTime())
                .build();
    }
}
