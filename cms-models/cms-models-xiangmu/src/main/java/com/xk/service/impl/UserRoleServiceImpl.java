package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.Role;
import com.xk.entity.User;
import com.xk.entity.UserRole;
import com.xk.mapper.UserMapper;
import com.xk.mapper.UserRoleMapper;
import com.xk.service.UserRoleService;
import com.xk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户和角色关联表实现类
 */
@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    /**
     * 用户和角色关联表
     */
    private final UserRoleMapper userRoleMapper;

    @Override
    public List<Long> isNoStudent(List<Long> userIdList) {
        List<Long> isNoStudentIds = new ArrayList<>();
        Map<Long, List<UserRole>> userRoleMap = getUserRolesByUserIds(userIdList);
        userIdList.forEach(userId -> {
            List<UserRole> roles = userRoleMap.getOrDefault(userId, Collections.emptyList());
            if (isNo102(roles)) {
                isNoStudentIds.add(userId);
            }
        });
        return isNoStudentIds;
    }

    //查找role_id是否有102以外的值
    public boolean isNo102(List<UserRole> userRoles) {
        return userRoles.stream().anyMatch(role -> role.getRoleId() != 102);
    }

    // 根据 userIdList 获取 UserRole 列表

    public Map<Long, List<UserRole>> getUserRolesByUserIds(List<Long> userIdList) {

        if (userIdList == null || userIdList.isEmpty()) {
            return Collections.emptyMap();
        }
        List<UserRole> userRoles = userRoleMapper.selectList(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, userIdList));
        return userRoles.stream().collect(Collectors.groupingBy(UserRole::getUserId));
    }

    @Override
    public List<Long> getUserIdsByRoleList(List<Role> roleList) {
        //角色集合转为角色id集合
        if (roleList.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> list = roleList.stream()
                .map(role -> role.getRoleId())
                .distinct()
                .collect(Collectors.toList());

        List<Long> userIds = this.lambdaQuery()
                .in(UserRole::getRoleId, list)
                .list()
                .stream()
                .map(UserRole::getUserId)
                .distinct()
                .collect(Collectors.toList());
        //返回结果
        return userIds.size()==0?Collections.emptyList():userIds;
    }
}

