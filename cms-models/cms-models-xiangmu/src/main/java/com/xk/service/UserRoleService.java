package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.TeacherApplys;
import com.xk.entity.UserRole;

import java.util.List;
import java.util.Map;

/**
 * 用户和角色关联表服务接口
 */
public interface UserRoleService extends IService<UserRole> {
    List<Long> isStudent(List<Long> userIdList);


//    Map<Long, List<UserRole>> getUserRolesByUserIds(List<Long> userIds);
}
