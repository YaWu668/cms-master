package com.cms.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.system.api.domain.pojo.SysRole;
import com.cms.system.domain.vo.SysRoleListVo;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysRoleQuery;
import com.cms.system.domain.vo.RoleAddVo;
import com.cms.system.domain.vo.RoleDateScope;
import com.cms.system.domain.vo.TreeSelect;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * 角色信息表 Service 接口
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 查询角色信息表列表数据(无分页)
     *
     * @param sysRole 角色信息表
     * @return 角色信息表列表数据
     */
    List<SysRole> listAllEntities(SysRole sysRole);

    /**
     * 查询角色信息表列表分页数据
     *
     * @param query 分页查询参数
     * @return 角色信息表列表数据
     */
    List<SysRoleListVo> listEntities(SysRoleQuery query);

    /**
     * 根据id查询角色信息表详细信息
     *
     * @param id 角色信息表表数据id
     */
    SysRoleListVo getEntityById(Long id);

    /**
     * 添加角色信息表数据
     *
     * @param role 角色信息
     * @return 添加角色信息表数据成功返回 true 否则返回 false
     */
    boolean addEntity(RoleAddVo role);

    /**
     * 修改角色信息表数据
     *
     * @param role 角色信息
     * @return 修改角色信息表数据成功返回 true 否则返回 false
     */
    boolean updateEntity(SysRole role);

    /**
     * 根据id删除角色信息表数据
     *
     * @param ids 角色信息表表id集合
     * @return 删除角色信息表数据成功返回 true 否则返回 false
     */
    @Transactional
    boolean deleteEntityById(List<Long> ids);

    /**
     * 校验角色是否允许操作
     *
     * @param roleId 角色id
     */
    void checkRoleAllowed(Long roleId);

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRolePermissionByUserId(Long userId);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     */
    boolean checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限字符是否唯一
     *
     * @param roleKey 角色权限字符
     */
    boolean checkRoleKeyUnique(String roleKey);

    /**
     * 获取角色菜单权限
     *
     * @param id 角色id
     * @return 角色拥有的菜单权限
     */
    List<SysMenu> getRoleMenuPermission(Long id);

    /**
     * 修改角色菜单权限
     *
     * @param ids 菜单id集合
     * @param id  角色id
     */
    @Transactional
    boolean updateRoleMenu(List<Long> ids, Long id);

    /**
     * 获取前端岗位选择
     *
     * @return 岗位选择树
     */
    List<TreeSelect> getRoleTree();

    /**
     * 取消角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    boolean cancelAuthUser(Long roleId, Long[] userIds);

    /**
     * 角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    boolean authUser(Long roleId, Long[] userIds);

    /**
     * 修改角色状态
     *
     * @param roleId 角色id
     * @param status 角色状态
     */
    boolean updateRoleStatus(Long roleId, Integer status);

    /**
     * 获取角色数据权限信息
     *
     * @param id 角色id
     * @return 数据权限信息
     */
    RoleDateScope getRolDateScope(Long id);

    /**
     * 修改角色数据权限信息
     *
     * @param dateScope 修改信息
     */
    @Transactional
    boolean updateRolDateScope(RoleDateScope dateScope);
}
