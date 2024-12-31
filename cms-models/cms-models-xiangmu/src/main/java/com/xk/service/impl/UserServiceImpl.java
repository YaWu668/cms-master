package com.xk.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.system.api.domain.pojo.SysUser;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.ProjectPersonDto;
import com.xk.domain.vo.SearchPersonListVo;
import com.xk.domain.vo.detail.Student;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.xk.domain.dto.InquireUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.entity.User;
import com.xk.mapper.UserMapper;
import com.xk.service.UserRoleService;
import com.xk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;


    private final UserRoleService userRoleService;

    @Override
    public List<User> selectByUserIds(List<Long> userIds) {
        // 去重
        List<Long> list = userIds.stream().distinct().collect(Collectors.toList());
        // 检查列表是否为空
        if (list.isEmpty()) {
            return Collections.emptyList(); // 返回空结果
        }
        // 查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .in(User::getUserId, list);
        return this.list(wrapper);
    }

    @Override
    public PageDTO<UserListVo> getUserList(InquireUserDTO inquireUserDto) {
        Page<User> page = inquireUserDto.toMpPageDefaultSortByCreateTimeDesc();
        //获取符合条件的id集合
        List<Long> userList = getUserIds(inquireUserDto);
        //获取角色不全是学生的id集合
        List<Long> isNoStudentUser = userRoleService.isNoStudent(userList);

        if (isNoStudentUser.isEmpty()){
            throw new ServiceException("没有符合要求的用户",444);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getUserId, isNoStudentUser);
        this.page(page,queryWrapper);
        return PageDTO.of(page, UserListVo.class);
    }

    /*@Override
    public PageDTO<SearchPersonListVo> searchPerson(ProjectPersonDto projectPersonDto,List<Long> userIds) {
        //非空判断
        if (userIds.isEmpty()){
            return PageDTO.empty();
        }
        //1.分页请求
        Page<User> page = projectPersonDto.toMpPageDefaultSortByCreateTimeDesc();
        //2.构造查询条件
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .in(User::getUserId, userIds)
                .like(StrUtil.isNotBlank(projectPersonDto.getParameter()), User::getUserName, projectPersonDto.getParameter())
                .like(StrUtil.isNotBlank(projectPersonDto.getParameter()), User::getNickName, projectPersonDto.getParameter());
        this.page(page, queryWrapper);

        return PageDTO.of(page,SearchPersonListVo.class);
    }*/

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


    @Override
    public PageDTO<SearchPersonListVo> getUsersByRolesAndKeyword( ProjectPersonDto projectPersonDto) {
        ArrayList<String> roleKey = new ArrayList<>();
        //1.判断是不是学生
        if(projectPersonDto.getIsStudent()==null){
            throw new ServiceException("isStudent不能为null",444);
        }
        if(projectPersonDto.getIsStudent()){
            roleKey.add(RoleConstant.STUDENT);
        }else {
            roleKey.add(RoleConstant.TEACHER);
            roleKey.add(RoleConstant.COLLEGE);
            roleKey.add(RoleConstant.EXPERT);
            roleKey.add(RoleConstant.ADMIN);
        }

        // 构造 LambdaQueryWrapper
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .inSql(User::getUserId,
                "SELECT ur.user_id " +
                        "FROM sys_user_role ur " +
                        "JOIN sys_role r ON ur.role_id = r.role_id " +
                        "WHERE r.role_key IN (" +
                        roleKey.stream()
                                .map(role -> "'" + role + "'")
                                .collect(Collectors.joining(",")) +
                        ")" +
                        "AND r.del_flag = '0'"
        )
                .and(StrUtil.isNotBlank(projectPersonDto.getParameter()),wrapper ->
                wrapper.like(User::getUserName, projectPersonDto.getParameter())
                        .or()
                        .like(User::getNickName, projectPersonDto.getParameter())
        );
        //1.分页
        Page<User> page = projectPersonDto.toMpPageDefaultSortByCreateTimeDesc();
        this.page(page, queryWrapper);
        return PageDTO.of(page,SearchPersonListVo.class);
    }
}

