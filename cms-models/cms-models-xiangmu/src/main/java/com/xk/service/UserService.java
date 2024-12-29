package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.domain.dto.InquireUserDTO;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.admin.UserListVo;
import com.xk.entity.User;


/**
 * 用户信息表(User)表服务接口
 *
 * @author yawu
 * @since 2024-12-05 01:00:12
 */
public interface UserService extends IService<User> {

    /**
     * 分页查询全部用户信息
     * @param inquireUserDto
     * @return
     */
    PageDTO<UserListVo> getUserList(InquireUserDTO inquireUserDto);
}

