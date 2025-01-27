package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author YaWu
 * @since 2024-12-18 00:29:34
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 检查用户是否拥有指定角色
     *
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 返回匹配的记录数
     */
    @Select("SELECT COUNT(*) FROM sys_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    Long checkUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 根据用户id和角色key查询用户是否拥有该角色
     * @param userId  用户id
     * @param roleKey 角色key
     * @return 角色数量
     */
    @Select("SELECT  count(sr.role_key) FROM sys_role sr JOIN sys_user_role sur ON sr.role_id = sur.role_id WHERE   sr.del_flag = '0' AND  sur.user_id = #{userId} AND sr.role_key = #{roleKey}")
    int isUserHasRole( @Param("userId")Long userId,@Param("roleKey") String roleKey);

    /**
     * 根据角色key查询角色id
     * @param roleKey 角色key
     * @return 角色id
     */
    @Select("SELECT  DISTINCT role_id  FROM sys_role WHERE del_flag = '0' AND  role_key = #{roleKey}")
    List<Long> getRoleIdByRoleKey(@Param("roleKey") String roleKey);
}

