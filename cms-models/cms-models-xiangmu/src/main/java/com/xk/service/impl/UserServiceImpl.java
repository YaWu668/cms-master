package com.xk.service.impl;

import java.util.Arrays;
import cn.hutool.db.Db;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.xk.domain.dto.InquireUserDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.domain.vo.detail.Student;
import com.xk.entity.User;
import com.xk.entity.UserRole;
import com.xk.mapper.UserMapper;
import com.xk.mapper.UserRoleMapper;
import com.xk.service.UserRoleService;
import com.xk.service.UserService;
import com.xk.utils.BeanCopyUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.management.Query;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private  UserRoleService userRoleService;

    @Override
    public PageDTO<UserListVo> getUserList(InquireUserDto inquireUserDto) {
        //查询全部用户的数据，如果有查询条件，则根据条件查询
        Page<User> page = inquireUserDto.toMpPageDefaultSortByCreateTimeDesc();
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(StringUtils.isNotBlank(inquireUserDto.getUserName()), User::getUserName, inquireUserDto.getUserName())
                .like(StringUtils.isNotBlank(inquireUserDto.getNickName()), User::getNickName, inquireUserDto.getNickName())
                .eq(User::getStatus, 0);
        // 获取查询结果
        List<User> userList = this.list(queryWrapper);
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        List<Long> userIdList = userList.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
        System.out.println(userIdList);
        List<Long> noStudentListIds =  userRoleService.isStudent(userIdList);
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB");
        LambdaQueryWrapper<User> queryWrapper_user = new LambdaQueryWrapper<>();
        System.out.println("CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC");
        queryWrapper_user
                .in(User::getUserId, noStudentListIds);
        this.page(page, queryWrapper_user);
        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        return PageDTO.of(page, UserListVo.class);

        }
    }


