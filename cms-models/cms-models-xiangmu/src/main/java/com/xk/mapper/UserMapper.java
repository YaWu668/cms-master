package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 用户信息表(User)表数据库访问层
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
public interface UserMapper extends BaseMapper<User> {


    /**
     * 根据用户id查询该用户拥有哪些角色
     *
     * @param id 用户id
     * @return 角色id集合
     */
    @Select("select ur.role_id from sys_user_role ur where ur.user_id = #{id}")
    List<Long> getUserRolesIds(Long id);

}

