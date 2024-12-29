package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.TeacherApplys;

import java.util.List;


/**
 * 老师报名表(TeacherApplys)表服务接口
 *
 * @author makejava
 * @since 2024-12-06 01:05:55
 */
public interface TeacherApplysService extends IService<TeacherApplys> {
    /**
     * 获取用户自己报名的项目id
     * @return 项目列表id
     */
    List<Long> getUserApplyListId();
}

