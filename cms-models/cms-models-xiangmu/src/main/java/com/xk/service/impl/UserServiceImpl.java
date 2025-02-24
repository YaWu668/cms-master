package com.xk.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.utils.ExcelUtils;
import com.cms.system.api.domain.pojo.SysUser;
import com.xk.constant.RoleConstant;
import com.xk.domain.dto.ProjectPersonDto;
import com.xk.domain.dto.UserBindingRoleDto;
import com.xk.domain.vo.SearchPersonListVo;
import com.xk.domain.vo.detail.Student;
import com.cms.common.core.exception.ServiceException;
import com.cms.common.core.utils.StringUtils;
import com.xk.domain.dto.InquireUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.entity.Role;
import com.xk.entity.User;
import com.xk.entity.UserRole;
import com.xk.mapper.UserMapper;
import com.xk.service.RoleService;
import com.xk.service.UserRoleService;
import com.xk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    /**
     * 用户
     */
    private final UserMapper userMapper;

    /**
     * 用户关联角色
     */
    private final UserRoleService userRoleService;
    /**
     * 角色
     */
    private final RoleService roleService;

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

    @Override
    @Transactional
    public boolean importUserBindingRole(MultipartFile file, String rolekey) {
        //1.解析用户信息,同时校验不为空和学号不能重复(没有查数据库)
        List<UserBindingRoleDto> list = parsingUserBindingRoleExecl(file);
        //2.根据角色key查询,如果不存在抛出异常
        if (StrUtil.isBlank(rolekey)){
            throw new ServiceException("角色key不能为null");
        }
        Role role = roleService.selectByRoleKey(rolekey);
        //4.校验用户信息是不是都存在和正确(查数据库),同时校验这些用户是否已经绑定过角色,如果已经绑定过就抛出异常
        List<User> users = checkUserBindingRoleDto(list, role);//返回校验完成可以绑定用户信息
        //6.写入数据库
        boolean r=userBindRole(role,users);
        return r;
    }

    /**
     * 用户批量绑定指导角色
     * @param role 指定角色
     * @param users 用户信息
     * @return  true:绑定成功 false:绑定失败
     */
    private boolean userBindRole(Role role,List<User> users) {
        List<UserRole> userRoleList = users.stream()
                .map(e -> new UserRole(e.getUserId(), role.getRoleId()))
                .collect(Collectors.toList());
        if (userRoleService.saveBatch(userRoleList)){
            return true;
        }
        return false;
    }

    /***
     * 校验这些用户是否已经绑定过角色,如果已经绑定过就抛出异常
     * @param list 导入的用户信息
     * @param role 角色信息
     */
    private void checkTheUserToWhomTheRoleIsBound(List<UserBindingRoleDto> list, Role role,List<User> userList) {
        //1.设置hashMap集合 key是:userName value是:User
        Map<String, User> userMap = userList.stream()
                .collect(Collectors.toMap(User::getUserName, e -> e));

        //2.再根据 用户ID 和 角色id去查询 userRole已经存在关联关系,再转为 set集合存在userID
        List<UserRole> userRoles = selectByUserRoleList(role, userList);
        Set<Long> userIdSet = userRoles.stream()
                .map(UserRole::getUserId)
                .collect(Collectors.toSet());
        //3.遍历用户信息,进行校验是否存在数据库
        // 先获取dto -->根据userName查询 userMap获取到UserId,再根据userId去查询userIdSet判断判断是否存在
        //  如果存在记录保存信息
        ArrayList<String> msg = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ArrayList<String> rowMsg = new ArrayList<>();
            UserBindingRoleDto dto = list.get(i);
            User user = userMap.get(dto.getUserName());
            if (userIdSet.contains(user.getUserId())){
                rowMsg.add("第"+(i+2)+"行用户名:"+dto.getUserName()+"已经绑定过角色");
            }

            if(CollUtil.isNotEmpty(rowMsg)){
                msg.addAll(rowMsg);
            }
        }

        if (CollUtil.isNotEmpty(msg)){
            throw new ServiceException("下面用户意见绑定过"+role.getRoleName()+":"+msg);
        }
    }

    /**
     * 根据userId和角色id查询已经存在的关系
     * @param role
     * @param userList
     * @return 返回元素数量和 userList的数量一致代表,需要导入元素全部都有对应绑定关系,直接抛出异常
     */
    private List<UserRole> selectByUserRoleList(Role role, List<User> userList) {
        //转为UserRole实体类
        List<UserRole> userRoles = userList.stream()
                .map(user -> new UserRole(user.getUserId(), role.getRoleId()))
                .collect(Collectors.toList());
        QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();

        // 动态构造 SQL 语句
        queryWrapper.and(qw -> {
            for (UserRole userRole : userRoles) {
                qw.or().eq("user_id", userRole.getUserId()).eq("role_id", userRole.getRoleId());
            }
        });
        // 查询数据库
        List<UserRole> list = userRoleService.list(queryWrapper);
        if (userList.size() == list.size()){
            throw new ServiceException("导入用户信息,全部都已经存在数据库");
        }
        return list;
    }

    /**
     * 校验用户信息是不是都存在和正确(查数据库)
     * @param list 导入的用户信息
     * @param role 角色信息
     * @return 返回用户信息(用用于后续的插入,优化查询数据库次数)
     */
    private List<User> checkUserBindingRoleDto(List<UserBindingRoleDto> list,Role role) {
        //1.根据学号查询出来用户信息,再转为key:学号 value:名字
        List<String> userNameList = list.stream().map(e -> e.getUserName()).collect(Collectors.toList());
        List<User> userList = this.lambdaQuery()
                .in(User::getUserName, userNameList)
                .list();
        if (CollUtil.isEmpty(userList)){//优化,如果全部不在时候直接抛出异常
            throw new ServiceException("导入用户信息,全部都不存在数据库");
        }
        Map<String, String> dataMap = userList.stream()
                .collect(Collectors.toMap(User::getUserName, User::getNickName));

        //2.再遍历用户信息,进行校验是否存在数据库
        ArrayList<String> msg = new ArrayList<>();//返回错误信息
        for (int i = 0; i < list.size(); i++) {
            ArrayList<String> rowMsg = new ArrayList<>();
            UserBindingRoleDto dto = list.get(i);
            if(dataMap.containsKey(dto.getUserName())){
                if (!dataMap.get(dto.getUserName()).equals(dto.getNickName())){
                    rowMsg.add("第"+(i+2)+"行,账号:"+dto.getUserName()+"与数据库中的用户名不一致");
                }
            }else {
                rowMsg.add("第"+(i+2)+"行,账号:"+dto.getUserName()+"不存在数据库");
            }
            if (CollUtil.isNotEmpty(rowMsg)){
                msg.addAll(rowMsg);
            }
        }
        //3.有错误信息就抛出异常
        if (CollUtil.isNotEmpty(msg)){
            throw new ServiceException(CollUtil.join(msg,"\n"));
        }

        //4.校验这些用户是否已经绑定过角色,如果已经绑定过就抛出异常
        checkTheUserToWhomTheRoleIsBound(list,role,userList);

        return userList;
    }

    /**
     * 解析用户绑定角色信息,同时校验,不为空和学号不能重复(没有查数据库)
     * @param file
     */
    private List<UserBindingRoleDto> parsingUserBindingRoleExecl(MultipartFile file) {
        List<UserBindingRoleDto> data;
        try {
            data = ExcelUtils.read(file, UserBindingRoleDto.class);
        } catch (IOException e) {
            throw new RuntimeException("excel格式不对,解析excel出错");
        }

        if(CollUtil.isEmpty(data)){
            throw new ServiceException("导入数据为空");
        }

        //1.校验是否为空
        UserBindingRoleNotEmpty(data);
        //2.校验是否有重复学号
        UserBindingRoleRepeatData(data);
        return data;
    }

    /**
     * 重复学号校验
     * @param data
     */
    private void UserBindingRoleRepeatData(List<UserBindingRoleDto> data) {
        //1.错误信息
        List<String> messages = new ArrayList<>();
        //2.记录学号
        Set<String> userNameSet = new HashSet<>();

        for (int i = 0; i < data.size(); i++) {
            UserBindingRoleDto dto = data.get(i);
            //3.判断当前行是否有重复的
            if(userNameSet.contains(dto.getUserName())){
                messages.add("第" + (i + 2) + "行");
            }
            userNameSet.add(dto.getUserName());
        }
        //4.判断响集合有数据抛出异常
        if(CollUtil.isNotEmpty(messages)){
            throw new ServiceException("学号不可以有重复的,重复行:"+CollUtil.join(messages,";"));
        }

    }

    /**
     * 校验不为空
     * @param data
     */
    private void UserBindingRoleNotEmpty(List<UserBindingRoleDto> data) {
        //1.错误信息
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            UserBindingRoleDto dto = data.get(i);
            List<String> rowMessages = new ArrayList<>();//记录行错写信息
            //2.判断当前行是否是为空
            if(StrUtil.isBlank(dto.getUserName())){
                rowMessages.add("账号");
            }
            if (StrUtil.isBlank(dto.getNickName())){
                rowMessages.add("名字");
            }
            //3.当前行有错误信息进行记录
            if(CollUtil.isNotEmpty(rowMessages)){
                messages.add("第" + (i + 2) + "行："+CollUtil.join(rowMessages,","));
            }
        }

        //4.判断响集合有数据抛出异常
        if(CollUtil.isNotEmpty(messages)){
            throw new ServiceException("数据不允许为空:"+CollUtil.join(messages,";"));
        }
    }
}

