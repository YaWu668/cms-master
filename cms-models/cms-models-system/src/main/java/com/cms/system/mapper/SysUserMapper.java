package com.cms.system.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cms.system.api.domain.dto.SysUserDto;
import com.cms.system.api.domain.pojo.SysUser;
import com.cms.system.domain.query.SysUserQuery;
import com.cms.system.domain.vo.UserUpdateStatusVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 系统用户信息Mapper
 *
 * @author 邓志军
 * @date 2024-05-28
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据id查询系统用户信息详细信息
     *
     * @param id 系统用户信息表数据id
     */
    @Select("select * from sys_user where user_id = #{id}")
    SysUserDto getEntityById(Long id);


    /**
     * 查询用户列表数据
     *
     * @param query 查询参数
     */
    List<SysUserDto> listEntities(SysUserQuery query);

    /**
     * 根据用户id查询该用户拥有哪些岗位
     *
     * @param id 用户id
     * @return 岗位id集合
     */
    @Select("select up.post_id from sys_user_post up where up.user_id = #{id}")
    List<Long> getUserPostIds(Long id);

    /**
     * 根据用户id查询该用户拥有哪些角色
     *
     * @param id 用户id
     * @return 角色id集合
     */
    @Select("select ur.role_id from sys_user_role ur where ur.user_id = #{id}")
    List<Long> getUserRolesIds(Long id);

    /**
     * 查询已分配用户角色列表
     *
     * @param query  系统用户信息
     * @param roleId 角色id
     * @return 已分配系统用户信息列表数据
     */
    List<SysUserDto> selectAllocatedList(@Param("query") SysUserQuery query, @Param("roleId") Long roleId);

    /**
     * 查询未分配用户角色列表
     *
     * @param query  系统用户信息
     * @param roleId 角色id
     * @return 未分配系统用户信息列表数据
     */
    List<SysUserDto> selectUnallocatedList(@Param("query") SysUserQuery query, @Param("roleId") Long roleId);

    /**
     * 修改用户密码
     *
     * @param userId      用户id
     * @param newPassword 用户新密码
     */
    @Update("update sys_user set password = #{newPassword} where user_id = #{userId}")
    boolean updateUserPassword(@Param("userId") Long userId, @Param("newPassword") String newPassword);

    /**
     * 修改用户头像
     *
     * @param userId 用户id
     * @param url    头像地址
     */
    @Update("update sys_user set avatar = #{url} where user_id = #{userId}")
    boolean updateAvatar(@Param("userId") Long userId, @Param("url") String url);

    /**
     * 修改用户状态
     *
     * @param userUpdateStatus 用户状态更新对象
     * @return 更新操作是否成功
     */
    @Update("update sys_user set status = #{status} where user_id = #{userId}")
    boolean updateStatus(UserUpdateStatusVo userUpdateStatus);
}
