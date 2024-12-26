package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.constant.RoleConstant;
import com.xk.entity.Role;
import com.xk.mapper.RoleMapper;
import com.xk.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-18 00:29:34
 */
@Service("roleService")
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {


    private final RoleMapper roleMapper;

    @Override
    public boolean existRole(String roleKey) {
        LambdaQueryWrapper<Role> eq = new LambdaQueryWrapper<Role>().eq(Role::getRoleKey, roleKey);
        if (this.count(eq) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Role selectByRoleKey(String roleKey) {
        LambdaQueryWrapper<Role> eq = new LambdaQueryWrapper<Role>().eq(Role::getRoleKey, roleKey);
        Role one = this.getOne(eq);
        if (one == null) {
            throw new ServiceException("角色不存在",444);
        }
        return one;
    }

    @Override
    public Long getAdminId() {
        LambdaQueryWrapper<Role> eq = new LambdaQueryWrapper<Role>().eq(Role::getRoleKey, RoleConstant.ADMIN);
        Long roleId = this.getOne(eq).getRoleId();
        if(roleId == null){
            throw  new ServiceException("一生一项目的管理员角色不存在,请联系管理员修复");
        }
        return roleId;
    }

    @Override
    public boolean hasRole(String role) {
        //1.获取用户的id
        Long userId = SecurityUtils.getUserId();
        //2.查询角色是否存在
        Role roleKey = this.selectByRoleKey(role);
        Long aLong = roleMapper.checkUserRole(userId, roleKey.getRoleId());
        if (aLong == null ){
            throw new ServiceException("角色不存在,请联系管理员修复",444);
        }
        //3.判断用户是否有角色
        if(aLong > 0){
            return true;
        }
        return false;
    }
}

