package com.cms.system.service;

import com.cms.system.domain.vo.SysUserOnline;

import java.util.List;

public interface SysUserOnlineService {

    /**
     * 获取在线用户列表
     *
     * @param userName 用户名称
     * @return 在线用户回合集合
     */
    List<SysUserOnline> getOnlineUser(String userName);

    /**
     * 强退在线用户
     *
     * @param tokenId 会话id
     */
    boolean forceLogout(String tokenId);
}
