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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户和角色关联表实现类
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {


    public Map<Long, List<UserRole>> getUserRolesByUserIds(List<Long> userIds) {
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserRole::getUserId, userIds);

        List<UserRole> userRoleList = list(queryWrapper);

        return userRoleList.stream()
                .collect(Collectors.groupingBy(UserRole::getUserId));
    }
}

