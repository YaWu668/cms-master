package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.entity.TeacherApplys;
import com.xk.mapper.TeacherApplysMapper;
import com.xk.service.TeacherApplysService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 老师报名表(TeacherApplys)表服务实现类
 *
 * @author makejava
 * @since 2024-12-06 01:05:55
 */
@Service
public class TeacherApplysServiceImpl extends ServiceImpl<TeacherApplysMapper, TeacherApplys> implements TeacherApplysService {

    @Override
    public List<Long> getUserApplyListId() {
        Long userId = SecurityUtils.getUserId();
        LambdaQueryWrapper<TeacherApplys> wrapper = new LambdaQueryWrapper<TeacherApplys>()
                .eq(TeacherApplys::getUserId, userId);
        return this.list(wrapper).stream()
                .map(TeacherApplys::getProjectId)
                .distinct()
                .collect(Collectors.toList());
    }
}

