package com.cms.system.service;

import com.cms.system.api.domain.dto.SysUserDto;

import java.util.Set;

/**
 * 权限信息 服务层
 *
 * @author 邓志军
 * @date 2024年5月29日17:56:38
 */
public interface SysPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUserDto user);

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUserDto user);
}
