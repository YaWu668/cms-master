package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.domain.dto.SysMenuDto;
import com.cms.system.domain.pojo.SysMenu;
import com.cms.system.domain.query.SysMenuQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 菜单权限表Mapper
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByRoleId(Long roleId);

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<SysMenuDto> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenuDto> selectMenuTreeByUserId(Long userId);


    /**
     * 查询菜单权限表列表数据(无分页)
     *
     * @param query 菜单权限表查询条件
     * @return 菜单权限表列表数据
     */
    List<SysMenuDto> listEntities(SysMenuQuery query);

    /**
     * 获取角色菜单权限
     *
     * @param id 角色id
     * @return 角色拥有的菜单权限
     */
    List<SysMenu> getRoleMenuPermission(Long id);

    /**
     * 根据角色id删除角色菜单权限
     *
     * @param roleId 角色id
     */
    @Delete("delete from sys_role_menu where role_id=#{roleId}")
    boolean deleteRoleMenuByRoleId(Long roleId);


    /**
     * 绑定角色与菜单权限
     *
     * @param menuIds 菜单id集合
     * @param roleIds 角色id
     */
    int batchRoleMenu(@Param("menuIds") List<Long> menuIds, @Param("roleIds") Long roleIds);

    /**
     * 批量删除角色菜单关联信息
     *
     * @param ids 需要删除的数据ID
     */
    void deleteRoleMenu(List<Long> ids);

    /**
     * 查询该菜单下是否存在子菜单
     *
     * @param menuId 菜单ID
     */
    @Select("select count(1) from sys_menu where parent_id = #{menuId}")
    Integer hasChildByMenuId(@Param("menuId") Long menuId);

    /**
     * 查询该菜单是否被角色绑定
     *
     * @param menuId 菜单ID
     */
    @Select("select count(1) from sys_role_menu where menu_id = #{menuId}")
    Integer checkMenuExistRole(@Param("menuId") Long menuId);
}
