package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户和角色关联表数据访问层
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    /**
     * todo 可以优化,一个用户删除多个角色
     * 根据用户 ID 和角色 ID 删除记录
     * @param userId 用户 ID
     * @param roleId  角色 ID
     * @return 删除的记录数
     */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId} AND role_id = #{roleId}")
    int deleteUserRoleById(@Param("userId") Long userId,@Param("roleId") Long roleId);
}
