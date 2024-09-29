package com.cms.common.security.auth;


import com.cms.common.core.constant.UserConstants;
import com.cms.common.core.context.SecurityContextHolder;
import com.cms.common.core.exception.auth.NotLoginException;
import com.cms.common.core.exception.auth.NotPermissionException;
import com.cms.common.core.exception.auth.NotRoleException;
import com.cms.common.core.utils.SpringUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.security.annotation.Logical;
import com.cms.common.security.annotation.RequiresLogin;
import com.cms.common.security.annotation.RequiresPermissions;
import com.cms.common.security.annotation.RequiresRoles;
import com.cms.common.security.service.TokenService;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.model.LoginUser;
import org.springframework.util.PatternMatchUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Token 权限验证，逻辑实现类
 *
 * @author 邓志军
 * @date 2024年5月29日13:42:30
 */
public class AuthLogic {
    /**
     * 所有权限标识
     */
    private static final String ALL_PERMISSION = UserConstants.PERMISSION;

    /**
     * 管理员角色权限标识
     */
    private static final String SUPER_ADMIN = UserConstants.ADMIN;

    public TokenService tokenService = SpringUtils.getBean(TokenService.class);

    /**
     * 会话注销
     */
    public void logout() {
        String token = SecurityUtils.getToken();
        if (token == null) {
            return;
        }
        logoutByToken(token);
    }

    /**
     * 会话注销，根据指定Token
     */
    public void logoutByToken(String token) {
        tokenService.delLoginUser(token);
    }

    /**
     * 检验用户是否已经登录，如未登录，则抛出异常
     */
    public void checkLogin() {
        getLoginUser();
    }

    /**
     * 获取当前用户缓存信息, 如果未登录，则抛出异常
     *
     * @return 用户缓存信息
     */
    public LoginUser getLoginUser() {
        // 1.从上下文中获取token
        String token = SecurityUtils.getToken();
        if (token == null) {
            throw new NotLoginException("未提供token");
        }
        // 2.从上下文中获取登录对象
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser == null) {
            throw new NotLoginException("无效的token");
        }

        // 3.返回数据
        return loginUser;
    }

    /**
     * 获取当前用户缓存信息, 如果未登录，则抛出异常
     *
     * @param token 前端传递的认证信息
     * @return 用户缓存信息
     */
    public LoginUser getLoginUser(String token) {
        return tokenService.getLoginUser(token);
    }

    /**
     * 验证当前用户有效期, 如果相差不足120分钟，自动刷新缓存
     *
     * @param loginUser 当前用户信息
     */
    public void verifyLoginUserExpire(LoginUser loginUser) {
        tokenService.verifyToken(loginUser);
    }

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        return hasPermi(getPermiList(), permission);
    }

    /**
     * 验证用户是否具备某权限, 如果验证未通过，则抛出异常: NotPermissionException
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public void checkPermi(String permission) {
        if (!hasPermi(getPermiList(), permission)) {
            throw new NotPermissionException(permission);
        }
    }

    /**
     * 根据注解(@RequiresPermissions)鉴权, 如果验证未通过，则抛出异常
     *
     * @param requiresPermissions 注解对象
     */
    public void checkPermi(RequiresPermissions requiresPermissions) {
        // 1.设置本次权限，将注解上的所有权限值进进行拼装
        SecurityContextHolder.setPermission(StringUtils.join(requiresPermissions.value(), ","));

        // 2.判断校验模式是否是 AND 模式，
        if (requiresPermissions.logical() == Logical.AND) {
            this.checkPermiAnd(requiresPermissions.value());
        } else {
            // 3.校验 OR 模式
            this.checkPermiOr(requiresPermissions.value());
        }
    }

    /**
     * 验证用户是否含有指定权限，必须全部拥有
     *
     * @param permissions 权限列表
     */
    public void checkPermiAnd(String... permissions) {
        // 1.从上下文线程中获取当前登录的用户权限
        Set<String> permissionList = this.getPermiList();

        // 2.遍历权限，进行校验，判断是否包含这个权限
        for (String permission : permissions) {
            if (!this.hasPermi(permissionList, permission)) {
                throw new NotPermissionException(permission);
            }
        }
    }

    /**
     * 验证用户是否含有指定权限，只需包含其中一个
     *
     * @param permissions 权限码数组
     */
    public void checkPermiOr(String... permissions) {
        // 1.从上下文对象中获取用户权限
        Set<String> permissionList = this.getPermiList();

        // 2.判断用户是否包含这个权限
        for (String permission : permissions) {
            if (hasPermi(permissionList, permission)) {
                return;
            }
        }

        // 3.没有权限抛出异常
        if (permissions.length > 0) {
            throw new NotPermissionException(permissions);
        }
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色标识
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        return hasRole(getRoleList(), role);
    }

    /**
     * 判断用户是否拥有某个角色, 如果验证未通过，则抛出异常: NotRoleException
     *
     * @param role 角色标识
     */
    public void checkRole(String role) {
        if (!hasRole(role)) {
            throw new NotRoleException(role);
        }
    }

    /**
     * 根据注解(@RequiresRoles)鉴权
     *
     * @param requiresRoles 注解对象
     */
    public void checkRole(RequiresRoles requiresRoles) {
        if (requiresRoles.logical() == Logical.AND) {
            checkRoleAnd(requiresRoles.value());
        } else {
            checkRoleOr(requiresRoles.value());
        }
    }

    /**
     * 验证用户是否含有指定角色，必须全部拥有
     *
     * @param roles 角色标识数组
     */
    public void checkRoleAnd(String... roles) {
        Set<String> roleList = getRoleList();
        for (String role : roles) {
            if (!hasRole(roleList, role)) {
                throw new NotRoleException(role);
            }
        }
    }

    /**
     * 验证用户是否含有指定角色，只需包含其中一个
     *
     * @param roles 角色标识数组
     */
    public void checkRoleOr(String... roles) {
        Set<String> roleList = getRoleList();
        for (String role : roles) {
            if (hasRole(roleList, role)) {
                return;
            }
        }
        if (roles.length > 0) {
            throw new NotRoleException(roles);
        }
    }

    /**
     * 根据注解(@RequiresLogin)鉴权
     *
     * @param at 注解对象
     */
    public void checkByAnnotation(RequiresLogin at) {
        this.checkLogin();
    }

    /**
     * 根据注解(@RequiresRoles)鉴权
     *
     * @param at 注解对象
     */
    public void checkByAnnotation(RequiresRoles at) {
        String[] roleArray = at.value();
        if (at.logical() == Logical.AND) {
            this.checkRoleAnd(roleArray);
        } else {
            this.checkRoleOr(roleArray);
        }
    }

    /**
     * 根据注解(@RequiresPermissions)鉴权
     *
     * @param at 注解对象
     */
    public void checkByAnnotation(RequiresPermissions at) {
        String[] permissionArray = at.value();
        if (at.logical() == Logical.AND) {
            this.checkPermiAnd(permissionArray);
        } else {
            this.checkPermiOr(permissionArray);
        }
    }

    /**
     * 获取当前账号的角色列表
     *
     * @return 角色列表
     */
    public Set<String> getRoleList() {
        try {
            LoginUser loginUser = getLoginUser();
            return loginUser.getRoles();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 获取当前账号的权限列表
     *
     * @return 权限列表
     */
    public Set<String> getPermiList() {
        try {
            LoginUser loginUser = this.getLoginUser();
            return loginUser.getPermissions();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    /**
     * 判断是否包含权限
     *
     * @param authorities 权限列表
     * @param permission  权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 判断是否包含角色
     *
     * @param roles 角色列表
     * @param role  角色
     * @return 用户是否具备某角色权限
     */
    public boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }
}
