package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.InquireUserDto;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.domain.vo.detail.Student;
import com.xk.entity.User;


/**
 * 用户信息表(User)表服务接口
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
public interface UserService extends IService<User> {


    /**
     * 分页查询用户列表
     * @param inquireUserDto
     * @return
     */
    PageDTO<UserListVo> getUserList(InquireUserDto inquireUserDto);
}

