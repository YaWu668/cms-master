package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.domain.vo.detail.Student;
import com.xk.entity.User;
import com.xk.mapper.UserMapper;
import com.xk.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public List<User> selectByUserIds(List<Long> userIds) {
        // 去重
        List<Long> list = userIds.stream().distinct().collect(Collectors.toList());
        // 查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getUserId, list);
        return this.list(wrapper);
    }
}

