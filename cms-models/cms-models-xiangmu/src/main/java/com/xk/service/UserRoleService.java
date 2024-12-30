package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.Role;
import com.xk.entity.TeacherApplys;
import com.xk.entity.UserRole;

import java.util.List;
import java.util.Map;

/**
 * 用户和角色关联表服务接口
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据角色集合获取用户ID集合
     * @param roleList 角色集合
     * @return 用户ID集合
     */
    List<Long> getUserIdsByRoleList(List<Role> roleList);



    List<Long> isNoStudent(List<Long> userIdList);




}
