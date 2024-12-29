package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.entity.StudnetApplys;
import com.xk.mapper.StudnetApplysMapper;
import com.xk.service.StudnetApplysService;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 学生报名表(StudnetApplys)表服务实现类
 *
 * @author makejava
 * @since 2024-12-06 00:37:22
 */
@Service
public class StudnetApplysServiceImpl extends ServiceImpl<StudnetApplysMapper, StudnetApplys> implements StudnetApplysService {

    @Override
    public List<Long> getUserApplyListId() {
        //1.获取用户自己的id
        Long userId = SecurityUtils.getUserId();
        //2.根据用户的id查询
        LambdaQueryWrapper<StudnetApplys> wrapper = new LambdaQueryWrapper<StudnetApplys>()
                .eq(StudnetApplys::getUserId, userId);
        List<StudnetApplys> list = this.list(wrapper);
        //3.返回项目id
        return list.stream()
                .map(StudnetApplys::getUserId)
                .distinct()
                .collect(Collectors.toList());
    }
}

