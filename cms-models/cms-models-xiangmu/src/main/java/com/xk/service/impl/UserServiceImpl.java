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
        System.out.println(inquireUserDto.getUserName());
        queryWrapper
                .like(StringUtils.isNotBlank(inquireUserDto.getUserName()), User::getUserName, inquireUserDto.getUserName())
                .like(StringUtils.isNotBlank(inquireUserDto.getNickName()), User::getNickName, inquireUserDto.getNickName())
                .eq(User::getStatus, 0);
        IPage<User> resultPage = this.page(page, queryWrapper);

        // 获取查询结果
        List<User> userList = resultPage.getRecords();
        List<Long> userIdList = userList.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());

        Map<Long, List<UserRole>> userRoleMap = userRoleService.getUserRolesByUserIds(userIdList);

        // 检查所有 user 是否全为学生
        boolean allRoleIdsAre102 = userRoleMap.values().stream()
                .allMatch(roles -> roles.stream().allMatch(role -> role.getRoleId() == 102));

        // 根据检查结果进行相应的处理
        if (allRoleIdsAre102) {
            // 所有 role_id 都是 102
            List<UserListVo> emptyUserListVoList = Collections.emptyList();
            throw new ServiceException("查询不存在老师",444);
        } else {
            // 存在 role_id 不是 102
            // 优先排序老师
            List<User> sortedUserList = userList.stream()
                    .sorted((u1, u2) -> {
                        List<UserRole> roles1 = userRoleMap.getOrDefault(u1.getUserId(), Collections.emptyList());
                        List<UserRole> roles2 = userRoleMap.getOrDefault(u2.getUserId(), Collections.emptyList());

                        boolean isTeacher1 = roles1.stream().anyMatch(role -> role.getRoleId() == 103);
                        boolean isTeacher2 = roles2.stream().anyMatch(role -> role.getRoleId() == 103);

                        if (isTeacher1 && !isTeacher2) {
                            return -1; // u1 是老师，u2 不是老师
                        } else if (!isTeacher1 && isTeacher2) {
                            return 1; // u1 不是老师，u2 是老师
                        } else {
                            return 0; // 都是老师或都不是老师，按默认顺序
                        }
                    })
                    .collect(Collectors.toList());

            System.out.println("111111111111111111111111111111111"+sortedUserList);
            // 转换为 UserListVo
            List<UserListVo> userListVoList = sortedUserList.stream()
                    .map(user -> BeanCopyUtils.copyBean(user, UserListVo.class))
                    .collect(Collectors.toList());

            return new PageDTO<>(page.getTotal(), page.getPages(), userListVoList);
        }
    }


}

