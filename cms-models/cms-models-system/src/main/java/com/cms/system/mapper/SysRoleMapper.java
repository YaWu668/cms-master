package com.cms.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.pojo.SysRole;
import com.cms.system.domain.vo.SysRoleListVo;
import com.cms.system.domain.query.SysRoleQuery;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 角色信息表Mapper
 *
 * @author 邓志军
 * @date 2024-05-29
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 校验角色名称是否唯一
     *
     * @param roleName 角色名称
     */
    @Select("select * from sys_role where role_name = #{roleName}")
    SysRole checkRoleNameUnique(String roleName);

    /**
     * 校验角色权限字符是否唯一
     *
     * @param roleKey 角色权限字符
     */
    @Select("select * from sys_role where role_key = #{roleKey}")
    SysRole checkRoleKeyUnique(String roleKey);

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
    @Select("select role_id,role_name,role_key,role_sort,data_scope,status,remark from sys_role where role_id = #{id} ")
    SysRoleListVo getEntityById(Long id);

    /**
     * 根据角色id查询，该角色下绑定了多少个用户
     *
     * @param roleId 角色Id
     */
    @Select("select count(1) from sys_user_role where role_id = #{roleId}")
    Integer countUserRoleByRoleId(Long roleId);

    /**
     * 批量删除角色部门关联信息
     *
     * @param ids 需要删除的数据ID
     */
    void deleteRoleDept(List<Long> ids);

    /**
     * 取消角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    boolean cancelAuthUser(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);

    /**
     * 角色用户授权
     *
     * @param roleId  角色id
     * @param userIds 用户id集合
     */
    boolean authUser(@Param("roleId") Long roleId, @Param("userIds") Long[] userIds);

    /**
     * 修改角色状态
     *
     * @param roleId 角色id
     * @param status 角色状态
     */
    @Update("update sys_role set status = #{status} where role_id = #{roleId}")
    boolean updateRoleStatus(@Param("roleId") Long roleId, @Param("status") Integer status);

    /**
     * 获取角色部门数据权限
     *
     * @param id 角色id
     * @return 部门id集合
     */
    @Select("select dept_id from sys_role_dept where role_id = #{id}")
    List<Long> getRoleDeptDateScope(Long id);

    /**
     * 修改角色数据权限信息
     *
     * @param id        角色id
     * @param dataScope 修改信息
     */
    @Update("update sys_role set data_scope = #{dataScope} where role_id = #{id}")
    void updateRolDateScope(@Param("id") Long id, @Param("dataScope") String dataScope);

    /**
     * 根据角色id删除所有角色绑定部门
     *
     * @param roleId 角色id
     */
    @Delete("delete from sys_role_dept where role_id = #{roleId}")
    void deleteRoleDeptByRoleId(Long roleId);

    /**
     * 根据用户id，取消该用户绑定的所有角色
     * @param userId 用户id
     */
    @Delete("delete from sys_user_role where user_id = #{userId}")
    void cancelUserBuildUser(Long userId);

    /**
     * 根据角色id绑定部门
     *
     * @param roleId  角色id
     * @param deptIds 部门id
     */
    void addRoleDeptByRoleId(@Param("roleId") Long roleId, @Param("deptIds") List<Long> deptIds);
}
