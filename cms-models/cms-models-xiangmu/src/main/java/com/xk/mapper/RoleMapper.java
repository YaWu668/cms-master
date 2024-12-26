package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


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
}

