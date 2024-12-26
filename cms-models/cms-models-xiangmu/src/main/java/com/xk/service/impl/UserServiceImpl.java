package com.xk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xk.entity.User;
import com.xk.mapper.UserMapper;
import com.xk.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}

