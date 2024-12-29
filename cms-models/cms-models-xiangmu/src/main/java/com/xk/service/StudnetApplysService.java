package com.xk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xk.entity.StudnetApplys;

import java.util.List;


/**
 * 学生报名表(StudnetApplys)表服务接口
 *
 * @author makejava
 * @since 2024-12-06 00:37:22
 */
public interface StudnetApplysService extends IService<StudnetApplys> {
    /**
     * 获取用户自己报名的项目id
     * @return
     */
    List<Long> getUserApplyListId();
}

