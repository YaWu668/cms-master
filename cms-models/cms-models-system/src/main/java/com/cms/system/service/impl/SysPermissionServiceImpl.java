package com.cms.system.service.impl;

import com.cms.common.core.constant.UserConstants;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.dto.SysRoleDto;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.service.SysMenuService;
import com.cms.system.service.SysPermissionService;
import com.cms.system.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户权限处理
 *
 * @author 邓志军
 * @date 2024年5月29日17:57:34
 */
@Service
public class SysPermissionServiceImpl implements SysPermissionService {

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysMenuService menuService;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(SysUserDto user) {
        Set<String> roles = new HashSet<>();

        // 1.判断是否是超级管理员，管理员拥有所有权限
        if(SecurityUtils.isAdmin(user.getUserName())) {
            roles.add(UserConstants.ADMIN);
        } else {
            // 2.不是管理员，获取数据
            roles.addAll(this.roleService.selectRolePermissionByUserId(user.getUserId()));
        }

        // 3.返回结果
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUserDto user) {
        Set<String> perms = new HashSet<>();

        // 1.判断是否是超级管理员，管理员拥有所有权限
        if(SecurityUtils.isAdmin(user.getUserName())) {
            perms.add(UserConstants.PERMISSION);
        } else {
            List<SysRoleDto> roles = user.getRoles();
            if (!CollectionUtils.isEmpty(roles)) {
                // 多角色设置permissions属性，以便数据权限匹配权限
                for (SysRoleDto role : roles) {
                    // 根据角色id查询权限信息
                    Set<String> rolePerms = this.menuService.selectMenuPermsByRoleId(role.getRoleId());
                    role.setPermissions(rolePerms);
                    perms.addAll(rolePerms);
                }
            } else {
                // 根据用户id查询权限信息
                perms.addAll(menuService.selectMenuPermsByUserId(user.getUserId()));
            }
        }
        return perms;
    }
}
