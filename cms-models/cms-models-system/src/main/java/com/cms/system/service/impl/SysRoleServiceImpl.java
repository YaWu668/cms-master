package com.cms.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.SpringUtils;
import com.cms.common.core.utils.StringUtils;
import com.cms.common.security.utils.SecurityUtils;
import com.cms.system.api.domain.pojo.SysRole;
import com.cms.system.domain.vo.SysRoleListVo;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysRoleQuery;
import com.cms.system.domain.vo.RoleAddVo;
import com.cms.system.domain.vo.RoleDateScope;
import com.cms.system.domain.vo.TreeSelect;
import com.cms.system.mapper.SysMenuMapper;
import com.cms.system.mapper.SysRoleMapper;
import com.cms.system.mapper.SysUserMapper;
import com.cms.system.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色信息表 Service 实现
 *
 * @author 邓志军
 * @date 2024-05-29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private SysRoleMapper roleMapper;

    @Resource
    private SysMenuMapper sysMenuMapper;

    /**
     * 查询角色信息表列表数据(无分页)
     *
     * @param sysRole 角色信息表
     * @return 角色信息表列表数据
     */
    @Override
    public List<SysRole> listAllEntities(SysRole sysRole) {
        // 1.构建查询条件对象
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // 2.返回数据
        return this.roleMapper.selectList(queryWrapper);
    }

    /**
     * 查询角色信息表列表分页数据
     *
     * @param query 分页查询参数
     * @return 角色信息表列表数据
     */
    @Override
    public List<SysRoleListVo> listEntities(SysRoleQuery query) {
        return this.roleMapper.listEntities(query);
    }

    /**
     * 根据id查询角色信息表详细信息
     *
     * @param id 角色信息表表数据id
     */
    @Override
    public SysRoleListVo getEntityById(Long id) {
        return this.roleMapper.getEntityById(id);
    }

    /**
     * 添加角色信息表数据
     *
     * @param role 角色信息表
     * @return 添加角色信息表数据成功返回 true 否则返回 false
     */
    @Override
    public boolean addEntity(RoleAddVo role) {
        // 1.判断角色名称是否存在
        if (!this.checkRoleNameUnique(role.getRoleName())) {
            throw new ServiceException(String.format("新增角色 %s 失败，角色名称已经存在!", role.getRoleName()));
        }
        // 2.判断角色权限是否存在
        if (!this.checkRoleKeyUnique(role.getRoleKey())) {
            throw new ServiceException(String.format("新增角色 %s 失败，角色权限字符已经存在!", role.getRoleKey()));
        }
        // 3.添加数据
        return this.save(SpringUtils.copyProperties(role, SysRole.class));
    }

    /**
     * 修改角色信息表数据
     *
     * @param role 角色信息表
     * @return 修改角色信息表数据成功返回 true 否则返回 false
     */
    @Override
    public boolean updateEntity(SysRole role) {

        // 1.查询原始的数据
        SysRoleListVo roleEntity = this.getEntityById(role.getRoleId());

        // 2.判断角色名称是否存在
        if (!roleEntity.getRoleName().equals(role.getRoleName()) && !this.checkRoleNameUnique(role.getRoleName())) {
            throw new ServiceException(String.format("修改角色 %s 失败，角色名称已经存在!", role.getRoleName()));
        }

        // 3.判断角色权限是否存在
        if (!roleEntity.getRoleKey().equals(role.getRoleKey()) && !this.checkRoleKeyUnique(role.getRoleKey())) {
            throw new ServiceException(String.format("修改角色 %s 失败，角色权限字符已经存在!", role.getRoleKey()));
        }

        // 4.修改数据
        return this.updateById(SpringUtils.copyProperties(role, SysRole.class));
    }

    /**
     * 根据id删除角色信息表数据
     *
     * @param ids 角色信息表表id集合
     * @return 删除角色信息表数据成功返回 true 否则返回 false
     */
    @Override
    public boolean deleteEntityById(List<Long> ids) {
        for (Long id : ids) {
            // 1.判断该角色是否允许修改
            this.checkRoleAllowed(id);
            // 2.判断该角色下是否绑定了其它角色
            SysRoleListVo role = this.getEntityById(id);
            if (this.roleMapper.countUserRoleByRoleId(role.getRoleId()) > 0) {
                throw new ServiceException(String.format("%s已分配,不能删除", role.getRoleName()));
            }
            // 3.删除该角色与菜单的关联
            this.sysMenuMapper.deleteRoleMenu(ids);
            // 4.删除角色与部门的关联
            this.roleMapper.deleteRoleDept(ids);
            // 5.删除该角色信息
            this.removeById(id);
        }
        return true;
    }

    /**
     * 校验角色是否允许操作
     *
     * @param roleId 角色id
     */
    @Override
    public void checkRoleAllowed(Long roleId) {
        if (SecurityUtils.isAdmin(roleId)) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        // 1.查询用户角色
        List<SysRole> perms = roleMapper.selectRolePermissionByUserId(userId);

        // 2.整合用户权限
        Set<String> permsSet = new HashSet<>();
        for (SysRole perm : perms) {
            // 如果角色对象不为空，则处理其权限信息
            if (StringUtils.isNotNull(perm)) {
                // 将角色权限字符串根据逗号拆分成数组，然后添加到权限集合中
                permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
            }
        }

        // 3.返回数据
        return permsSet;
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     */
    @Override
    public boolean checkRoleNameUnique(String roleName) {
        SysRole sysRole = this.roleMapper.checkRoleNameUnique(roleName);
        return StringUtils.isNull(sysRole);
    }

    /**
     * 校验角色权限字符是否唯一
     *
     * @param roleKey 角色权限字符
     */
    @Override
    public boolean checkRoleKeyUnique(String roleKey) {
        SysRole sysRole = this.roleMapper.checkRoleKeyUnique(roleKey);
        return StringUtils.isNull(sysRole);
    }

    /**
     * 获取角色菜单权限
     *
     * @param id 角色id
     * @return 角色拥有的菜单权限
     */
    @Override
    public List<SysMenu> getRoleMenuPermission(Long id) {
        return this.sysMenuMapper.getRoleMenuPermission(id);
    }

    /**
     * 修改角色菜单权限
     *
     * @param ids 菜单id集合
     * @param id  角色id
     */
    @Override
    public boolean updateRoleMenu(List<Long> ids, Long id) {
        // 1.删除原来的所有菜单
        this.sysMenuMapper.deleteRoleMenuByRoleId(id);
        if (StringUtils.isEmpty(ids)) return true;
        // 2.添加现在的菜单
        return this.sysMenuMapper.batchRoleMenu(ids, id) > 0;
    }

    /**
     * 获取前端岗位选择
     *
     * @return 岗位选择树
     */
    @Override
    public List<TreeSelect> getRoleTree() {
        // 1.获取所有岗位数据
        List<SysRole> roleList = this.listAllEntities(new SysRole());
        // 2.构建树
        return roleList.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 取消角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    @Override
    public boolean cancelAuthUser(Long roleId, Long[] userIds) {
        return this.roleMapper.cancelAuthUser(roleId, userIds);
    }

    /**
     * 角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    @Override
    public boolean authUser(Long roleId, Long[] userIds) {
        return this.roleMapper.authUser(roleId, userIds);
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色id
     * @param status 角色状态
     */
    @Override
    public boolean updateRoleStatus(Long roleId, Integer status) {
        return this.roleMapper.updateRoleStatus(roleId, status);
    }

    /**
     * 获取角色数据权限信息
     *
     * @param id 角色id
     * @return 数据权限信息
     */
    @Override
    public RoleDateScope getRolDateScope(Long id) {
        // 1.查询角色数据权限信息
        RoleDateScope dateScope = SpringUtils.copyProperties(this.getEntityById(id), RoleDateScope.class);
        // 2.查询角色拥有哪些部门权限
        if (dateScope != null && dateScope.getDataScope().equals("2")) {
            List<Long> roleDeptDateScope = this.roleMapper.getRoleDeptDateScope(id);
            dateScope.setDeptIds(roleDeptDateScope);
        }
        // 3.返回数据
        return dateScope;
    }

    /**
     * 修改角色数据权限信息
     *
     * @param dateScope 修改信息
     */
    @Override
    public boolean updateRolDateScope(RoleDateScope dateScope) {
        // 1.修改权限信息
        this.roleMapper.updateRolDateScope(dateScope.getRoleId(), dateScope.getDataScope());
        // 2.删除部门绑定
        this.roleMapper.deleteRoleDeptByRoleId(dateScope.getRoleId());
        if (!StringUtils.isEmpty(dateScope.getDeptIds())) {
            // 3.添加部门绑定
            this.roleMapper.addRoleDeptByRoleId(dateScope.getRoleId(), dateScope.getDeptIds());
        }
        return true;
    }
}
