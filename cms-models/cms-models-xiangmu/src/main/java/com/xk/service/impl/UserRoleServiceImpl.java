package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.User;
import com.xk.entity.UserRole;
import com.xk.mapper.UserMapper;
import com.xk.mapper.UserRoleMapper;
import com.xk.service.UserRoleService;
import com.xk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户和角色关联表实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Resource
    private  UserRoleMapper userRoleMapper;
    @Override
    public List<Long> isStudent(List<Long> userIdList) {
        System.out.println("11111111111111111111111111");
        List<Long> isNoStudentIds = new ArrayList<>();
        System.out.println("22222222222222222222222222");
        Map<Long, List<UserRole>> userRoleMap = getUserRolesByUserIds(userIdList);
        System.out.println("3333333333333333333333333333");
        userIdList.forEach(userId -> {
            List<UserRole> roles = userRoleMap.getOrDefault(userId, Collections.emptyList());
            if (isNo102(roles)) {
                isNoStudentIds.add(userId);
            }
        });
        System.out.println("444444444444444444444444444444");
        return isNoStudentIds;
    }

    //查找role_id是否有102以外的值
    public boolean isNo102(List<UserRole> userRoles) {
        System.out.println("55555555555555555555555555555555555");
        return userRoles.stream().anyMatch(role -> role.getRoleId() != 102);
    }

    // 根据 userIdList 获取 UserRole 列表
    public Map<Long, List<UserRole>> getUserRolesByUserIds(List<Long> userIdList) {
        System.out.println("66666666666666666666666666666666666");
        System.out.println(userIdList);
        if (userIdList == null || userIdList.isEmpty()) {
            return Collections.emptyMap();
        }
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, userIdList));
        System.out.println("77777777777777777777777777777777777777");
        return userRoles.stream().collect(Collectors.groupingBy(UserRole::getUserId));
    }

}

