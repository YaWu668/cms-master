package com.cms.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.page.PageParams;
import com.cms.system.domain.dto.SysMenuDto;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysMenuQuery;
import com.cms.system.domain.vo.RouterVo;
import com.cms.system.domain.vo.TreeSelect;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 菜单权限表 Service 接口
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询菜单权限表列表数据(无分页)
     *
     * @param query 菜单权限表查询条件
     * @return 菜单权限表列表数据
     */
    List<SysMenuDto> listEntities(SysMenuQuery query);

    /**
     * 根据id查询菜单权限表详细信息
     *
     * @param id 菜单权限表表数据id
     */
    SysMenu getEntityById(Long id);

    /**
     * 添加菜单权限表数据
     *
     * @param sysMenu 菜单权限表
     * @return 添加菜单权限表数据成功返回 true 否则返回 false
     */
    boolean addEntity(SysMenu sysMenu);

    /**
     * 修改菜单权限表数据
     *
     * @param sysMenu 菜单权限表
     * @return 修改菜单权限表数据成功返回 true 否则返回 false
     */
    boolean updateEntity(SysMenu sysMenu);

    /**
     * 根据id删除菜单权限表数据
     *
     * @param id 菜单id
     * @return 删除菜单权限表数据成功返回 true 否则返回 false
     */
    boolean deleteEntityById(Long id);

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    Set<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Collection<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenuDto> selectMenuTreeByUserId(Long userId);

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    List<RouterVo> buildMenus(List<SysMenuDto> menus);

    /**
     * 构建前端列表所需要的菜单
     *
     * @param menus 菜单列表
     * @return 列表树
     */
    List<SysMenuDto> buildListMenus(List<SysMenuDto> menus);

    /**
     * 获取菜单下拉列表树
     */
    List<TreeSelect> getMenuTree();
}
