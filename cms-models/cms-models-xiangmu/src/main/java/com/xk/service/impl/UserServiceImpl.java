package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.xk.domain.dto.InquireUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.entity.User;
import com.xk.mapper.UserMapper;
import com.xk.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleServiceImpl userRoleServiceImpl;
    @Override
    public PageDTO<UserListVo> getUserList(InquireUserDTO inquireUserDto) {
        Page<User> page = inquireUserDto.toMpPageDefaultSortByCreateTimeDesc();
        //获取符合条件的id集合
        List<Long> userList = getUserIds(inquireUserDto);
        //获取角色不全是学生的id集合
        List<Long> isNoStudentUser = userRoleServiceImpl.isNoStudent(userList);

        if (isNoStudentUser.isEmpty()){
            throw new ServiceException("没有符合要求的用户",444);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getUserId, isNoStudentUser);
        this.page(page,queryWrapper);
        return PageDTO.of(page, UserListVo.class);
    }

    //获取符合条件的id集合
    public List<Long> getUserIds(InquireUserDTO inquireUserDto) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(User::getStatus,0)
                .like(StringUtils.isNotEmpty(inquireUserDto.getUserName()), User::getUserName, inquireUserDto.getUserName())
                .like(StringUtils.isNotEmpty(inquireUserDto.getNickName()), User::getNickName, inquireUserDto.getNickName());
        List<User> userIds = userMapper.selectList(queryWrapper);
        if (userIds == null) {
            return Collections.emptyList();
        } else {
            return userIds.stream().map(User::getUserId).collect(Collectors.toList());
        }
    }
}

