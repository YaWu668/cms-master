package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.web.domain.Response;
import com.cms.common.security.utils.SecurityUtils;
import com.xk.domain.dto.ApplyForStudent;
import com.xk.domain.dto.PageDTO;
import com.xk.domain.vo.student.StudentApplyVo;
import com.xk.entity.Project;
import com.xk.entity.StudnetApplys;
import com.xk.mapper.StudnetApplysMapper;
import com.xk.service.StudnetApplysService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学生报名表(StudnetApplys)表服务实现类
 *
 * @author makejava
 * @since 2024-12-06 00:37:22
 */
@Service
@RequiredArgsConstructor
public class StudnetApplysServiceImpl extends ServiceImpl<StudnetApplysMapper, StudnetApplys> implements StudnetApplysService {

    private final StudnetApplysMapper studnetApplysMapper;

    @Override
    public Response getBindStudent(int currentPage, int pageSize, String StudentId, String StudentName, String StudentSchool) {
        //当前登录用户ID
        Long userId = SecurityUtils.getLoginUser().getUserid();

        //分页
        Page<StudnetApplys> page = new Page<>(currentPage, pageSize);
        //获取该教师绑定的所有学生id
        List<Long> StudentId_list = studnetApplysMapper.getMyBindStudentId(userId);
        //构造查询语句
        LambdaQueryWrapper<StudnetApplys> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(StudnetApplys::getUserId, StudentId_list);

        if (StudentId != null && !StudentId.trim().isEmpty()) {
            queryWrapper.like(StudnetApplys::getUserId, StudentId);
        }

        if (StudentName != null && !StudentName.trim().isEmpty()) {
            queryWrapper.like(StudnetApplys::getName, StudentName);
        }

        if (StudentSchool != null && !StudentSchool.trim().isEmpty()) {
            queryWrapper.like(StudnetApplys::getCollegeGroupId, StudentSchool);
        }

        //分页查询
        Page<StudnetApplys> studentPage = page(page,queryWrapper);



        return Response.success(PageDTO.of(studentPage, StudentApplyVo.class),"获取成功");
    }
}

