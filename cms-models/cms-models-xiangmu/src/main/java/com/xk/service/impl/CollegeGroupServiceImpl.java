package com.xk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cms.common.core.web.domain.Response;
import com.xk.domain.dto.collegeListDto;
import com.xk.entity.CollegeGroup;
import com.xk.mapper.CollegeGroupMapper;
import com.xk.service.CollegeGroupService;
import com.xk.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 学院组表(CollegeGroup)表服务实现类
 *
 * @author yawu
 * @since 2024-12-05 21:21:54
 */
@Service
public class CollegeGroupServiceImpl extends ServiceImpl<CollegeGroupMapper, CollegeGroup> implements CollegeGroupService {

    @Override
    public Response<List<collegeListDto>> collegeList(String name) {
        new LambdaQueryWrapper<CollegeGroup>()
                .eq(CollegeGroup::getStatus,0)
                .like(name!= null&& name.length() > 0, CollegeGroup::getName, name)
                .orderByAsc(CollegeGroup::getCreateTime);
        List<CollegeGroup> collegeGroups = this.list();
        List<collegeListDto> listDtos = BeanCopyUtils.copyBeans(collegeGroups, collegeListDto.class);
        return Response.success(listDtos);
    }
}

