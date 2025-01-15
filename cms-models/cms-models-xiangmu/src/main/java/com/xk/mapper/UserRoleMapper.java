package com.xk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xk.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户和角色关联表数据访问层
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
