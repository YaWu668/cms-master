package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.xk.constant.RoleConstant;
import com.xk.entity.Role;
import com.xk.mapper.RoleMapper;
import com.xk.service.RoleService;
import org.springframework.stereotype.Service;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author YaWu
 * @since 2024-12-18 00:29:34
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

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
}

