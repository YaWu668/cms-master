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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        //1构造条件
        LambdaQueryWrapper<CollegeGroup> wrapper = new LambdaQueryWrapper<CollegeGroup>()
                .eq(CollegeGroup::getStatus, 0)
                .like(name != null && name.length() > 0, CollegeGroup::getName, name)
                .orderByAsc(CollegeGroup::getCreateTime);
        //2.查询
        List<CollegeGroup> collegeGroups = this.list(wrapper);
        List<collegeListDto> listDtos = BeanCopyUtils.copyBeans(collegeGroups, collegeListDto.class);
        return Response.success(listDtos);
    }

    @Override
    public List<CollegeGroup> selectByIds(List<Long> ids) {
        //1.去重
        List<Long> list = ids.stream()
                .distinct()
                .collect(Collectors.toList());
        //2.构建条件
        List<CollegeGroup> groupList = this.lambdaQuery()
                .in(CollegeGroup::getCollegeGroupId, list)
                .list();
        return groupList;
    }
}

